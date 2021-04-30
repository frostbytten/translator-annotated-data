package org.agmip.translators.annotated.sidecar2.validators;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.functions.Sc2ExampleValidator;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;
import org.agmip.translators.annotated.sidecar2.functions.Sc2JoinColumnsValidator;

import java.util.Arrays;

import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.FUN_EXAMPLE;
import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.FUN_JC;
import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.SUPPORTED_FUNCTIONS;


public class Sc2FormulaValidator {
  public Validation<String, ? extends Sc2Function> validateFormula(String fun, JsonNode args) {
    if (Arrays.stream(SUPPORTED_FUNCTIONS).noneMatch(fun::equalsIgnoreCase)) {
      return Validation.invalid("Unsupported function: " + fun);
    }
    switch(fun.toLowerCase()) {
      case FUN_EXAMPLE:
        Sc2ExampleValidator example = new Sc2ExampleValidator();
        return example.validate(args);
      case FUN_JC:
        Sc2JoinColumnsValidator jc = new Sc2JoinColumnsValidator();
        return jc.validate(args);
      default:
        return Validation.invalid("Unmatched function: " + fun);
    }
  }
}
