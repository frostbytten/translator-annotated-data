package org.agmip.translators.annotated.sidecar2.components;

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

  public Sc2Rule(
      String icasa,
      String unit,
      Integer index,
      Integer category,
      String value,
      String format,
      Sc2Function function,
      RuleType ruleType) {
    this._icasa = icasa;
    this._unit = unit;
    this._index = index;
    this._category = category;
    this._format = format;
    this._value = value;
    this._function = function;
    this._ruleType = ruleType;
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
}
