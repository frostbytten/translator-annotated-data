package org.agmip.translators.annotated;

import static io.vavr.API.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.core.*;
import io.vavr.*;
import io.vavr.control.*;
import org.agmip.ace.AceDataset;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.parsers.*;
import org.agmip.translators.interfaces.IInputTranslator;
import org.agmip.translators.interfaces.WithWorkDir;

public class Sc2Translator implements IInputTranslator, WithWorkDir {
  private final List<String> _filenames;
  private Path _workingDir;

  public Sc2Translator() {
    _filenames = new ArrayList<>();
  }

  @Override
  public void addFile(String file) {
    _filenames.add(file);
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
    List<Either<String, Path>> sidecar2Files =
        _filenames.stream().map(this::realizePath).collect(Collectors.toList());
    List<String> missingFiles =
        sidecar2Files.stream()
            .filter(Either::isLeft)
            .map(Either::getLeft)
            .collect(Collectors.toList());
    // TODO Add customized API logging facilities to return errors
    if (missingFiles.size() > 0) {
      missingFiles.forEach(System.err::println);
    }
    if (sidecar2Files.size() - missingFiles.size() == 0) {
      System.err.println("No files found to process.");
      return null;
    }
    List<Sidecar2> sidecars =
        sidecar2Files.stream()
            .filter(Either::isRight)
            .map(
                f -> {
                  try {
                    return Sidecar2Parser.parse(f.get().toFile(), _workingDir);
                  } catch (IOException ex) {
                    System.err.println("IO Error [" + f.get() + "]: " + ex.getMessage());
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    if (sidecars.size() == 0) {
      System.err.println("Could not parse any SC2 files.");
      return null;
    }
    System.out.println(
        "\t[" + sidecars.size() + "/" + sidecar2Files.size() + "] SC2 files parsable.");
    sidecars.forEach(sc -> {});

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

  private Either<String, Path> realizePath(String file) {
    Path rp = _workingDir.resolve(file);
    if (Files.isReadable(rp)) {
      return Either.right(rp);
    } else {
      return Either.left("Unable to open file: " + rp);
    }
  }

  private boolean checkSC2Validity(Sidecar2 sc) {
    System.out.println("\t-- Checking " + sc.self());
    System.out.println("\t\t -- Fully validated: " + sc.isValid());
    if (sc.isValid()) {
      return true;
    }
    System.out.println("\t\t -- All file entries valid: " + sc.areAllFilesValid());
    if (!sc.areAllFilesValid()) {
      System.out.println("\t\t -- Any file entries valid: " + sc.areAnyFilesValid());
      if (!sc.areAnyFilesValid()) {
        return false;
      }
    }
    System.out.println("\t\t -- All relations valid: " + sc.areAllRelationsValid());
    if (!sc.areAllRelationsValid()) {
      System.out.println("\t\t -- Any relations valid: " + sc.areAnyRelationsValid());
      if (!sc.areAnyRelationsValid()) {
        return false;
      }
    }
    return true;
  }
}

  /* Stashing this until later..
    private DataRegistry buildRegistry() {
      DataRegistry reg = new DataRegistry();
      for (Sc2FileReference f : _validFiles) {
        for (Sc2Sheet s : f.sheets()) {
          reg.add(f, s);
        }
      }
      return reg;
    }

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
