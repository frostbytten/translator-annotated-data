package org.agmip.translators.annotated.sidecar2.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelationsParser {
    public static List<Sc2Relation> parse(JsonNode relationsNode) {
        List<Sc2Relation> entries = new ArrayList<>();
        Iterator<JsonNode> relations = relationsNode.elements();
        while(relations.hasNext()) {
            JsonNode relation = relations.next();
            if (! relation.isMissingNode()) {
                entries.add(RelationParser.parse(relation));
            }
        }
        return entries;
    }
}
