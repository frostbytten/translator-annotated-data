package org.agmip.translators.annotated.sidecar2.components;

import io.vavr.collection.List;

public class Sc2RelationKey {
  private final String _file;
  private final String _sheet;
  private final int _table;
  private final List<Integer> _keys;

  public Sc2RelationKey(String file, String sheet, Integer table, List<Integer> keys) {
    this._file = file;
    this._sheet = sheet;
    this._table = table == null ? 1 : table;
    this._keys = keys;
  }

  public String getFile() {
    return _file;
  }

  public String getSheet() {
    return _sheet;
  }

  public int getTableIndex() {
    return _table;
  }

  public List<Integer> getKeys() {
    return _keys;
  }

  @Override
  public int hashCode() {
    int result = 13;

    result = ((_file != null) ? 53 * result + _file.hashCode() : 47 * result);
    result = ((_sheet != null) ? 53 * result + _sheet.hashCode() : 47 * result);
    result = 59 * result + _keys.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != this.getClass()) {
      return false;
    }
    final Sc2RelationKey other = (Sc2RelationKey) o;
    if (this._file == null ? other.getFile() != null : !this._file.equals(other.getFile()))
      return false;
    if (this._sheet == null ? other.getSheet() != null : !this._sheet.equals(other.getSheet()))
      return false;
    return this._keys.equals(other._keys);
  }

  @Override
  public String toString() {
    return _file + ((_sheet == null) ? "" : ("[" + _sheet + "]")) + "(" + _keys.mkString(",") + ")";
  }
}
