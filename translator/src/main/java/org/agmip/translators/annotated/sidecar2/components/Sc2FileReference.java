package org.agmip.translators.annotated.sidecar2.components;

import java.net.URI;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class Sc2FileReference {
  private final URI _location;
  private final String _contentType;
  private final List<Validation<Seq<String>, Sc2Sheet>> _sheets;
  private final boolean _allSheetsValid;
  private final boolean _anySheetsValid;
  private ComponentState _state;

  public Sc2FileReference(
      URI location, String contentType, List<Validation<Seq<String>, Sc2Sheet>> sheets) {
    this._location = location;
    this._contentType = contentType;
    this._sheets = sheets;
    _allSheetsValid = sheets.forAll(Validation::isValid);
    _anySheetsValid = sheets.find(Validation::isValid).isDefined();
    _state = setSheetState();
  }

  public URI location() {
    return _location;
  }

  public String getContentType() {
    return _contentType;
  }

  public List<Sc2Sheet> sheets() {
    return _sheets.filter(Validation::isValid).map(Validation::get);
  }

  public List<Seq<String>> errors() {
    return _sheets.filter(Validation::isInvalid).map(Validation::getError);
  }

  public List<Validation<Seq<String>, Sc2Sheet>> rawSheets() {
    return _sheets;
  }

  public ComponentState setSheetState() {
    if (_sheets.forAll(Validation::isValid)
        && _sheets.forAll(s -> s.get().rulesState() == ComponentState.COMPLETE)) {
      return ComponentState.COMPLETE;
    } else if (_sheets.forAll(Validation::isInvalid)
        || _sheets
            .filter(Validation::isValid)
            .forAll(s -> s.get().rulesState() == ComponentState.INVALID)) {
      return ComponentState.INVALID;
    } else {
      return ComponentState.PARTIAL;
    }
  }

  public ComponentState sheetState() {
    return _state;
  }
}
