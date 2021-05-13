package org.agmip.translators.annotated.sidecar2.components;

import java.net.URI;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class Sc2FileReference {
  private final URI _location;
  private final String _contentType;
  private final List<Validation<Seq<String>, Sc2Sheet>> _sheets;
  private final List<Sc2Sheet> _validSheets;
  private final List<Seq<String>> _errors;

  public Sc2FileReference(
      URI location, String contentType, List<Validation<Seq<String>, Sc2Sheet>> sheets) {
    this._location = location;
    this._contentType = contentType;
    this._sheets = sheets;
    this._validSheets = _sheets.filter(Validation::isValid).map(Validation::get);
    this._errors = _sheets.filter(Validation::isInvalid).map(Validation::getError);
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
}
