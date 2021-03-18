package test.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.agmip.translators.annotated.sidecar2.functions.Sc2JoinColumns;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.agmip.translators.annotated.sidecar2.functions.Sc2JoinColumns.JC_VD;
import static org.agmip.translators.annotated.sidecar2.functions.Sc2JoinColumns.JC_VVK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

public class Sc2JoinColumnsTest {
    private static final Check emptyArgs = new Builder(false)
            .build();

    private static final Check fullArgs = new Builder(true)
            .withJoiner("-")
            .addColumn(1)
            .addColumn(2)
            .addColumn(3)
            .build();
    private static final String JC_VALID_BUILD = "Columns: 1,2,3 joined by -";
    private static final String JC_VALID_BUILD2 = "Columns: 1,1,2,3 joined by -";
    private static final String JC_VALID_BUILD3 = "Columns: 1,2,3 joined by  ";

    private static final Check repeatedColumns = new Builder(true)
            .withJoiner("-")
            .addColumn(1)
            .addColumn(1)
            .addColumn(2)
            .addColumn(3)
            .build();

    private static final Check noJoiner = new Builder(true)
            .addColumn(1)
            .addColumn(2)
            .addColumn(3)
            .build();

    private static final Check onlyJoiner = new Builder(false)
            .withJoiner("-")
            .build();

    private static final List<Arguments> provider = List.of(
            Arguments.of(emptyArgs),
            Arguments.of(fullArgs),
            Arguments.of(repeatedColumns),
            Arguments.of(noJoiner),
            Arguments.of(onlyJoiner)
    );

    private static List<Arguments> providesArgs() {
        return provider;
    }
    @ParameterizedTest
    @MethodSource("providesArgs")
    public void correctlyValidateArguments(Check args) {
        Sc2JoinColumns fun = new Sc2JoinColumns(args.json);
        assertThat(fun.isValid()).isEqualTo(args.checker.valid);
    }

    @ParameterizedTest
    @MethodSource("providesArgs")
    public void generatingCorrectBuildString(Check args) {
        Sc2JoinColumns fun = new Sc2JoinColumns(args.json);
        assumeThat(fun.isValid()).isTrue();
        assumeThat(fun.getJoiner()).isEqualTo("-");
        assumeThat(fun.getColumns()).hasSize(3);
        assertThat(fun.buildString()).isEqualTo(JC_VALID_BUILD);
    }
    @ParameterizedTest
    @MethodSource("providesArgs")
    public void generatingCorrectBuildString2(Check args) {
        Sc2JoinColumns fun = new Sc2JoinColumns(args.json);
        assumeThat(fun.isValid()).isTrue();
        assumeThat(fun.getJoiner()).isEqualTo("-");
        assumeThat(fun.getColumns()).hasSize(4);
        assertThat(fun.buildString()).isEqualTo(JC_VALID_BUILD2);
    }

    @ParameterizedTest
    @MethodSource("providesArgs")
    public void generatingCorrectBuildString3(Check args) {
        Sc2JoinColumns fun = new Sc2JoinColumns(args.json);
        assumeThat(fun.isValid()).isTrue();
        assumeThat(fun.getJoiner()).isEqualTo(" ");
        assertThat(fun.buildString()).isEqualTo(JC_VALID_BUILD3);
    }

    private static class Check {
        public JsonNode json;
        public CheckFrame checker;

        public Check (JsonNode json, CheckFrame checker) {
            this.json = json;
            this.checker = checker;
        }

        public String toString() {
            return json.toString() + " - " + checker.valid;
        }
    }

    private static class CheckFrame {
        public final String joiner;
        public final int[] columns;
        public final boolean valid;

        public CheckFrame(String joiner, int[] columns, boolean valid) {
            this.joiner = joiner;
            this.columns = columns;
            this.valid = valid;
        }
    }

    private static class Builder {
        private String joiner;
        private List<Integer> columns;
        private ArrayNode columnNode;
        private ObjectNode json;
        private final boolean valid;

        public Builder(boolean valid) {
            this.valid = valid;
            this.json = mapper.createObjectNode();
            this.columnNode = mapper.createArrayNode();
            this.columns = new ArrayList<>();
        }


        public Builder addColumn(int col) {
            columns.add(col);
            columnNode.add(col);
            return this;
        }

        public Builder withJoiner(String joiner) {
            this.joiner = joiner;
            json.put(JC_VD, joiner);
            return this;
        }

        public Check build() {
            json.set(JC_VVK, columnNode);
            if (this.columns.size() == 0) {
                return new Check(json, new CheckFrame(this.joiner, null, this.valid));
            } else {
                return new Check(json, new CheckFrame(this.joiner, this.columns.stream().mapToInt(i -> i).toArray(), this.valid));
            }
        }
    }
}
