package test.sidecar2.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.FileGenerator.FileCheck;
import static test.samples.FileGenerator.FileCheckBuilder;
import static test.samples.Sidecar2SampleKeys.*;

import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.parsers.FileParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Sc2FileReferenceTest {
  public static FileCheck csvFull =
      new FileCheckBuilder(true).withContentType(FMCT_CSV_VAL).withName(FMFN_CSV_VAL).build();

  public static FileCheck xlsxFull =
      new FileCheckBuilder(true)
          .withContentType(FMCT_XLSX_VAL)
          .withName(FMFN_XLSX_VAL)
          .withUrl(FMFU_XLSX_VAL)
          .build();

  public static FileCheck xlsxFileOnly =
      new FileCheckBuilder(true).withContentType(FMCT_XLSX_VAL).withName(FMFN_XLSX_VAL).build();

  public static FileCheck xlsxUrlOnly =
      new FileCheckBuilder(true).withContentType(FMCT_XLSX_VAL).withUrl(FMFU_XLSX_VAL).build();

  public static FileCheck fileEmpty = new FileCheckBuilder(false).build();

  public static FileCheck fileInvalidCT =
      new FileCheckBuilder(false).withContentType(FMCT_UNS_VAL).withName(FMFN_UNS_VAL).build();

  public static FileCheck fileInvalidFileAndUrl =
      new FileCheckBuilder(false).withContentType(FMCT_XLSX_VAL).build();

  public static List<Arguments> provider =
      List.of(
          Arguments.of(csvFull),
          Arguments.of(xlsxFull),
          Arguments.of(xlsxFileOnly),
          Arguments.of(xlsxUrlOnly),
          Arguments.of(fileEmpty),
          Arguments.of(fileInvalidCT),
          Arguments.of(fileInvalidFileAndUrl));

  private static List<Arguments> providesFiles() {
    return provider;
  }

  private static Path workDir = Path.of("tmp", "sc2frt");

  @ParameterizedTest
  @MethodSource("providesFiles")
  void shouldValidateFileMetadata(FileCheck file) throws JsonProcessingException {
    Validation<Seq<String>, Sc2FileReference> fev = FileParser.parse(file.json, workDir);
    assertThat(fev.isValid()).isEqualTo(file.checker.valid);
  }

  @ParameterizedTest
  @MethodSource("providesFiles")
  void shouldBeAbleToExtractFileNames(FileCheck file) throws JsonProcessingException {
    Validation<Seq<String>, Sc2FileReference> fev = FileParser.parse(file.json, workDir);
    assumeThat(fev.isValid()).isTrue();
    Sc2FileReference fe = fev.get();
    assumeThat(fe.location().getScheme()).isEqualTo("file");
    assertThat(Path.of(fe.location()).getFileName().toString()).isEqualTo(file.checker.name);
    assertThat(fe.getContentType()).isEqualTo(file.checker.contentType);
  }

  @ParameterizedTest
  @MethodSource("providesFiles")
  void canExtractUrls(FileCheck file) throws JsonProcessingException {
    Validation<Seq<String>, Sc2FileReference> fev = FileParser.parse(file.json, workDir);
    assumeThat(fev.isValid()).isTrue();
    Sc2FileReference fe = fev.get();
    assumeThat(file.checker.url).isNotNull();
    assertThat(fe.location().toString()).isEqualTo(file.checker.url);
    assertThat(fe.getContentType()).isEqualTo(file.checker.contentType);
  }
}
