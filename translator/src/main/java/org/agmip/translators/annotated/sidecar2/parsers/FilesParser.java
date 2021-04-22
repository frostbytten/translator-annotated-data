package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;

public class FilesParser {
  public static List<Sc2FileReference> parse(JsonNode json) {
    List<Sc2FileReference> entries = new ArrayList<>();
    Iterator<JsonNode> files = json.elements();
    while (files.hasNext()) {
      JsonNode currentFile = files.next().path("file");
      if (!currentFile.isMissingNode()) entries.add(FileParser.parse(currentFile));
    }
    return entries;
  }
}
