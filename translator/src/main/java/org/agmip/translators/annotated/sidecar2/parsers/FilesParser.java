package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilesParser {
    public static List<Sc2File> parse(JsonNode json) {
        List<Sc2File> entries = new ArrayList<>();
        Iterator<JsonNode> files = json.elements();
        while(files.hasNext()) {
            JsonNode currentFile = files.next().path("file");
            if(! currentFile.isMissingNode()) entries.add(FileParser.parse(currentFile));
        }
        return entries;
    }
}
