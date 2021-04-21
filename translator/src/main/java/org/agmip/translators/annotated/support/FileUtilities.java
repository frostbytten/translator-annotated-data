package org.agmip.translators.annotated.support;

import java.nio.file.Files;
import java.nio.file.Path;

import org.agmip.translators.annotated.sidecar2.components.Sc2File;

public class FileUtilities {
  public static Path resolveSc2Path(Sc2File file, Path workingDir) {
    if (file.getName().isPresent()) {
      Path finalPath = workingDir.resolve(file.getName().get());
      if (!Files.isReadable(finalPath)) {
        return null;
      }
      return finalPath;
    }
    return null;
  }
}
