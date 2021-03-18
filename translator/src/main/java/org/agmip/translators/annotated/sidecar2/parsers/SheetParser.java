package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

import java.util.List;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.convertStringToInteger;

public class SheetParser {
    public static Sc2Sheet parse(JsonNode sheetJson, String context) {
       String name = sheetJson.path(SN_FIELD).asText(null);
       Integer dataStartRow = convertStringToInteger(sheetJson.path(SDSR_FIELD).asText(null));
       Integer dataEndRow = convertStringToInteger(sheetJson.path(SDER_FIELD).asText(null));
       List<Sc2Rule> rules = RulesParser.parse(sheetJson.path(SM_FIELD));
       return new Sc2Sheet(name, dataStartRow, dataEndRow, rules, context);
    }
}
