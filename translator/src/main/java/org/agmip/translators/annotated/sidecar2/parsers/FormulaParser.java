package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Formula;

public class FormulaParser {
  public static Sc2Formula parse(JsonNode json) {
    String fun = json.path("function").asText();
    JsonNode args = json.path("args");
    return new Sc2Formula(fun, args);
  }
}
