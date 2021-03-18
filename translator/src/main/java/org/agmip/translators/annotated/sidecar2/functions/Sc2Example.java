package org.agmip.translators.annotated.sidecar2.functions;

import com.fasterxml.jackson.databind.JsonNode;

public class Sc2Example implements Sc2Function {
    public static final String EXAMPLE_FUN_ARG = "echo";
    private final String echo;
    private final boolean valid;

    public Sc2Example(JsonNode json) {
        this.echo = json.path(EXAMPLE_FUN_ARG).asText(null);
        valid = validate();
    }

    public String buildString() {
        return "I should repeat: " + echo;
    }

    public boolean isValid() {
        return valid;
    }

    private boolean validate() {
        return echo != null;
    }

}
