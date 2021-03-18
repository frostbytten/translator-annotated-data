package test.samples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FORMA_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FORMF_FIELD;

public class FormulaGenerator {
    public static class FormulaCheck {
        public final JsonNode json;
        public final FormulaCheckFrame checker;

        public FormulaCheck(JsonNode json, FormulaCheckFrame checker) {
            this.json = json;
            this.checker = checker;
        }

        public String toString() {
            return json.toString() + " - " + checker.valid;
        }
    }

    public static class FormulaCheckFrame {
        public final String function;
        public final JsonNode args;
        public final boolean valid;

        public FormulaCheckFrame(String fun, JsonNode args, boolean valid) {
            this.function = fun;
            this.args = args;
            this.valid = valid;
        }
    }

    public static class FormulaCheckBuilder {
        private String function;
        private JsonNode args;
        private final boolean valid;
        private final ObjectNode json;

        public FormulaCheckBuilder(boolean valid) {
            this.valid = valid;
            this.json = mapper.createObjectNode();
        }

        public FormulaCheckBuilder withFunction(String fun) {
            this.function = fun;
            json.put(FORMF_FIELD, fun);
            return this;
        }

        public FormulaCheckBuilder withFunctionArgs(JsonNode node) {
            this.args = node;
            json.set(FORMA_FIELD, node);
            return this;
        }

        public FormulaCheck build() {
            return new FormulaCheck(json, new FormulaCheckFrame(function, args, valid));
        }
    }
}
