package org.agmip.translators.annotated.sidecar2.functions;

import java.util.List;
import java.util.stream.Collectors;

public class Sc2JoinColumns implements Sc2Function {
  private final List<Integer> valueColumns;
  private final String joiner;

  public Sc2JoinColumns(List<Integer> keys, String joiner) {
    this.valueColumns = keys;
    this.joiner = joiner;
  }

  public String buildString() {
    return "Columns: "
        + valueColumns.stream().map(String::valueOf).collect(Collectors.joining(","))
        + " joined by "
        + joiner;
  }

  public String getJoiner() {
    return joiner;
  }

  public List<Integer> getColumns() {
    return valueColumns;
  }
}
