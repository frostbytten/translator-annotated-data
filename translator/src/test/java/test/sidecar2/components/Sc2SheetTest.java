package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_CSV;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_XLSX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.SheetGenerator.SheetCheck;
import static test.samples.SheetGenerator.SheetCheckBuilder;
import static test.samples.Sidecar2SampleKeys.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.parsers.SheetParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Sc2SheetTest {
  private static final SheetCheck sheetMinimal =
      new SheetCheckBuilder(true).withContext(MIME_CSV).build();

  private static final SheetCheck sheetComplete =
      new SheetCheckBuilder(true)
          .withContext(MIME_XLSX)
          .withName(SSN_VAL)
          .withDataStartRow(SDSR_VAL)
          .withDataEndRow(SDER_VAL)
          .build();

  private static final SheetCheck sheetXlsxNoName =
      new SheetCheckBuilder(false).withContext(MIME_XLSX).build();

  private static final SheetCheck sheetDERLessThanDSR =
      new SheetCheckBuilder(false)
          .withContext(MIME_CSV)
          .withDataStartRow(SDSR_VAL)
          .withDataEndRow(SDER_INVAL)
          .build();

  private static final SheetCheck sheetDSRLessThanDefault =
      new SheetCheckBuilder(false).withContext(MIME_CSV).withDataStartRow(SD_R_INVAL).build();

  private static final SheetCheck sheetDERLessThanDefault =
      new SheetCheckBuilder(false).withContext(MIME_CSV).withDataEndRow(SD_R_INVAL).build();

  private static List<Arguments> provider =
      List.of(
          Arguments.of(sheetMinimal),
          Arguments.of(sheetComplete),
          Arguments.of(sheetXlsxNoName),
          Arguments.of(sheetDERLessThanDSR),
          Arguments.of(sheetDSRLessThanDefault),
          Arguments.of(sheetDERLessThanDefault));

  @ParameterizedTest
  @MethodSource("providesSheets")
  void shouldValidateSheets(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assertThat(scv.isValid()).isEqualTo(sheet.checker.valid);
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void xlsxShouldHaveName(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sheet.checker.context).isEqualTo(MIME_XLSX);
    assumeThat(scv.isValid()).isTrue();
    Sc2Sheet sc = scv.get();
    assertThat(sc.getName()).isNotEqualTo(Optional.empty());
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDerBeforeDsr(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(Objects.nonNull(sheet.checker.dataStartRow) && Objects.nonNull(sheet.checker.dataEndRow)).isTrue();
    int dsr = sheet.checker.dataStartRow;
    int der = sheet.checker.dataEndRow;
    assumeThat(dsr).isGreaterThan(0);
    assumeThat(der).isGreaterThan(0);
    assumeThat(dsr).isGreaterThan(der);
    assertThat(scv.isValid()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeValidIfDerAfterDsr(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(Objects.nonNull(sheet.checker.dataStartRow) && Objects.nonNull(sheet.checker.dataEndRow)).isTrue();
    int dsr = sheet.checker.dataStartRow;
    int der = sheet.checker.dataEndRow;
    assumeThat(dsr).isGreaterThan(0);
    assumeThat(der).isGreaterThan(0);
    assumeThat(dsr).isLessThan(der);
    assertThat(scv.isValid()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDsrLessThanDefault(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(Objects.nonNull(sheet.checker.dataStartRow) && Objects.nonNull(sheet.checker.dataEndRow)).isTrue();
    assumeThat(sheet.checker.dataStartRow).isLessThan(-1);
    assertThat(scv.isValid()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDerLessThanDefault(SheetCheck sheet) {
    Validation<Seq<String>, Sc2Sheet> scv = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(Objects.nonNull(sheet.checker.dataStartRow) && Objects.nonNull(sheet.checker.dataEndRow)).isTrue();
    assumeThat(sheet.checker.dataEndRow).isLessThan(-1);
    assertThat(scv.isValid()).isFalse();
  }

  private static List<Arguments> providesSheets() {
    return provider;
  }
}
