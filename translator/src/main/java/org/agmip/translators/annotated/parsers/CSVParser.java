package org.agmip.translators.annotated.parsers;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.opencsv.CSVReader;
import io.vavr.collection.List;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.data.RawDataRow;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;

public class CSVParser {
  public static Validation<String, RawDataRow> parse(Sc2FileReference file) {
    if (file.location().getScheme().equals("file")) {
      final Path csvPath = Path.of(file.location());
      if (csvPath != null) {
        Try.of(
                () -> {
                  try (Reader bfReader = Files.newBufferedReader(csvPath);
                      CSVReader reader = new CSVReader(bfReader)) {
                    return List.ofAll(reader).map(RawDataRow::new);
                  }
                })
            .toValidation((ex) -> "Error parsing CSV file: " + csvPath + ex.getMessage());
      } else {
        return Validation.invalid("Unable to open file: " + csvPath);
      }
    }
    return Validation.invalid("Unsupported URI schema in CSV Parser: " + file.location());
  }
}
