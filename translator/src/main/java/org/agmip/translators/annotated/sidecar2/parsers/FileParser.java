package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FM_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.S_FIELD;

import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.validators.Sc2FileReferenceValidator;

public class FileParser {
  public static Validation<Seq<String>, Sc2FileReference> parse(JsonNode fileJson, Path workDir) {
    JsonNode currentFM = fileJson.path(FM_FIELD);
    JsonNode sheetNode = fileJson.path(S_FIELD);
    String name = currentFM.path("file_name").asText(null);
    String url = currentFM.path("file_url").asText(null);
    String contentType = currentFM.path("content-type").asText(null);
    List<Validation<Seq<String>, Sc2Sheet>> sheets = SheetsParser.parse(sheetNode, contentType);
    return new Sc2FileReferenceValidator().validate(name, url, contentType, sheets, workDir);
  }
}
