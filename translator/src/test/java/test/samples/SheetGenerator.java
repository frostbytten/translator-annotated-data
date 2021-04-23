package test.samples;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SheetGenerator {
  public static class SheetCheckFrame {
    public final String context;
    public final String name;
    public final Integer dataStartRow;
    public final Integer dataEndRow;
    public final boolean valid;

    public SheetCheckFrame(
        String name, Integer dataStartRow, Integer dataEndRow, String context, boolean valid) {
      this.context = context;
      this.name = name;
      this.dataStartRow = dataStartRow;
      this.dataEndRow = dataEndRow;
      this.valid = valid;
    }
  }

  public static class SheetCheck {
    public final JsonNode json;
    public final SheetCheckFrame checker;

    public SheetCheck(JsonNode json, SheetCheckFrame checker) {
      this.json = json;
      this.checker = checker;
    }

    public String toString() {
      return this.json + " - " + this.checker.valid + " [" + this.checker.context + "]";
    }
  }

  public static class SheetCheckBuilder {
    private String context;
    private String name;
    private Integer dataStartRow;
    private Integer dataEndRow;
    private final boolean valid;
    private final ObjectNode json;

    public SheetCheckBuilder(boolean valid) {
      this.valid = valid;
      this.json = mapper.createObjectNode();
    }

    public SheetCheckBuilder withContext(String context) {
      this.context = context;
      return this;
    }

    public SheetCheckBuilder withName(String name) {
      this.name = name;
      json.put(SN_FIELD, name);
      return this;
    }

    public SheetCheckBuilder withDataStartRow(int dsr) {
      this.dataStartRow = dsr;
      json.put(SDSR_FIELD, dsr);
      return this;
    }

    public SheetCheckBuilder withDataEndRow(int der) {
      this.dataEndRow = der;
      json.put(SDER_FIELD, der);
      return this;
    }

    public SheetCheck build() {
      return new SheetCheck(
          json, new SheetCheckFrame(name, dataStartRow, dataEndRow, context, valid));
    }
  }
}
