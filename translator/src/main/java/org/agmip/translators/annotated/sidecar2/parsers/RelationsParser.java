package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

public class RelationsParser {
  public static List<Validation<Seq<String>, Sc2Relation>> parse(JsonNode relationsNode) {
    return List.ofAll(relationsNode::elements)
        .filter(Predicate.not(JsonNode::isMissingNode))
        .map(RelationParser::parse);
  }
}
