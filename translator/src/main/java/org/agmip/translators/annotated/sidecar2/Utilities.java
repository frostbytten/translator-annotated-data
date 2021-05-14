package org.agmip.translators.annotated.sidecar2;

import java.util.Comparator;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public enum Utilities {
  INSTANCE;
  private static final Comparator<Sc2Rule> colCompare =
      Comparator.comparing(Sc2Rule::getColumnIndex);
  public static final ObjectMapper mapper = new ObjectMapper();

  public static Validation<String, Integer> tryStringToInteger(String val, String context) {
    return tryStringToInteger(val, context, -1);
  }

  public static Validation<String, Integer> tryStringToInteger(
      String val, String context, int defaultVal) {
    if (Objects.isNull(val)) return Validation.valid(defaultVal);
    return Try.of(() -> Integer.valueOf(val))
        .toValidation(() -> context + " is not a valid number.");
  }

  public static Validation<String, String> checkForBlankString(String val, String context) {
    return val.isBlank()
        ? Validation.invalid(context + " is missing or blank.")
        : Validation.valid(val);
  }

  public static int getMaxSheetColumn(Sc2Sheet sheet) {
    int maxval = 0;
    Sc2Rule maxRule = sheet.rules().stream().max(colCompare).get();
    return maxRule.getColumnIndex();
  }
}
