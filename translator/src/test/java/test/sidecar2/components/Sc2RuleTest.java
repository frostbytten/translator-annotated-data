package test.sidecar2.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.RuleGenerator.*;
import static test.samples.Sidecar2SampleKeys.*;

import java.util.List;
import java.util.Optional;

import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule.RuleType;
import org.agmip.translators.annotated.sidecar2.parsers.RuleParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Sc2RuleTest {
  public static RuleCheck ruleMinimum =
      new RuleCheckBuilder(true).withIcasa(SMI_VAL).withColumnIndex(SMCI_VAL).build();

  public static RuleCheck ruleFull =
      new RuleCheckBuilder(true)
          .withIcasa(SMI_VAL)
          .withColumnIndex(SMCI_VAL)
          .withUnit(SMU_VAL)
          .withCategory(SMC_VAL)
          .withFormat(SMF_VAL)
          .build();

  public static RuleCheck ruleInvalidCI =
      new RuleCheckBuilder(false).withIcasa(SMI_VAL).withColumnIndex(SMCI_INVAL).build();

  public static RuleCheck ruleInvalidCC =
      new RuleCheckBuilder(false)
          .withIcasa(SMI_VAL)
          .withColumnIndex(SMCI_VAL)
          .withCategory(SMC_INVAL)
          .build();

  public static RuleCheck ruleMissingIcasa =
      new RuleCheckBuilder(false).withColumnIndex(SMCI_VAL).build();

  public static RuleCheck ruleMissingCI = new RuleCheckBuilder(false).withIcasa(SMI_VAL).build();

  public static RuleCheck ruleValue =
      new RuleCheckBuilder(true).withIcasa(SMI_VAL).withValue(SMV_VAL).build();

  public static RuleCheck ruleExplicitNullValue =
      new RuleCheckBuilder(false).withIcasa(SMI_VAL).withValue(null).build();

  public static RuleCheck ruleValueWithColumnIndex =
      new RuleCheckBuilder(true)
          .withIcasa(SMI_VAL)
          .withColumnIndex(SMCI_VAL)
          .withValue(SMV_VAL)
          .build();

  public static RuleCheck ruleEmpty = new RuleCheckBuilder(false).build();

  public static List<Arguments> provider =
      List.of(
          Arguments.of(ruleMinimum),
          Arguments.of(ruleFull),
          Arguments.of(ruleInvalidCI),
          Arguments.of(ruleInvalidCC),
          Arguments.of(ruleMissingIcasa),
          Arguments.of(ruleMissingCI),
          Arguments.of(ruleValue),
          Arguments.of(ruleExplicitNullValue),
          Arguments.of(ruleValueWithColumnIndex),
          Arguments.of(ruleEmpty));

  @ParameterizedTest
  @MethodSource("providesRules")
  void rulesShouldValidate(RuleCheck rule) {
    Sc2Rule rc = RuleParser.parse(rule.json);
    assertThat(rc.isValid()).isEqualTo(rule.checker.valid);
  }

  @ParameterizedTest
  @MethodSource("providesRules")
  void rulesShouldExposeFields(RuleCheck rule) {
    Sc2Rule rc = RuleParser.parse(rule.json);
    assertThat(rc.getVariable()).isEqualTo(rule.checker.icasa);
    if (rule.checker.columnIndex == null) {
      assertThat(rc.getColumnIndex()).isEqualTo(-1);
    } else {
      assertThat(rc.getColumnIndex()).isEqualTo(rule.checker.columnIndex);
    }
    if (rule.checker.category == null) {
      assertThat(rc.getCategory()).isEqualTo(-1);
    } else {
      assertThat(rc.getCategory()).isEqualTo(rule.checker.category);
    }
    if (rule.checker.unit == null) {
      assertThat(rc.getUnit()).isEmpty();
    } else {
      assertThat(rc.getUnit()).isEqualTo(Optional.of(rule.checker.unit));
    }
    if (rule.checker.value == null) {
      assertThat(rc.getValue()).isEmpty();
      assertThat(rc.getRuleType()).isEqualTo(RuleType.EXTRACTION_RULE);
    } else {
      assertThat(rc.getValue()).isEqualTo(Optional.of(rule.checker.value));
    }
  }

  @ParameterizedTest
  @MethodSource("providesRules")
  void valueOnlyRulesShouldHaveValueRuleType(RuleCheck rule) {
    Sc2Rule rc = RuleParser.parse(rule.json);
    assumeThat(rc.getColumnIndex()).isEqualTo(-1);
    assumeThat(rc.getValue()).isNotEmpty();
    assertThat(rc.getRuleType()).isEqualTo(RuleType.VALUE_RULE);
  }

  @ParameterizedTest
  @MethodSource("providesRules")
  void allOtherRulesShouldHaveExtractionRuleType(RuleCheck rule) {
    Sc2Rule rc = RuleParser.parse(rule.json);
    assumeThat(rc.getColumnIndex()).isNotEqualTo(-1);
    assertThat(rc.getRuleType()).isEqualTo(RuleType.EXTRACTION_RULE);
  }

  private static List<Arguments> providesRules() {
    return provider;
  }
}
