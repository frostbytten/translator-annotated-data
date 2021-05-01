package org.agmip.translators.annotated.sidecar2.validators;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMCI_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMC_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMV_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.tryStringToInteger;

import java.util.Objects;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule.RuleType;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;

public class Sc2RuleValidator {
  public Validation<Seq<String>, Sc2Rule> validate(
      String variable,
      String unit,
      String index,
      String category,
      String value,
      String format,
      Validation<String, ? extends Sc2Function> function) {
    Validation<String, Integer> indexColumn = validateInt(index, SMCI_FIELD);
    RuleType ruleType = determineRuleType(indexColumn, value, function);
    return (ruleType == RuleType.INVALID_RULE)
        ? Validation.invalid(List.of("Could not determine the translation rule."))
        : Validation.combine(
                validateICASA(variable),
                Validation.valid(unit),
                validateIndexWithRule(indexColumn, ruleType),
                validateInt(category, SMC_FIELD),
                validateValue(value, ruleType),
                Validation.valid(format),
                function)
            .ap(Sc2Rule::new);
  }

  private RuleType determineRuleType(
      Validation<String, Integer> indexVal,
      String value,
      Validation<String, ? extends Sc2Function> fun) {
    if (indexVal.isInvalid()) return RuleType.INVALID_RULE;
    int index = indexVal.get();
    if (index == -1) {
      if (value != null) {
        return RuleType.VALUE_RULE;
      } else if (fun.isValid() && fun.get() != null) {
        return RuleType.FUNCTION_RULE;
      } else {
        return RuleType.INVALID_RULE;
      }
    } else {
      if (fun != null) {
        return RuleType.FILL_WITH_FORMULA_RULE;
      } else {
        return RuleType.EXTRACTION_RULE;
      }
    }
  }

  private Validation<String, String> validateICASA(String variable) {
    return (Objects.isNull(variable))
        ? Validation.invalid("icasa variable not specified.")
        : Validation.valid(variable);
  }

  private Validation<String, Integer> validateInt(String index, String context) {
    if (Objects.isNull(index)) return Validation.valid(-1);
    Validation<String, Integer> converted = tryStringToInteger(index, context);
    if (converted.isInvalid()) return converted;
    if (converted.get() < -1) {
      return Validation.invalid(context + "(" + index + ") is not a valid column index.");
    }
    return converted;
  }

  private Validation<String, Integer> validateIndexWithRule(
      Validation<String, Integer> indexVal, RuleType ruleType) {
    if (indexVal.isInvalid()) return indexVal;
    int index = indexVal.get();
    if (ruleType == RuleType.EXTRACTION_RULE && index < 0) {
      return Validation.invalid(SMCI_FIELD + " is not a valid column index.");
    }
    return indexVal;
  }

  private Validation<String, String> validateValue(String val, RuleType ruleType) {
    if (ruleType != RuleType.VALUE_RULE) return Validation.valid(val);
    if (Objects.isNull(val)) {
      return Validation.invalid(
          SMV_FIELD + " must be present if a " + SMCI_FIELD + " is not specified.");
    } else {
      return Validation.valid(val);
    }
  }
}
