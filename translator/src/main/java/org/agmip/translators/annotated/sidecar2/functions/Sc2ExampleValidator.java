package org.agmip.translators.annotated.sidecar2.functions;

import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.FUN_EXAMPLE;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Validation;

public class Sc2ExampleValidator {
  public static final String EXAMPLE_FUN_ARG = "echo";

  public Validation<String, Sc2Example> validate(JsonNode args) {
    String echo = args.path(EXAMPLE_FUN_ARG).asText(null);
    return echo == null
        ? Validation.invalid(
            "Function " + FUN_EXAMPLE + ": missing argument \"" + EXAMPLE_FUN_ARG + "\"")
        : Validation.valid(new Sc2Example(echo));
  }
}
