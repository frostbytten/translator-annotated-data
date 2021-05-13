package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FILES_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FILE_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.parsers.FilesParser;
import org.junit.jupiter.api.Test;

public class FilesTest {
  public static JsonNode filesAllGood =
      mapper
          .createObjectNode()
          .set(
              FILES_FIELD,
              mapper
                  .createArrayNode()
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.csvFull.json))
                  .add(
                      mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxFull.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.xlsxFileOnly.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.xlsxUrlOnly.json)));

  public static JsonNode filesAllBad =
      mapper
          .createObjectNode()
          .set(
              FILES_FIELD,
              mapper
                  .createArrayNode()
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileEmpty.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidCT.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidFileAndUrl.json)));

  public static JsonNode filesSomeGood =
      mapper
          .createObjectNode()
          .set(
              FILES_FIELD,
              mapper
                  .createArrayNode()
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.csvFull.json))
                  .add(
                      mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxFull.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileEmpty.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidCT.json))
                  .add(
                      mapper
                          .createObjectNode()
                          .set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidFileAndUrl.json)));

  public static int BAD_FILES_COUNT = 3;
  public static int GOOD_FILES_COUNT = 4;
  public static int SOME_GOOD_FILES_COUNT = 2;
  private static Path workDir = Path.of("tmp", "ft");

  @Test
  void worksWithGoodFiles() throws JsonProcessingException {
    JsonNode filesNode = filesAllGood.path("files");
    int numFiles = filesNode.size();
    System.out.println(filesAllGood.toString());
    List<Validation<Seq<String>, Sc2FileReference>> files = FilesParser.parse(filesNode, workDir);
    assertEquals(numFiles, files.size());
    assertThat(files.forAll(Validation::isValid)).isTrue();
  }

  @Test
  void failsWithBadFiles() throws JsonProcessingException {
    System.out.println(filesAllBad.toString());
    List<Validation<Seq<String>, Sc2FileReference>> files =
        FilesParser.parse(filesAllBad.path("files"), workDir);
    assertEquals(BAD_FILES_COUNT, files.size());
    assertThat(files.forAll(Validation::isInvalid)).isTrue();
  }
}
