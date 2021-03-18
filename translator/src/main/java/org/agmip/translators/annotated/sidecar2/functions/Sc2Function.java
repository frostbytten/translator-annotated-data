package org.agmip.translators.annotated.sidecar2.functions;

import com.fasterxml.jackson.databind.JsonNode;

public interface Sc2Function {
    public String buildString();
    public boolean isValid();
}
