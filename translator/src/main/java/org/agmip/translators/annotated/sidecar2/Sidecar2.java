package org.agmip.translators.annotated.sidecar2;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.ComponentState;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

public class Sidecar2 {
  private final String _self;
  private final List<Validation<Seq<String>, Sc2FileReference>> _files;
  private final List<Validation<Seq<String>, Sc2Relation>> _relations;
  private final boolean _valid;
  private final boolean _allFilesValid;
  private final boolean _anyFilesValid;
  private final boolean _allRelationsValid;
  private final boolean _anyRelationsValid;
  private final ComponentState _fileState;
  private final ComponentState _relationState;

  public Sidecar2(
      String self,
      List<Validation<Seq<String>, Sc2FileReference>> files,
      List<Validation<Seq<String>, Sc2Relation>> relations) {
    _self = self;
    _files = files;
    _relations = relations;
    _fileState = setFilesState();
    _relationState = setRelationsState();
    _allFilesValid = files.forAll(Validation::isValid);
    _anyFilesValid = files.find(Validation::isValid).isDefined();
    _allRelationsValid = relations.forAll(Validation::isValid);
    _anyRelationsValid = relations.find(Validation::isValid).isDefined();
    _valid = _fileState == ComponentState.COMPLETE && _relationState == ComponentState.COMPLETE;
  }

  public String self() {
    return _self;
  }

  public List<Sc2FileReference> files() {
    return _files.filter(Validation::isValid).map(Validation::get);
  }

  public List<Sc2Relation> relations() {
    return _relations.filter(Validation::isValid).map(Validation::get);
  }

  public List<Validation<Seq<String>, Sc2FileReference>> rawFiles() {
    return _files;
  }

  public List<Validation<Seq<String>, Sc2Relation>> rawRelations() {
    return _relations;
  }

  public boolean isValid() {
    return _valid;
  }

  public boolean areAnyFilesValid() {
    return _anyFilesValid;
  }

  public boolean areAllFilesValid() {
    return _allFilesValid;
  }

  public boolean areAnyRelationsValid() {
    return _anyRelationsValid;
  }

  public boolean areAllRelationsValid() {
    return _allRelationsValid;
  }

  public ComponentState fileState() {
    return _fileState;
  }

  public ComponentState relationState() {
    return _relationState;
  }

  private ComponentState setFilesState() {
    if (_files.forAll(Validation::isValid)
        && _files.forAll(f -> f.get().sheetState() == ComponentState.COMPLETE)) {
      return ComponentState.COMPLETE;
    } else if (_files.forAll(Validation::isInvalid)
        || _files
            .filter(Validation::isValid)
            .forAll(f -> f.get().sheetState() == ComponentState.INVALID)) {
      return ComponentState.INVALID;
    } else {
      return ComponentState.PARTIAL;
    }
  }

  private ComponentState setRelationsState() {
    if (_relations.forAll((Validation::isValid))) {
      return ComponentState.COMPLETE;
    } else if (_relations.forAll(Validation::isInvalid)) {
      return ComponentState.INVALID;
    } else {
      return ComponentState.PARTIAL;
    }
  }
}
