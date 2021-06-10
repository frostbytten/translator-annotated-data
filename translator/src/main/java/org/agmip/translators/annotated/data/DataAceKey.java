package org.agmip.translators.annotated.data;

import io.vavr.collection.List;

public class DataAceKey {
  private final String file;
  private final String sheet;
  private final int table;
  private final String foreignKey;

  public DataAceKey(DataContextKey context, List<Integer> relColumns) {
    file = context.getFile();
    sheet = context.getSheetName();
    table = context.getTableId();
    foreignKey = relColumns.mkString(",");
  }
}
