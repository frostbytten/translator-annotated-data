package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;

public class RulesParser {
  public static List<Validation<Seq<String>, Sc2Rule>> parse(JsonNode rulesNode) {
    return List.ofAll(rulesNode::elements)
        .filter(Predicate.not(JsonNode::isMissingNode))
        .map(RuleParser::parse);
  }
}
