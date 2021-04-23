package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation.Sc2RelationPart;

public class RelationParser {
  public static Sc2Relation parse(JsonNode json) {
    Sc2RelationPart from = _parse(json.path(RELF_FIELD));
    Sc2RelationPart to = _parse(json.path(RELT_FIELD));
    return new Sc2Relation(from, to);
  }

  public static Sc2RelationPart _parse(JsonNode json) {
    String file = json.path(REL_F_FIELD).asText();
    String sheet = json.path(REL_S_FIELD).asText();
    List<Integer> entries = new ArrayList<>();
    if (json.path(REL_K_FIELD).isArray()) {
      Iterator<JsonNode> keys = json.path(REL_K_FIELD).elements();
      while (keys.hasNext()) {
        JsonNode key = keys.next();
        entries.add(key.path(COL_FIELD).asInt(-1));
      }
    }
    return new Sc2RelationPart(file, sheet, entries.stream().mapToInt(i -> i).toArray());
  }
}
