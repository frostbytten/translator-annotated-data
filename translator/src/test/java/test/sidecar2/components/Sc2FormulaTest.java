package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.Sidecar2SampleKeys.*;

import java.util.List;

import org.agmip.translators.annotated.sidecar2.components.Sc2Formula;
import org.agmip.translators.annotated.sidecar2.parsers.FormulaParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.samples.FormulaGenerator.FormulaCheck;
import test.samples.FormulaGenerator.FormulaCheckBuilder;

public class Sc2FormulaTest {
  public static FormulaCheck emptyFormula = new FormulaCheckBuilder(false).build();
  public static FormulaCheck formulaInvalidFunction =
      new FormulaCheckBuilder(false).withFunction(FORMF_INVAL).build();
  public static FormulaCheck formulaFunExample =
      new FormulaCheckBuilder(true).withFunction(FORMF_VAL).withFunctionArgs(FORMA_VAL).build();
  public static FormulaCheck formulaFunExampleFail =
      new FormulaCheckBuilder(false)
          .withFunction(FORMF_VAL)
          .withFunctionArgs(mapper.createObjectNode())
          .build();
  public static List<Arguments> provider =
      List.of(
          Arguments.of(emptyFormula),
          Arguments.of(formulaInvalidFunction),
          Arguments.of(formulaFunExample),
          Arguments.of(formulaFunExampleFail));

  @ParameterizedTest
  @MethodSource("providesFormulas")
  void checkFormulaValidity(FormulaCheck formula) {
    Sc2Formula fc = FormulaParser.parse(formula.json);
    assertThat(fc.isValid()).isEqualTo(formula.checker.valid);
  }

  @ParameterizedTest
  @MethodSource("providesFormulas")
  void checkFormulaFunctionOutput(FormulaCheck formula) {
    Sc2Formula fc = FormulaParser.parse(formula.json);
    assumeThat(fc.isValid()).isTrue();
    assertThat(fc.function().get().buildString()).isEqualTo(FORMA_FINAL_ANS);
  }

  private static List<Arguments> providesFormulas() {
    return provider;
  }
}
