package org.agmip.translators.annotated.app;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.agmip.translators.annotated.Sc2Translator;

public class Application {
  public static void main(String[] args) {
    String sc2Path = null;
    Path workDir = null;
    if (args.length == 2) {
      sc2Path = args[0];
      workDir = Paths.get(args[1]).toAbsolutePath();
    } else {
      System.err.println("Weird arguments:");
      for (String arg : args) {
        System.err.println("\t" + arg);
      }
    }
    Sc2Translator tr = new Sc2Translator();
    tr.setWorkDirectory(workDir);
    tr.addFile(sc2Path);
    tr.translate();
    //    preflightSC2Files(sc2, workDir);
    //    Sidecar2 sc2 = processSC2(sc2Path);
    //    determineRealizationOrder(sc2);
  }
}
