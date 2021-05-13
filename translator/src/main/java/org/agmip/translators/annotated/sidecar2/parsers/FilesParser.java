package org.agmip.translators.annotated.sidecar2.parsers;

import java.nio.file.Path;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;

public class FilesParser {
  public static List<Validation<Seq<String>, Sc2FileReference>> parse(JsonNode json, Path workDir) {
    return List.ofAll(json::elements)
        .filter(Predicate.not(JsonNode::isMissingNode))
        .map(node -> FileParser.parse(node.path("file"), workDir));
  }
}
