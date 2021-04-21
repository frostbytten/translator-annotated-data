package org.agmip.translators.annotated.sidecar2.components;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SUPPORTED_MIME;

public class Sc2File {
  private final String _name;
  private final String _url;
  private final String _contentType;
  private final List<Sc2Sheet> _sheets;
  private boolean _fileValid;
  private boolean valid;
  private List<String> _reasons;

  public Sc2File(String name, String url, String contentType, List<Sc2Sheet> sheets) {
    this._name = name;
    this._url = url;
    this._contentType = contentType;
    this._sheets = sheets;
    this._reasons = new ArrayList<>();
    this._fileValid = ((this._name != null) || (this._url != null)) && (this._contentType != null) &&
      Arrays.stream(SUPPORTED_MIME).anyMatch(this._contentType::equalsIgnoreCase);
    this.valid = _fileValid;
  }

  public Optional<String> getName() {
    return Optional.ofNullable(_name);
  }

  public Optional<String> getUrl() {
    return Optional.ofNullable(_url);
  }

  public String getContentType() {
    return _contentType;
  }

  public String tryName(String orElse) {
    return getName().orElseGet(() -> orElse);
  }

  public String tryUrl(String orElse) {
    return getUrl().orElseGet(() -> orElse);
  }

  public List<Sc2Sheet> sheets() {
    return _sheets.stream().filter(s -> s.isValid()).collect(Collectors.toList());
  }

  public boolean isValid() {
    return valid;
  }

  public boolean isFileValid() {
    return _fileValid;
  }

  public void invalidate(String reason) {
    _reasons.add(reason);
    valid = false;
  }

  public List<String> reasons() {
    return _reasons;
  }
}
