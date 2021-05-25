package test.samples;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileGenerator {
  public static class FileCheck {
    public final JsonNode json;
    public final FileCheckFrame checker;

    public FileCheck(JsonNode json, FileCheckFrame checker) {
      this.json = json;
      this.checker = checker;
    }

    public String toString() {
      return json.toString() + " - " + checker.valid;
    }
  }

  public static class FileCheckFrame {
    public final String name;
    public final String url;
    public final String contentType;
    public final boolean valid;

    public FileCheckFrame(String name, String url, String contentType, boolean valid) {
      this.name = name;
      this.url = url;
      this.contentType = contentType;
      this.valid = valid;
    }
  }

  public static class FileCheckBuilder {
    private String fileName;
    private String fileUrl;
    private String contentType;
    private final boolean valid;
    private final ObjectNode json;

    public FileCheckBuilder(boolean valid) {
      this.valid = valid;
      this.json = mapper.createObjectNode();
    }

    public FileCheckBuilder withName(String name) {
      this.fileName = name;
      json.put(FMN_FIELD, name);
      return this;
    }

    public FileCheckBuilder withUrl(String url) {
      this.fileUrl = url;
      json.put(FMU_FIELD, url);
      return this;
    }

    public FileCheckBuilder withContentType(String contentType) {
      this.contentType = contentType;
      json.put(FMC_FIELD, contentType);
      return this;
    }

    public FileCheck build() {
      return new FileCheck(
          mapper.createObjectNode().set(FM_FIELD, json),
          new FileCheckFrame(fileName, fileUrl, contentType, valid));
    }
  }
}
