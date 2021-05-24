package org.agmip.translators.annotated;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import io.vavr.collection.List;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.agmip.ace.AceDataset;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.components.ComponentState;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.parsers.Sidecar2Parser;
import org.agmip.translators.annotated.data.DataRegistry;
import org.agmip.translators.interfaces.IInputTranslator;
import org.agmip.translators.interfaces.WithWorkDir;

public class Sc2Translator implements IInputTranslator, WithWorkDir {
  private List<String> _filenames;
  private Path _workingDir;

  public Sc2Translator() {
    _filenames = List.empty();
  }

  @Override
  public void addFile(String file) {
    _filenames = _filenames.append(file);
  }

  public List<String> getInputFilenames() {
    return _filenames;
  }

  @Override
  public AceDataset translate() {
    System.out.println("-- Stage 0: Validate translator configuration");
    if (_workingDir == null) {
      System.err.println("Working directory not set.");
      return null;
    }
    System.out.println("-- Stage 1: Validating Sidecar2 Files");
    System.out.println("Checking " + _filenames.size() + " files.");
    List<Validation<String, Path>> sidecar2Files = _filenames.map(this::realizePath);
    // TODO Add customized API logging facilities to return errors
    List<String> missingFiles =
        sidecar2Files.filter(Validation::isInvalid).map(Validation::getError);
    if (missingFiles.size() > 0) {
      missingFiles.forEach(System.err::println);
    }
    if (sidecar2Files.size() - missingFiles.size() == 0) {
      System.err.println("No files found to process.");
      return null;
    }
    List<Validation<String, Sidecar2>> scv =
        sidecar2Files
            .filter(Validation::isValid)
            .map(
                f ->
                    Try.of(() -> Sidecar2Parser.parse(f.get().toFile(), _workingDir))
                        .toValidation(ex -> "IO Error [" + f.get() + "]:" + ex.getMessage()));
    scv.filter(Validation::isInvalid).forEach(System.out::println);
    if (scv.forAll(Validation::isInvalid)) {
      System.err.println("Could not parse any SC2 files.");
      return new AceDataset();
    }
    List<Sidecar2> sidecars = scv.filter(Validation::isValid).map(Validation::get);
    System.out.println(
        "\t[" + sidecars.size() + "/" + sidecar2Files.size() + "] SC2 files were parsable.");
    List<Sidecar2> validSidecars = sidecars.filter(this::checkSC2Validity);
    System.out.println("Valid sidecars: " + validSidecars.size());

    System.out.println("-- Stage 2: Preflight files");

    System.out.println("Checking for file availability...");
    List<Validation<Path, Path>> fileValidation = validSidecars
      .flatMap(Sidecar2::files)
      .map(f -> Path.of(f.location()))
      .distinct()
      .map(p -> Files.isReadable(p) ?
        Validation.valid(p) :
        Validation.invalid(p));
    System.out.println("Found " + fileValidation.filter(Validation::isValid).size() + "/" + fileValidation.size() + " files.");
    if (fileValidation.find(Validation::isInvalid).isDefined()) {
      fileValidation.forEach(f -> {
          if (f.isValid()) {
            System.out.println(f.get().getFileName() + " ... FOUND");
          } else {
            System.out.println(f.getError().getFileName() + "... MISSING");
          }
        });
    }
    List<Path> invalidFiles = fileValidation.filter(Validation::isInvalid).map(Validation::getError);
    List<Sidecar2> translatableSidecars = validSidecars.filter(s -> s.files().forAll(f -> ! invalidFiles.contains(Path.of(f.location()))));
    System.out.println("Translatable sidecar files: " + translatableSidecars.size());
    return new AceDataset();
  }

  @Override
  public void setWorkDirectory(Path dir) {
    _workingDir = dir;
  }

  @Override
  public Path getWorkDirectory() {
    return _workingDir;
  }

  private Validation<String, Path> realizePath(String file) {
    Path rp = _workingDir.resolve(file);
    if (Files.isReadable(rp)) {
      return Validation.valid(rp);
    } else {
      return Validation.invalid("Unable to open file: " + rp);
    }
  }

  private boolean checkSC2Validity(Sidecar2 sc) {
    System.out.println("\t-- Checking " + Path.of(sc.self()).getFileName());
    if (sc.isValid()) {
      System.out.println("\t\t -- Fully validated: " + sc.isValid());
      return true;
    }

    System.out.println(
        "\t\t -- All file entries valid: " + (sc.fileState() == ComponentState.COMPLETE));
    if (sc.fileState() != ComponentState.COMPLETE) {
      System.out.println(
          "\t\t -- Any file entries valid: " + (sc.fileState() == ComponentState.PARTIAL));
      sc.rawFiles()
          .forEachWithIndex(
              (f, i) -> {
                if (f.isValid()) {
                  System.out.println("\t\t    " + i + ". valid");
                  Sc2FileReference fr = f.get();
                  System.out.println(
                      "\t\t\t -- All sheet entries valid: "
                          + (fr.sheetState() == ComponentState.COMPLETE));
                  if (fr.sheetState() != ComponentState.COMPLETE) {
                    System.out.println(
                        "\t\t\t -- Any sheet entries valid: "
                            + (fr.sheetState() == ComponentState.PARTIAL));
                    fr.rawSheets()
                        .forEachWithIndex(
                            (s, si) -> {
                              if (s.isValid()) {
                                System.out.println("\t\t\t    " + si + ". valid");
                                Sc2Sheet sr = s.get();
                                System.out.println(
                                    "\t\t\t\t -- All rules valid: "
                                        + (sr.rulesState() == ComponentState.COMPLETE));
                                if (sr.rulesState() != ComponentState.COMPLETE) {
                                  System.out.println(
                                      "\t\t\t\t -- Any rules valid: "
                                          + (sr.rulesState() == ComponentState.PARTIAL));
                                  sr.rawRules()
                                      .forEachWithIndex(
                                          (r, ri) -> {
                                            if (r.isValid()) {
                                              System.out.println("\t\t\t\t    " + ri + ". valid");
                                            } else {
                                              System.out.println("\t\t\t\t    " + ri + ". invalid");
                                              r.getError()
                                                  .forEach(
                                                      reason ->
                                                          System.out.println(
                                                              "\t\t\t\t        - " + reason));
                                            }
                                          });
                                }
                              } else {
                                System.out.println("\t\t\t    " + si + ". invalid");
                                s.getError()
                                    .forEach(
                                        reason -> System.out.println("\t\t\t        - " + reason));
                              }
                            });
                  }
                } else {
                  System.out.println("\t\t    " + i + ". invalid");
                  f.getError().forEach(reason -> System.out.println("\t\t        - " + reason));
                }
              });
    }

    System.out.println(
        "\t\t -- All relations valid: " + (sc.relationState() == ComponentState.COMPLETE));
    if (sc.relationState() != ComponentState.COMPLETE) {
      System.out.println(
          "\t\t -- Any relations valid: " + (sc.relationState() == ComponentState.PARTIAL));
      sc.rawRelations()
          .forEachWithIndex(
              (r, i) -> {
                if (r.isValid()) {
                  System.out.println("\t\t    " + i + ". Valid");
                } else {
                  System.out.println("\t\t    " + i + ". Invalid");
                  r.getError().forEach(reason -> System.out.println("\t\t        - " + reason));
                }
              });
    }
    System.out.println(sc.fileState()+ ", " + sc.relationState());
    return sc.fileState().ordinal() < ComponentState.INVALID.ordinal() && sc.relationState().ordinal() < ComponentState.INVALID.ordinal();
  }

  private List<Sidecar2> sidecarsWithReachableFiles(List<Sidecar2> sidecars, List<Path> unreachable) {
    return sidecars.filter(s -> s.files().forAll(f -> unreachable.contains(Path.of(f.location()))));
  }

  private DataRegistry buildRegistry(List<Sidecar2> sidecars) {
    DataRegistry reg = new DataRegistry();
    sidecars.forEach(
        s -> s.files().forEach(file -> file.sheets().forEach(sheet -> reg.add(file, sheet))));
    return reg;
  }
}
/*
  public List<String> getRealizationOrder() {
    Graph<DataContext, DefaultEdge> rGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
    boolean addedPrimary;
    boolean addedForeign;
    for (Sc2Relation rel : _validRelations) {
      Sc2Relation.Sc2RelationPart primary = rel.getPrimary();
      Sc2Relation.Sc2RelationPart foreign = rel.getForeign();
      String primaryKey = primary.getFile() + "$$" + primary.getSheet();
      String foreignKey = foreign.getFile() + "$$" + foreign.getSheet();
      DataContext p = _registry.get(primaryKey);
      DataContext f = _registry.get(foreignKey);
      rGraph.addVertex(p);
      rGraph.addVertex(f);
      rGraph.addEdge(p, f);
    }
    List<String> relOrder = new ArrayList<>();
    Iterator<DataContext> iter = new TopologicalOrderIterator<>(rGraph);
    iter.forEachRemaining(
      v ->
        relOrder.add(
          v.toString(false) + " - " + rGraph.degreeOf(v) + "[" + v.maxBound() + "]"));
    Collections.reverse(relOrder);
    return relOrder;
  }

  public Map<String, List<RawDataRow>> parseFiles() {
    return null;
  }
}
*/
