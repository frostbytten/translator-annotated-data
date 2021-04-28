package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

public class Sidecar2Parser {
  public static Sidecar2 parse(String json) throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse("INTERNAL JSON STRING", root);
  }

  public static Sidecar2 parse(JsonNode json) throws IOException, JsonProcessingException {
    return _parse("INTERNAL JSON NODE", json);
  }

  public static Sidecar2 parse(File json) throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse(json.getAbsolutePath(), root);
  }

  public static Sidecar2 parse(Reader json) throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse("INTERNAL READER", root);
  }

  private static Sidecar2 _parse(String self, JsonNode root) {
    List<Sc2FileReference> _files = FilesParser.parse(root.path(ATM_FIELD).path(FILES_FIELD));
    List<Sc2Relation> _relations = RelationsParser.parse(root.path(ATM_FIELD).path(REL_FIELD));
    return new Sidecar2(self, _files, _relations);
  }
}
