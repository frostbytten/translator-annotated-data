package org.agmip.translators.annotated.app;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SUPPORTED_MIME;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.agmip.translators.annotated.sidecar2.Sidecar2;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.parsers.Sidecar2Parser;

public class Application {
  public static void main(String[] args) throws IOException, JsonProcessingException {
    Path sc2Path = null;
    Path workDir = null;
    if (args.length == 2) {
      sc2Path = Paths.get(args[0]).toAbsolutePath();
      workDir = Paths.get(args[1]).toAbsolutePath();
    } else {
      System.err.println("Weird arguments:");
      for (String arg : args) {
        System.err.println("\t" + arg);
      }
    }
    if (sc2Path != null && workDir != null) {
      if (Files.isReadable(sc2Path) && Files.isReadable(workDir)) {
        Sidecar2 sc2 = processSC2(sc2Path);
        preflightSC2Files(sc2, workDir);
        determineRealizationOrder(sc2);
      }
    } else {
      System.err.println("Issue running the application");
    }
  }

  private static List<Sc2Sheet> validSheets(List<Sc2Sheet> allSheets) {
    return allSheets.stream().filter(Sc2Sheet::isValid).collect(Collectors.toList());
  }

  private static List<Sc2Rule> validRules(List<Sc2Rule> allRules) {
    return allRules.stream().filter(Sc2Rule::isValid).collect(Collectors.toList());
  }

  public static Sidecar2 processSC2(Path sc2Path) throws IOException, JsonProcessingException {
    System.out.println(sc2Path.toFile());
    Sidecar2 sc2 = Sidecar2Parser.parse(sc2Path.toFile());
    System.out.println("--- PARSING SC2 File ---");
    System.out.println("Translation unit is... " + sc2.isValid());
    System.out.println(
        "Number of valid files: [" + sc2.files().size() + "/" + sc2.allFiles().size() + "]");
    for (Sc2FileReference fc : sc2.invalidFiles()) {
      System.out.println(
          "File content-type supported... "
              + Arrays.stream(SUPPORTED_MIME).anyMatch(fc.getContentType()::equalsIgnoreCase));
      System.out.println(
          "Number of valid sheets in "
              + fc.getName()
              + ":"
              + "["
              + validSheets(fc.sheets()).size()
              + "/"
              + fc.sheets().size()
              + "]");
    }
    for (Sc2FileReference fc : sc2.files()) {
      System.out.println("Processing valid file: " + fc.getName().get());
      System.out.println(
          "Number of valid sheets in "
              + fc.getName().get()
              + ": "
              + "["
              + validSheets(fc.sheets()).size()
              + "/"
              + fc.sheets().size()
              + "]");
      for (Sc2Sheet sc : validSheets(fc.sheets())) {
        System.out.println("Processing sheet: " + sc.getName().get());
        System.out.println(
            "Number of valid rules in "
                + sc.tryName("unnamed sheet")
                + ": ["
                + validRules(sc.rules()).size()
                + "/"
                + sc.rules().size()
                + "]");
      }
    }
    return sc2;
  }

  public static void preflightSC2Files(Sidecar2 scf, Path workDir) {
    System.out.println("--- PREFLIGHT CHECK FOR FILES ---");
    System.out.println("!!! PREFLIGHT DISABLED !!!");
  }

  public static void determineRealizationOrder(Sidecar2 scf) {
  }
}
