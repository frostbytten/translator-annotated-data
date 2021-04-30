package org.agmip.translators.annotated.sidecar2.functions;

import com.fasterxml.jackson.databind.JsonNode;

public class Sc2Example implements Sc2Function {
  private final String echo;

  public Sc2Example(String echo) {
    this.echo = echo;
  }

  public String buildString() {
    return "I should repeat: " + echo;
  }
}
