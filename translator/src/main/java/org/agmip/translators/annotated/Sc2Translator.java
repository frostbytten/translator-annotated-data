package org.agmip.translators.annotated;

import java.nio.file.Path;
import java.util.*;
import org.agmip.ace.AceDataset;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
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
