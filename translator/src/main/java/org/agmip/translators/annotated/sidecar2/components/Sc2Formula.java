package org.agmip.translators.annotated.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.*;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Example;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;
import org.agmip.translators.annotated.sidecar2.functions.Sc2JoinColumns;

public class Sc2Formula {
  private final String _functionName;
  private final Sc2Function _function;
  private boolean valid;

  public Sc2Formula(String fun, JsonNode args) {
    this._functionName = fun;
    this.valid = validate();
    if (this.valid) {
      switch (this._functionName) {
        case FUN_EXAMPLE:
          this._function = new Sc2Example(args);
          break;
        case FUN_JC:
          this._function = new Sc2JoinColumns(args);
          break;
        default:
          // This SHOULDN'T happen
          this._function = null;
          break;
      }
    } else {
      this._function = null;
    }
    this.valid = validate2();
  }

  private boolean validate() {
    return Arrays.stream(SUPPORTED_FUNCTIONS).anyMatch(this._functionName::equalsIgnoreCase);
  }

  private boolean validate2() {
    if (this._function == null) {
      return false;
    } else {
      return this._function.isValid();
    }
  }

  public Optional<Sc2Function> function() {
    return Optional.ofNullable(this._function);
  }

  public Optional<String> getFunctionName() {
    return Optional.ofNullable(this._functionName);
  }

  public boolean isValid() {
    return valid;
  }
}
