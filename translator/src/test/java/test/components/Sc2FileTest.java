package test.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.agmip.translators.annotated.sidecar2.parsers.FileParser;
import org.agmip.translators.annotated.sidecar2.components.Sc2File;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static test.samples.FileGenerator.FileCheck;
import static test.samples.FileGenerator.FileCheckBuilder;

import static test.samples.Sidecar2SampleKeys.*;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

public class Sc2FileTest {
    public static FileCheck csvFull = new FileCheckBuilder(true)
            .withContentType(FMCT_CSV_VAL)
            .withName(FMFN_CSV_VAL)
            .build();

    public static FileCheck xlsxFull = new FileCheckBuilder(true)
            .withContentType(FMCT_XLSX_VAL)
            .withName(FMFN_XLSX_VAL)
            .withUrl(FMFU_XLSX_VAL)
            .build();

    public static FileCheck xlsxFileOnly = new FileCheckBuilder(true)
            .withContentType(FMCT_XLSX_VAL)
            .withName(FMFN_XLSX_VAL)
            .build();

    public static FileCheck xlsxUrlOnly = new FileCheckBuilder(true)
            .withContentType(FMCT_XLSX_VAL)
            .withUrl(FMFU_XLSX_VAL)
            .build();

    public static FileCheck fileEmpty = new FileCheckBuilder(false)
            .build();

    public static FileCheck fileInvalidCT = new FileCheckBuilder(false)
            .withContentType(FMCT_UNS_VAL)
            .withName(FMFN_UNS_VAL)
            .build();

    public static FileCheck fileInvalidFileAndUrl = new FileCheckBuilder(false)
            .withContentType(FMCT_XLSX_VAL)
            .build();

    public static List<Arguments> provider = List.of(
            Arguments.of(csvFull),
            Arguments.of(xlsxFull),
            Arguments.of(xlsxFileOnly),
            Arguments.of(xlsxUrlOnly),
            Arguments.of(fileEmpty),
            Arguments.of(fileInvalidCT),
            Arguments.of(fileInvalidFileAndUrl)
    );

    private static List<Arguments> providesFiles() {
        return provider;
    }

    @ParameterizedTest
    @MethodSource("providesFiles")
    void shouldValidateFileMetadata(FileCheck file) throws JsonProcessingException {
        Sc2File fe = FileParser.parse(file.json);
        assertThat(fe.isValid()).isEqualTo(file.checker.valid);
    }

    @ParameterizedTest
    @MethodSource("providesFiles")
    void shouldBeAbleToExtractFileNames(FileCheck file) throws JsonProcessingException {
        Sc2File fe = FileParser.parse(file.json);
        assumeThat(fe.isValid()).isTrue();
        assumeThat(fe.getUrl()).isEmpty();
        assertThat(fe.getName().get()).isEqualTo(file.checker.name);
        assertThat(fe.getContentType()).isEqualTo(file.checker.contentType);
    }

    @ParameterizedTest
    @MethodSource("providesFiles")
    void canExtractUrls(FileCheck file) throws JsonProcessingException {
        Sc2File fe = FileParser.parse(file.json);
        assumeThat(fe.isValid()).isTrue();
        assumeThat(fe.getUrl()).isNotEmpty();
        assertThat(fe.getUrl().get()).isEqualTo(file.checker.url);
        assertThat(fe.getContentType()).isEqualTo(file.checker.contentType);
    }
}
