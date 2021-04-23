package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_CSV;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_XLSX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.SheetGenerator.SheetCheck;
import static test.samples.SheetGenerator.SheetCheckBuilder;
import static test.samples.Sidecar2SampleKeys.*;

import java.util.List;
import java.util.Optional;

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
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assertThat(sc.isValid()).isEqualTo(sheet.checker.valid);
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void xlsxShouldHaveName(SheetCheck sheet) {
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sheet.checker.context).isEqualTo(MIME_XLSX);
    assertThat(sc.isValid()).isEqualTo(sheet.checker.valid);
    assumeThat(sc.isValid()).isTrue();
    assertThat(sc.getName()).isNotEqualTo(Optional.empty());
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDerBeforeDsr(SheetCheck sheet) {
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sc.getDataStartRow()).isGreaterThan(0);
    assumeThat(sc.getDataEndRow()).isGreaterThan(0);
    assumeThat(sc.getDataEndRow()).isLessThan(sc.getDataStartRow());
    assertThat(sc.isValid()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeValidIfDerAfterDsr(SheetCheck sheet) {
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sc.getDataStartRow()).isGreaterThan(0);
    assumeThat(sc.getDataEndRow()).isGreaterThan(0);
    assumeThat(sc.getDataStartRow()).isLessThan(sc.getDataEndRow());
    assertThat(sc.isValid()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDsrLessThanDefault(SheetCheck sheet) {
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sc.getDataStartRow()).isLessThan(-1);
    assertThat(sc.isValid()).isFalse();
  }

  @ParameterizedTest
  @MethodSource("providesSheets")
  void sheetShouldBeInvalidIfDerLessThanDefault(SheetCheck sheet) {
    Sc2Sheet sc = SheetParser.parse(sheet.json, sheet.checker.context);
    assumeThat(sc.getDataEndRow()).isLessThan(-1);
    assertThat(sc.isValid()).isFalse();
  }

  private static List<Arguments> providesSheets() {
    return provider;
  }
}
