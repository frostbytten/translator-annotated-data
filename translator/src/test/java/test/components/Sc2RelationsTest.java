package test.components;

import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation.Sc2RelationKeyType;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation.Sc2RelationPart;
import org.agmip.translators.annotated.sidecar2.parsers.RelationParser;

import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.Sidecar2SampleKeys.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.samples.RelationGenerator.RelationCheck;
import test.samples.RelationGenerator.RelationsCheck;
import test.samples.RelationGenerator.RelationCheckBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Sc2RelationsTest {
    public static RelationCheck relEmpty = new RelationCheckBuilder(false)
            .build();

    public static RelationCheck relMinimum = new RelationCheckBuilder(true)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .addKeyColumn(1)
            .build();

    public static RelationCheck relFull = new RelationCheckBuilder(true)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .withSheet(SSN_VAL)
            .addKeyColumn(1)
            .build();

    public static RelationCheck relWithoutColumns = new RelationCheckBuilder(false)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .build();

    public static RelationCheck relWithInvalidColumn = new RelationCheckBuilder(false)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .addKeyColumn(-7)
            .build();

    public static RelationCheck relWithDuplicateColumn = new RelationCheckBuilder(false)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .addKeyColumn(0)
            .addKeyColumn(0)
            .build();

    public static RelationCheck relWithMultipleColumn = new RelationCheckBuilder(true)
            .withFile(FMFN_XLSX_VAL)
            .withRelationType(Sc2RelationKeyType.PRIMARY)
            .addKeyColumn(0)
            .addKeyColumn(1)
            .build();

    public static List<Arguments> provider = List.of(
            Arguments.of(relEmpty),
            Arguments.of(relMinimum),
            Arguments.of(relFull),
            Arguments.of(relWithoutColumns),
            Arguments.of(relWithInvalidColumn),
            Arguments.of(relWithDuplicateColumn),
            Arguments.of(relWithMultipleColumn)
    );

    private static List<Arguments> providesRelations() {
        return provider;
    }

    @ParameterizedTest
    @MethodSource("providesRelations")
    void shouldValidateRelation(RelationCheck rel) {
        Sc2RelationPart part = RelationParser._parse(rel.json, rel.checker.scrType);
        System.out.println(part.reason());
        assertThat(part.isValid()).isEqualTo(rel.checker.valid);
    }
}
