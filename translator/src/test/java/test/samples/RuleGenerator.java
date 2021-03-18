package test.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.agmip.translators.annotated.sidecar2.components.Sc2Rule.RuleType;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMC_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMCI_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMI_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMU_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMV_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SMF_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

public class RuleGenerator {
    public static class RuleCheckFrame {
        public final String icasa;
        public final String unit;
        public final Integer columnIndex;
        public final Integer category;
        public final String value;
        public final String format;
        public final RuleType ruleType;
        public final boolean valid;

        public RuleCheckFrame(String icasa, String unit, Integer columnIndex, Integer category, String value, String format, RuleType ruleType, boolean valid) {
            this.icasa = icasa;
            this.unit = unit;
            this.columnIndex = columnIndex;
            this.category = category;
            this.value = value;
            this.format = format;
            this.ruleType = ruleType;
            this.valid = valid;
        }
    }

    public static class RuleCheck {
        public final JsonNode json;
        public final RuleCheckFrame checker;

        public RuleCheck(JsonNode json, RuleCheckFrame checker) {
            this.json = json;
            this.checker = checker;
        }

        public String toString() {
            return this.json + " - " + this.checker.valid;
        }
    }

    public static class RuleCheckBuilder {
        private String icasa;
        private String unit;
        private Integer columnIndex;
        private Integer category;
        private String value;
        private String format;
        private RuleType ruleType;
        private final boolean valid;
        private final ObjectNode json;

        public RuleCheckBuilder(boolean valid) {
            this.valid = valid;
            this.json = mapper.createObjectNode();
            this.ruleType = RuleType.INVALID_RULE;
        }

        public RuleCheckBuilder withIcasa(String icasa) {
            json.put(SMI_FIELD, icasa);
            this.icasa = icasa;
            return this;
        }

        public RuleCheckBuilder withUnit(String unit) {
            json.put(SMU_FIELD, unit);
            this.unit = unit;
            return this;
        }

        public RuleCheckBuilder withColumnIndex(int ci) {
            json.put(SMCI_FIELD, ci);
            this.columnIndex = ci;
            return this;
        }

        public RuleCheckBuilder withCategory(int cat) {
            json.put(SMC_FIELD, cat);
            this.category = cat;
            return this;
        }

        public RuleCheckBuilder withValue(String value) {
            json.put(SMV_FIELD, value);
            this.value = value;
            return this;
        }

        public RuleCheckBuilder withFormat(String format) {
            json.put(SMF_FIELD, format);
            this.format = format;
            return this;
        }

        public RuleCheck build() {
            if ((columnIndex == null && value != null)) {
                this.ruleType = RuleType.VALUE_RULE;
            } else {
                this.ruleType = RuleType.EXTRACTION_RULE;
            }
            return new RuleCheck(json, new RuleCheckFrame(icasa, unit, columnIndex, category, value, format, ruleType, valid));
        }
    }
}
