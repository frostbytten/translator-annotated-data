package org.agmip.translators.annotated.parsers;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.data.DataFileKey;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;

public class TikaParser {
  public static Validation<String, Map<String, Seq<Seq<String>>>> parse(
      DataFileKey fileKey, Set<String> sheets) {
    return Try.of(
            () -> {
              AutoDetectParser autoParser = new AutoDetectParser();
              Metadata md = new Metadata();
              TikaInputStream in = TikaInputStream.get(fileKey.getPath());
              RawDataHandler handler = new RawDataHandler(fileKey, sheets);
              autoParser.parse(in, handler, md);
              return handler.getResults();
            })
        .toValidation("something bad happened");
  }
}
