package org.agmip.translators.annotated.sidecar2.components;

import java.util.Optional;

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
	private final Sc2Formula _formula;
	private boolean valid;

	public Sc2Rule(String icasa, String unit, Integer index, Integer category, String value, String format, Sc2Formula formula) {
		this._icasa = icasa;
		this._unit = unit;
		this._index = Optional.ofNullable(index).orElseGet(() -> -1);
		this._category = Optional.ofNullable(category).orElseGet(()->-1);
		this._format = format;
		this._value = value;
		this._formula = formula;
		if (this._index == -1) {
			if (this._value != null) {
				this._ruleType = RuleType.VALUE_RULE;
			} else if (this._formula != null) {
				this._ruleType = RuleType.FORMULA_RULE;
			} else {
				this._ruleType = RuleType.EXTRACTION_RULE;
			}
		} else {
			if (this._formula != null) {
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

	public Optional<String>getValue() {
		return Optional.ofNullable(_value);
	}

	public RuleType getRuleType() {
		return _ruleType;
	}

	public Optional<String> getFormat() {
		return Optional.ofNullable(_format);
	}

	public Optional<Sc2Formula> getFormula() {
		return Optional.ofNullable(_formula);
	}

	public boolean isValid() {
		return valid;
	}

	public void invalidate() {
		valid = false;
	}

	private boolean validate() {
		boolean validated = true;
		if (_icasa == null) {
			validated = false;
		}
		if ((_ruleType == RuleType.EXTRACTION_RULE) && (getColumnIndex() < 0)) {
			validated = false;
		}
		if (_category < -1) {
			validated = false;
		}
		if((_ruleType == RuleType.VALUE_RULE) && (getValue().isEmpty())) {
			validated = false;
		}
		return validated;
	}
}
