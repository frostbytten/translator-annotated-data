package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FM_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.S_FIELD;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class FileParser {
  public static Sc2FileReference parse(JsonNode fileJson) {
    JsonNode currentFM = fileJson.path(FM_FIELD);
    JsonNode sheetNode = fileJson.path(S_FIELD);
    String name = currentFM.path("file_name").asText(null);
    String url = currentFM.path("file_url").asText(null);
    String contentType = currentFM.path("content-type").asText(null);
    List<Sc2Sheet> sheets = SheetsParser.parse(sheetNode, contentType);
    return new Sc2FileReference(name, url, contentType, sheets);
  }
}
