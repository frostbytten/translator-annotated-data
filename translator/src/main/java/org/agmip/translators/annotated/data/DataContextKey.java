package org.agmip.translators.annotated.data;

import java.util.Objects;

import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;

public class DataContextKey {
  private final String file;
  private final String sheet;
  private final int tableId;
  private final DataFileKey initialKey;
  private final Sc2FileReference fileRef;
  private final boolean complete;

  public DataContextKey(DataFileKey initialKey, String sheet, int tableId) {
    this.initialKey = initialKey;
    this.file = initialKey.getKey();
    this.sheet = sheet;
    this.tableId = tableId;
    this.fileRef = initialKey.getFileReference();
    this.complete = true;
  }

  public DataContextKey(DataFileKey initialKey, String sheet) {
    this(initialKey, sheet, -1);
  }

  public DataContextKey changeTableId(int newId) {
    return new DataContextKey(this.initialKey, this.sheet, newId);
  }

  public DataContextKey(String file, String sheet, int table) {
    this.file = file;
    this.sheet = sheet;
    this.tableId = table;
    this.initialKey = null;
    this.fileRef = null;
    this.complete = false;
  }

  public String getFile() {
    return file;
  }

  public String getSheetName() {
    return sheet;
  }

  public int getTableId() {
    return tableId;
  }

  public Sc2FileReference getFileRef() {
    return fileRef;
  }

  public boolean isComplete() {
    return complete;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataContextKey that = (DataContextKey) o;
    return tableId == that.tableId
        && Objects.equals(file, that.file)
        && Objects.equals(sheet, that.sheet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(file, sheet, tableId);
  }

  @Override
  public String toString() {
    return "DataContextKey{"
        + "file='"
        + file
        + '\''
        + ", sheet='"
        + sheet
        + '\''
        + ", tableId="
        + tableId
        + '}';
  }
}
