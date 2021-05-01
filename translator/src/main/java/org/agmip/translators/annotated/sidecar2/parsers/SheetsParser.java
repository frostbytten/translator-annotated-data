package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class SheetsParser {
  public static List<Validation<Seq<String>, Sc2Sheet>> parse(JsonNode json, String context) {
    return List.ofAll(json::elements)
        .filter(Predicate.not(JsonNode::isMissingNode))
        .map(node -> SheetParser.parse(node, context));
  }
}
