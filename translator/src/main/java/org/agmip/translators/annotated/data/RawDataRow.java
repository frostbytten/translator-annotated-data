package org.agmip.translators.annotated.data;

import java.util.Arrays;
import java.util.List;

public class RawDataRow {
  private List<String> _values;

  public RawDataRow(String[] values) {
    _values = Arrays.asList(values);
  }

  public String getValue(int column) {
    return _values.get(column);
  }
}
