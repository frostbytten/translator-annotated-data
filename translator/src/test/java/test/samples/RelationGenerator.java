package test.samples;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RelationGenerator {
  public static class RelationCheck {
    public final JsonNode json;
    public final RelationCheckFrame checker;

    public RelationCheck(JsonNode json, RelationCheckFrame checker) {
      this.json = json;
      this.checker = checker;
    }

    public String toString() {
      return json.toString() + " - " + checker.valid;
    }
  }

  public static class RelationsCheck {
    public final JsonNode json;
    public final RelationsCheckFrame checker;

    public RelationsCheck(JsonNode json, RelationsCheckFrame checker) {
      this.json = json;
      this.checker = checker;
    }

    public String toString() {
      return json.toString() + " - " + checker.valid;
    }
  }

  public static class RelationCheckFrame {
    public final String file;
    public final String sheet;
    public final int[] keys;
    public final boolean valid;

    public RelationCheckFrame(String file, String sheet, int[] keys, boolean valid) {
      this.file = file;
      this.sheet = sheet;
      this.keys = keys;
      this.valid = valid;
    }
  }

  public static class RelationsCheckFrame {
    private final RelationCheckFrame to;
    private final RelationCheckFrame from;
    private final boolean valid;

    public RelationsCheckFrame(RelationCheckFrame to, RelationCheckFrame from, boolean valid) {
      this.to = to;
      this.from = from;
      this.valid = valid;
    }
  }

  public static class RelationCheckBuilder {
    private String file;
    private String sheet;
    private List<Integer> keys = new ArrayList<>();
    private ObjectNode json;
    private ArrayNode keyList;
    private final boolean valid;

    public RelationCheckBuilder(boolean valid) {
      this.valid = valid;
      this.json = mapper.createObjectNode();
      this.keyList = mapper.createArrayNode();
    }

    public RelationCheckBuilder withFile(String file) {
      json.put(REL_F_FIELD, file);
      this.file = file;
      return this;
    }

    public RelationCheckBuilder withSheet(String sheet) {
      json.put(REL_S_FIELD, sheet);
      this.sheet = sheet;
      return this;
    }

    public RelationCheckBuilder addKeyColumn(int col) {
      keyList.add(mapper.createObjectNode().put(COL_FIELD, col));
      keys.add(col);
      return this;
    }

    public RelationCheck build() {
      return new RelationCheck(buildJson(), buildCheckFrame());
    }

    public RelationCheckFrame buildCheckFrame() {
      int[] colKeys = keys.stream().mapToInt(i -> i).toArray();
      return new RelationCheckFrame(file, sheet, colKeys, valid);
    }

    public JsonNode buildJson() {
      if (keys.size() > 0) {
        json.set(REL_K_FIELD, keyList);
      }
      return json;
    }
  }
}
