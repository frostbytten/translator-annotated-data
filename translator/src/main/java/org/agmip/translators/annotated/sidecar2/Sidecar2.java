package org.agmip.translators.annotated.sidecar2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

public class Sidecar2 {
  private final String _self;
  private final List<Sc2FileReference> _validFiles;
  private final List<Sc2FileReference> _invalidFiles;
  private final List<Sc2Relation> _validRelations;
  private final List<Sc2Relation> _invalidRelations;
  private final boolean _valid;
  private final boolean _allFilesValid;
  private final boolean _anyFilesValid;
  private final boolean _allRelationsValid;
  private final boolean _anyRelationsValid;

  public Sidecar2(String self, List<Sc2FileReference> files, List<Sc2Relation> relations) {
    _self = self;
    _validFiles = files.stream().filter(Sc2FileReference::isValid).collect(Collectors.toList());
    _invalidFiles = files.stream().filter(f -> !f.isValid()).collect(Collectors.toList());
    _validRelations = relations.stream().filter(Sc2Relation::isValid).collect(Collectors.toList());
    _invalidRelations = relations.stream().filter(r -> !r.isValid()).collect(Collectors.toList());
    _allFilesValid = _invalidFiles.isEmpty();
    _anyFilesValid = (_validFiles.size() > 0);
    _allRelationsValid = _invalidRelations.isEmpty();
    _anyRelationsValid = (_validRelations.size() > 0);
    _valid = _allFilesValid && _allRelationsValid;
  }

  public String self() {
    return _self;
  }

  public List<Sc2FileReference> files() {
    return _validFiles;
  }

  public List<Sc2FileReference> allFiles() {
    return Stream.concat(_validFiles.stream(), _invalidFiles.stream()).collect(Collectors.toList());
  }

  public List<Sc2FileReference> invalidFiles() {
    return _invalidFiles;
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
}
