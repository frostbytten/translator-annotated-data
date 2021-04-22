package test.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FILES_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.FILE_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxFull.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxFileOnly.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxUrlOnly.json)));

  public static JsonNode filesAllBad =
      mapper
          .createObjectNode()
          .set(
              FILES_FIELD,
              mapper
                  .createArrayNode()
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileEmpty.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidCT.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidFileAndUrl.json)));

  public static JsonNode filesSomeGood =
      mapper
          .createObjectNode()
          .set(
              FILES_FIELD,
              mapper
                  .createArrayNode()
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.csvFull.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.xlsxFull.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileEmpty.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidCT.json))
                  .add(mapper.createObjectNode().set(FILE_FIELD, Sc2FileReferenceTest.fileInvalidFileAndUrl.json)));

  public static int BAD_FILES_COUNT = 3;
  public static int GOOD_FILES_COUNT = 4;
  public static int SOME_GOOD_FILES_COUNT = 2;

  @Test
  void worksWithGoodFiles() throws JsonProcessingException {
    JsonNode filesNode = filesAllGood.path("files");
    int numFiles = filesNode.size();
    System.out.println(filesAllGood.toString());
    List<Sc2FileReference> files = FilesParser.parse(filesNode);
    assertEquals(numFiles, files.size());
    assertTrue(files.stream().allMatch(Sc2FileReference::isValid));
  }

  @Test
  void failsWithBadFiles() throws JsonProcessingException {
    System.out.println(filesAllBad.toString());
    List<Sc2FileReference> files = FilesParser.parse(filesAllBad.path("files"));
    assertEquals(BAD_FILES_COUNT, files.size());
    assertFalse(files.stream().allMatch(Sc2FileReference::isValid));
  }
}
