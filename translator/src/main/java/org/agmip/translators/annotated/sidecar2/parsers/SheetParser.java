package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.validators.Sc2SheetValidator;

public class SheetParser {
  public static Validation<Seq<String>, Sc2Sheet> parse(JsonNode sheetJson, String context) {
    String name = sheetJson.path(SN_FIELD).asText(null);
    String sheetIndex = sheetJson.path(SI_FIELD).asText(null);
    String dataStartRow = sheetJson.path(SDSR_FIELD).asText(null);
    String dataEndRow = sheetJson.path(SDER_FIELD).asText(null);
    List<Validation<Seq<String>, Sc2Rule>> rules = RulesParser.parse(sheetJson.path(SM_FIELD));
    return new Sc2SheetValidator()
        .validate(name, sheetIndex, dataStartRow, dataEndRow, rules, context);
  }
}
