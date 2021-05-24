package org.agmip.translators.annotated.data;

import java.util.ArrayList;
import java.util.List;

public class DataRange {
  private final int _start;
  private final int _end;
  private boolean valid;
  private List<String> reasons = new ArrayList<>();

  public DataRange(Integer start, Integer end) {
    _start = ((start == null) ? 1 : start);
    _end = ((end == null) ? -1 : end);
    valid = validate();
  }

  public int start() {
    return _start;
  }

  public int end() {
    return _end;
  }

  public boolean isValid() {
    return valid;
  }

  private boolean validate() {
    boolean _valid = true;
    if (_start < 1) {
      reasons.add("[DataRange] data_start_row (value: " + _start + ") cannot be less than 1.");
      _valid = false;
    }
    if (_end != -1 && _start >= _end) {
      reasons.add(
          "[DataRange] data_end_row (value: "
              + _end
              + ") cannot be less than or equal to data_start_row (value: "
              + _start
              + ").");
      _valid = false;
    }
    return _valid;
  }

  public List<String> reasons() {
    return reasons;
  }

  @Override
  public String toString() {
    return "[" + _start + "," + ((_end == -1) ? "unbounded)" : (_end + "]"));
  }
}
