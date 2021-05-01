package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.ATM_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.assertj.core.api.Assertions.assertThat;
import static test.samples.Sidecar2SampleKeys.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.jimfs.Jimfs;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.parsers.Sidecar2Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Sidecar2Test {
  public static JsonNode allGoodSc2 =
      mapper.createObjectNode().set(ATM_FIELD, FilesTest.filesAllGood);
  public static JsonNode allBadSc2 =
      mapper.createObjectNode().set(ATM_FIELD, FilesTest.filesAllBad);
  public static JsonNode mixedSc2 =
      mapper.createObjectNode().set(ATM_FIELD, FilesTest.filesSomeGood);
  private static Path workDir;

  @BeforeAll
  static void setupFileSystem() throws IOException {
    FileSystem fs = Jimfs.newFileSystem();
    workDir = fs.getPath("work");
    Files.createDirectory(workDir);
    Files.createFile(workDir.resolve(FMFN_CSV_VAL));
    Files.createFile(workDir.resolve(FMFN_XLSX_VAL));
  }

  @Test
  void validWithGoodFiles() throws IOException, JsonProcessingException {
    Sidecar2 tu = Sidecar2Parser.parse(allGoodSc2);
    assertThat(tu.isValid()).isTrue();
    assertThat(tu.files().size()).isEqualTo(FilesTest.GOOD_FILES_COUNT);
    assertThat(tu.areAllFilesValid()).isTrue();
  }

  @Test
  void invalidWithBadFiles() throws IOException, JsonProcessingException {
    System.out.println(mixedSc2.toString());
    Sidecar2 tu = Sidecar2Parser.parse(mixedSc2);
    assertThat(tu.isValid()).isFalse();
    assertThat(tu.areAllFilesValid()).isFalse();
    assertThat(tu.areAnyFilesValid()).isTrue();
    assertThat(tu.files().size()).isEqualTo(FilesTest.SOME_GOOD_FILES_COUNT);
    assertThat(tu.invalidFiles().size()).isEqualTo(FilesTest.BAD_FILES_COUNT);
  }
}
