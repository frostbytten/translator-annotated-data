package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class SheetsParser {
  public static List<Sc2Sheet> parse(JsonNode json, String context) {
    List<Sc2Sheet> entries = new ArrayList<>();
    Iterator<JsonNode> sheets = json.elements();
    while (sheets.hasNext()) {
      JsonNode currentSheet = sheets.next();
      if (!currentSheet.isMissingNode()) entries.add(SheetParser.parse(currentSheet, context));
    }
    return entries;
  }
}
