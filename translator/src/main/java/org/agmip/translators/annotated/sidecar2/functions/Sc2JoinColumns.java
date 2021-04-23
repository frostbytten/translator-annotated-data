package org.agmip.translators.annotated.sidecar2.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

public class Sc2JoinColumns implements Sc2Function {
  public static final String JC_VVK = "virtual_val_keys";
  public static final String JC_VD = "virtual_divider";
  private final int[] valueColumns;
  private final String joiner;
  private final boolean valid;

  public Sc2JoinColumns(JsonNode json) {
    if (json.path(JC_VVK).isArray()) {
      List<Integer> vc = new ArrayList<>();
      for (JsonNode val : json.path(JC_VVK)) {
        vc.add(val.asInt());
      }
      valueColumns = vc.stream().mapToInt(i -> i).toArray();
    } else {
      valueColumns = null;
    }
    joiner = json.path(JC_VD).asText(" ");
    valid = validate();
  }

  public String buildString() {
    return "Columns: "
        + Arrays.stream(valueColumns).mapToObj(String::valueOf).collect(Collectors.joining(","))
        + " joined by "
        + joiner;
  }

  public boolean isValid() {
    return valid;
  }

  public String getJoiner() {
    return joiner;
  }

  public int[] getColumns() {
    return valueColumns;
  }

  private boolean validate() {
    return (valueColumns != null && valueColumns.length > 0);
  }
}
