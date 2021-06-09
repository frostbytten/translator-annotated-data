package org.agmip.translators.annotated.data;

import java.nio.file.Path;
import java.util.Objects;

import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;

public class DataFileKey {
  private final Path fileLocation;
  private final String key;
  private final Sc2FileReference fileRef;

  private DataFileKey(Sc2FileReference fileRef) {
    this.fileRef = fileRef;
    this.fileLocation = Path.of(fileRef.location());
    this.key = fileLocation.getFileName().toString();
  }

  public Path getPath() {
    return fileLocation;
  }

  public String getKey() {
    return key;
  }

  public Sc2FileReference getFileReference() {
    return fileRef;
  }

  public static DataFileKey createKey(Sc2FileReference fileRef) {
    if (!fileRef.location().getScheme().equals("file")) {
      return null;
    }
    return new DataFileKey(fileRef);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataFileKey that = (DataFileKey) o;
    return fileLocation.equals(that.fileLocation) && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileLocation, key);
  }

  @Override
  public String toString() {
    return getKey() + "[" + getPath() + "]";
  }
}
