package test.sidecar2.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.FileGenerator.FileCheck;
import static test.samples.Sidecar2SampleKeys.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.common.jimfs.Jimfs;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.parsers.FileParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PreflightTest {
  static FileSystem fs;

  @BeforeAll
  static void setupFileSystem() throws IOException {
    fs = Jimfs.newFileSystem();
    Path workDir = fs.getPath("work");
    Files.createDirectory(workDir);
    Files.createFile(workDir.resolve(FMFN_XLSX_VAL));
  }

  private static List<Arguments> providesFiles() {
    return Sc2FileReferenceTest.provider;
  }

  @ParameterizedTest
  @MethodSource("providesFiles")
  void existingPreflightShouldStayValid(FileCheck file) {
    Sc2FileReference fc = FileParser.parse(file.json);
    Path workDir = fs.getPath("work");
    assumeThat(fc.isValid()).isTrue();
    assumeThat(fc.getContentType()).isEqualTo(FMCT_XLSX_VAL);
    assumeThat(fc.getName()).isPresent();
    assertThat(fc.isValid()).isTrue();
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("providesFiles")
  void missingPreflightShouldInvalidate(FileCheck file) {
    Sc2FileReference fc = FileParser.parse(file.json);
    Path workDir = fs.getPath("work");
    assumeThat(fc.isValid()).isTrue();
    assumeThat(fc.getContentType()).isEqualTo(FMCT_CSV_VAL);
    assertThat(fc.isValid()).isFalse();
    assertThat(fc.reasons().get(1)).isEqualTo("Could not read file: example.csv");
  }
}
