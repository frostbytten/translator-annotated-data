package org.agmip.translators.annotated.sidecar2.parsers;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.convertStringToInteger;

import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.functions.Sc2Function;

public class RuleParser {
  public static Sc2Rule parse(JsonNode ruleNode) {
    String variable = ruleNode.path(SMI_FIELD).asText(null);
    String unit = ruleNode.path(SMU_FIELD).asText(null);
    Integer columnIndex = convertStringToInteger(ruleNode.path(SMCI_FIELD).asText(null));
    Integer category = convertStringToInteger(ruleNode.path(SMC_FIELD).asText(null));
    String value = ruleNode.path(SMV_FIELD).asText(null);
    String format = ruleNode.path(SMF_FIELD).asText(null);
    Validation<String, ? extends Sc2Function> formula = null;
    if (ruleNode.path(FORM_FIELD).isObject()) {
      formula = FormulaParser.parse(ruleNode.path(FORM_FIELD));
    }
    return new Sc2Rule(variable, unit, columnIndex, category, value, format, formula);
  }
}
