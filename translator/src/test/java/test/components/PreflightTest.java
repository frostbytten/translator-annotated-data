package test.components;

import com.google.common.jimfs.Jimfs;
import org.agmip.translators.annotated.sidecar2.components.Sc2File;
import org.agmip.translators.annotated.sidecar2.parsers.FileParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.samples.FileGenerator;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.FileGenerator.FileCheck;
import static test.samples.Sidecar2SampleKeys.*;

public class PreflightTest {
    static FileSystem fs;
    @BeforeAll
    static void setupFileSystem() throws IOException  {
        fs = Jimfs.newFileSystem();
        Path workDir = fs.getPath("work");
        Files.createDirectory(workDir);
        Files.createFile(workDir.resolve(FMFN_XLSX_VAL));
    }

    private static List<Arguments> providesFiles() {
        return Sc2FileTest.provider;
    }

    @ParameterizedTest
    @MethodSource("providesFiles")
    void existingPreflightShouldStayValid(FileCheck file) {
        Sc2File fc = FileParser.parse(file.json);
        Path workDir = fs.getPath("work");
        assumeThat(fc.isValid()).isTrue();
        assumeThat(fc.getContentType()).isEqualTo(FMCT_XLSX_VAL);
        assumeThat(fc.getName()).isPresent();
        assertThat(fc.fileReachable(workDir)).isTrue();
        assertThat(fc.isValid()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("providesFiles")
    void missingPreflightShouldInvalidate(FileCheck file) {
        Sc2File fc = FileParser.parse(file.json);
        Path workDir = fs.getPath("work");
        assumeThat(fc.isValid()).isTrue();
        assumeThat(fc.getContentType()).isEqualTo(FMCT_CSV_VAL);
        assertThat(fc.fileReachable(workDir)).isFalse();
        assertThat(fc.isValid()).isFalse();
        assertThat(fc.reasonInvalid()).isEqualTo("Could not read file: example.csv\n");
    }

}
