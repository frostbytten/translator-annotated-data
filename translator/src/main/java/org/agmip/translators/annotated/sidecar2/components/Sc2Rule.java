package org.agmip.translators.annotated.sidecar2.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;

public class Sc2Rule {
  public enum RuleType {
    EXTRACTION_RULE,
    VALUE_RULE,
    FORMULA_RULE,
    FUNCTION_RULE,
    FILL_WITH_FORMULA_RULE,
    INVALID_RULE
  }

  private final String _icasa;
  private final String _unit;
  private final int _index;
  private final int _category;
  private final String _value;
  private final String _format;
  private final RuleType _ruleType;
  private final Sc2Function _function;
  private boolean valid;

  public Sc2Rule(
      String icasa,
      String unit,
      Integer index,
      Integer category,
      String value,
      String format,
      Sc2Function function) {
    this._icasa = icasa;
    this._unit = unit;
    this._index = index == null ? -1 : index;
    this._category = category == null ? -1 : category;
    this._format = format;
    this._value = value;
    this._function = function;
    if (this._index == -1) {
      if (this._value != null) {
        this._ruleType = RuleType.VALUE_RULE;
      } else if (this._function != null) {
        this._ruleType = RuleType.FORMULA_RULE;
      } else {
        this._ruleType = RuleType.EXTRACTION_RULE;
      }
    } else {
      if (this._function != null) {
        this._ruleType = RuleType.FILL_WITH_FORMULA_RULE;
      } else {
        this._ruleType = RuleType.EXTRACTION_RULE;
      }
    }
    this.valid = validate();
  }

  public String getVariable() {
    return _icasa;
  }

  public Optional<String> getUnit() {
    return Optional.ofNullable(_unit);
  }

  public int getColumnIndex() {
    return _index;
  }

  public int getCategory() {
    return _category;
  }

  public Optional<String> getValue() {
    return Optional.ofNullable(_value);
  }

  public RuleType getRuleType() {
    return _ruleType;
  }

  public Optional<String> getFormat() {
    return Optional.ofNullable(_format);
  }

  public Optional<Sc2Function> getFormula() {
    return Optional.ofNullable(_function);
  }

  public boolean isValid() {
    return valid;
  }

  public void invalidate() {
    valid = false;
  }

  private boolean validate() {
    List<String> reasons = new ArrayList<>();
    boolean validated = _icasa != null;
    if ((_ruleType == RuleType.EXTRACTION_RULE) && (getColumnIndex() < 0)) {

      validated = false;
    }
    if (_category < -1) {
      validated = false;
    }
    if ((_ruleType == RuleType.VALUE_RULE) && (getValue().isEmpty())) {
      validated = false;
    }
    return validated;
  }
}
