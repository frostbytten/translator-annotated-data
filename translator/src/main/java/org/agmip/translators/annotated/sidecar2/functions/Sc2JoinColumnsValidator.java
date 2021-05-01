package org.agmip.translators.annotated.sidecar2.functions;

import static org.agmip.translators.annotated.sidecar2.functions.Sc2Functions.FUN_JC;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Validation;

public class Sc2JoinColumnsValidator {
  public static final String JC_VVK = "virtual_val_keys";
  public static final String JC_VD = "virtual_divider";

  public Validation<String, Sc2JoinColumns> validate(JsonNode args) {
    String joiner = args.path(JC_VD).asText(" ");
    Validation<String, List<Integer>> vc = validateKeys(args);
    return validateKeys(args).isValid()
        ? Validation.valid(new Sc2JoinColumns(vc.get(), joiner))
        : Validation.invalid(vc.getError());
  }

  private Validation<String, List<Integer>> validateKeys(JsonNode args) {
    if (args.path(JC_VVK).isArray()) {
      List<Integer> vc = new ArrayList<>();
      for (JsonNode val : args.path(JC_VVK)) {
        vc.add(val.asInt());
      }
      return vc.size() > 0
          ? Validation.valid(List.copyOf(vc))
          : Validation.invalid(error(JC_VVK, "has no keys specified."));
    } else {
      return Validation.invalid(error(JC_VVK, " is not an array."));
    }
  }

  private String error(String arg, String err, boolean custom) {
    return custom ? err : "Function " + FUN_JC + ": argument \"" + arg + "\" " + err;
  }

  private String error(String arg, String err) {
    return error(arg, err, false);
  }
}
