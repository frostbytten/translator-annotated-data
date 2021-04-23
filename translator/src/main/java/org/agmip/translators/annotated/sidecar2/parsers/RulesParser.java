package org.agmip.translators.annotated.sidecar2.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;

public class RulesParser {
  public static List<Sc2Rule> parse(JsonNode rulesNode) {
    List<Sc2Rule> entries = new ArrayList<>();
    Iterator<JsonNode> rules = rulesNode.elements();
    while (rules.hasNext()) {
      JsonNode rule = rules.next();
      if (!rule.isMissingNode()) {
        entries.add(RuleParser.parse(rule));
      }
    }
    return entries;
  }
}
