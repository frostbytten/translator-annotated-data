package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2File;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class Sidecar2Parser {
    public static Sidecar2 parse(String json) throws JsonProcessingException  {
        JsonNode root = mapper.readTree(json);
        return _parse(root);
    }

    public static Sidecar2 parse(JsonNode json) throws JsonProcessingException {
        return _parse(json);
    }

    public static Sidecar2 parse(File json) throws IOException, JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        return _parse(root);
    }

    public static Sidecar2 parse(Reader json) throws IOException, JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        return _parse(root);
    }

    private static Sidecar2 _parse(JsonNode root) {
        List<Sc2File> _files = FilesParser.parse(root.path(ATM_FIELD).path(FILES_FIELD));
        List<Sc2Relation> _relations = RelationsParser.parse(root.path(ATM_FIELD).path(REL_FIELD));
        return new Sidecar2(_files, _relations);
    }
}
