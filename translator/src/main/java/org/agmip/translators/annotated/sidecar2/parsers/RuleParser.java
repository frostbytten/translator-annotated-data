package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;
import org.agmip.translators.annotated.sidecar2.validators.Sc2RuleValidator;

public class RuleParser {
  public static Validation<Seq<String>, Sc2Rule> parse(JsonNode ruleNode) {
    String variable = ruleNode.path(SMI_FIELD).asText(null);
    String unit = ruleNode.path(SMU_FIELD).asText(null);
    String columnIndex = ruleNode.path(SMCI_FIELD).asText(null);
    String category = ruleNode.path(SMC_FIELD).asText(null);
    String value = ruleNode.path(SMV_FIELD).asText(null);
    String format = ruleNode.path(SMF_FIELD).asText(null);
    Validation<String, ? extends Sc2Function> formula;
    if (ruleNode.path(FORM_FIELD).isObject()) {
      formula = FormulaParser.parse(ruleNode.path(FORM_FIELD));
    } else {
      formula = Validation.valid(null);
    }
    return new Sc2RuleValidator()
        .validate(variable, unit, columnIndex, category, value, format, formula);
  }
}
