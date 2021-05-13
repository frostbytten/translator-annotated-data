package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

public class Sidecar2Parser {
  public static Sidecar2 parse(String json, Path workDir)
      throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse("INTERNAL JSON STRING", root, workDir);
  }

  public static Sidecar2 parse(JsonNode json, Path workDir)
      throws IOException, JsonProcessingException {
    return _parse("INTERNAL JSON NODE", json, workDir);
  }

  public static Sidecar2 parse(File json, Path workDir)
      throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse(json.getAbsolutePath(), root, workDir);
  }

  public static Sidecar2 parse(Reader json, Path workDir)
      throws IOException, JsonProcessingException {
    JsonNode root = mapper.readTree(json);
    return _parse("INTERNAL READER", root, workDir);
  }

  private static Sidecar2 _parse(String self, JsonNode root, Path workDir) {
    List<Validation<Seq<String>, Sc2FileReference>> _files =
        FilesParser.parse(root.path(ATM_FIELD).path(FILES_FIELD), workDir);
    java.util.List<Sc2Relation> _relations =
        RelationsParser.parse(root.path(ATM_FIELD).path(REL_FIELD));
    return new Sidecar2(self, _files.asJava(), _relations);
  }
}
