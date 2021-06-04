package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;
import org.agmip.translators.annotated.sidecar2.components.Sc2RelationKey;
import org.agmip.translators.annotated.sidecar2.validators.Sc2RelationKeyValidator;

public class RelationParser {
  public static Validation<Seq<String>, Sc2Relation> parse(JsonNode json) {
    Validation<Seq<String>, Sc2RelationKey> from = _parse(json.path(RELF_FIELD));
    Validation<Seq<String>, Sc2RelationKey> to = _parse(json.path(RELT_FIELD));
    Seq<String> errors =
        List.ofAll(from.isInvalid() ? from.getError() : List.empty())
            .appendAll(to.isInvalid() ? from.getError() : List.empty());
    if (errors.size() > 0) {
      return Validation.invalid(errors);
    } else {
      return Validation.valid(new Sc2Relation(from.get(), to.get()));
    }
  }

  public static Validation<Seq<String>, Sc2RelationKey> _parse(JsonNode json) {
    String file = json.path(REL_F_FIELD).asText();
    String sheet = json.path(REL_S_FIELD).asText();
    String table = json.path(TABLE_FIELD).asText(null);
    return new Sc2RelationKeyValidator().validate(file, sheet, table, json.path(REL_K_FIELD));
  }
}
