package org.agmip.translators.annotated.sidecar2.validators;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.COL_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.REL_F_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.checkForBlankString;

import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2RelationKey;

public class Sc2RelationKeyValidator {
  public Validation<Seq<String>, Sc2RelationKey> validate(
      String file, String sheet, JsonNode keys) {
    Validation<String, List<Integer>> cleanKeys = validateKeys(keys);
    return Validation.combine(
            checkForBlankString(file, REL_F_FIELD),
            // checkForBlankString(sheet, REL_S_FIELD),
            Validation.valid(
                sheet), // We cannot really test the validity of sheets until after the true
            // filetype has been determined.
            validateUniqueKeys(cleanKeys))
        .ap(Sc2RelationKey::new);
  }

  private Validation<String, List<Integer>> validateKeys(JsonNode keys) {
    if (keys.isArray()) {
      try {
        List<Integer> cleanKeys =
            List.ofAll(keys).map(key -> Integer.valueOf(key.path(COL_FIELD).asText()));
        if (cleanKeys.size() < 1) {
          return Validation.invalid("keys need to be specified for a relation.");
        }
        if (!cleanKeys.forAll(i -> i >= 0)) {
          return Validation.invalid("keys must be greater than 0 for a relation.");
        }
        return Validation.valid(cleanKeys);
      } catch (NumberFormatException ex) {
        return Validation.invalid("Invalid column specified in keys for relation.");
      }
    } else {
      return Validation.invalid("keys are not an array for relation.");
    }
  }

  private Validation<String, List<Integer>> validateUniqueKeys(
      Validation<String, List<Integer>> keys) {
    if (keys.isInvalid()) return keys;
    return keys.get().forAll(new HashSet<>()::add)
        ? keys
        : Validation.invalid("Duplicate keys not allowed in a relation.");
  }
}
