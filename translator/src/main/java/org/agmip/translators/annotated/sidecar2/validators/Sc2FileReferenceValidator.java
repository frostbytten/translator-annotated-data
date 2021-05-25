package org.agmip.translators.annotated.sidecar2.validators;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FMC_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SUPPORTED_MIME;

import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class Sc2FileReferenceValidator {
  public Validation<Seq<String>, Sc2FileReference> validate(
      String name,
      String url,
      String contentType,
      List<Validation<Seq<String>, Sc2Sheet>> sheets,
      Path workDir) {
    return Validation.combine(
            validateLocation(name, url, workDir),
            validateContentType(contentType),
            Validation.valid(sheets))
        .ap(Sc2FileReference::new);
  }

  private Validation<String, URI> validateLocation(String name, String url, Path workDir) {
    if (Objects.isNull(name) && Objects.isNull(url)) {
      return Validation.invalid("A file reference must include a file or a URL.");
    } else {
      Validation<String, URI> cache = null;
      if (Objects.nonNull(name)) {
        cache = refUri(workDir.resolve(name));
      }
      if (Objects.nonNull(url)) {
        cache = refUri(url);
      }
      return cache;
    }
  }

  private Validation<String, String> validateContentType(String contentType) {
    if (Objects.isNull(contentType)) return Validation.invalid("Missing required " + FMC_FIELD);
    return Arrays.stream(SUPPORTED_MIME).anyMatch(contentType::equalsIgnoreCase)
        ? Validation.valid(contentType)
        : Validation.invalid(FMC_FIELD + " " + contentType + " is unsupported.");
  }

  private Validation<String, URI> refUri(Path ref) {
    return Try.of(ref::toUri).toValidation(() -> ref + " created an invalid URI.");
  }

  private Validation<String, URI> refUri(String ref) {
    return Try.of(() -> new URI(ref)).toValidation(() -> ref + " is not a valid URI.");
  }
}
