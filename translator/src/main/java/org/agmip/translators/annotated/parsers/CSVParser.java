package org.agmip.translators.annotated.parsers;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.agmip.translators.annotated.data.RawDataRow;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.support.FileUtilities;

public class CSVParser {
  public static List<RawDataRow> parse(Sc2FileReference file, Path workingDir) {
    List<RawDataRow> rows = new ArrayList<>();
    if (file.isFileValid()) {
      Path csvPath;
      if ((csvPath = FileUtilities.resolveSc2Path(file, workingDir)) != null) {
        try (Reader bfReader = Files.newBufferedReader(csvPath);
            CSVReader csvReader = new CSVReader(bfReader)) {
          String[] line;
          while ((line = csvReader.readNext()) != null) rows.add(new RawDataRow(line));
        } catch (IOException | CsvValidationException ex) {
          file.invalidate("[CSVParser] Error reading file: " + csvPath + "; " + ex);
        }
        return rows;
      } else {
        file.invalidate("[CSVParser] Unable to open file: " + file.tryName("<missing name>"));
        return rows;
      }
    }
    return rows;
  }
}
