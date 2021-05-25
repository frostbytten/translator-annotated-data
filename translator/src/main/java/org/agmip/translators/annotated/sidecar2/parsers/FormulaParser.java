package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;
import org.agmip.translators.annotated.sidecar2.validators.Sc2FormulaValidator;

public class FormulaParser {
  public static Validation<String, ? extends Sc2Function> parse(JsonNode json) {
    String fun = json.path("function").asText();
    JsonNode args = json.path("args");
    return new Sc2FormulaValidator().validateFormula(fun, args);
  }
}
