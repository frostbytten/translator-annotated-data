package org.agmip.translators.annotated.data;

import java.nio.file.Path;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.agmip.translators.annotated.sidecar2.Utilities;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class DataContext {
  private final String _filename;
  private final String _sheetname;
  private final Sc2FileReference _file;
  private final Sc2Sheet _sheet;
  private Set<DataRange> _ranges;
  private int _rootScore;

  public DataContext(Sc2FileReference file, Sc2Sheet sheet) {
    _file = file;
    _sheet = sheet;
    _filename = Path.of(file.location()).toString();
    _sheetname = sheet.tryName("_");
    _ranges = HashSet.empty();
    _rootScore = 0;
  }

  public String filename() {
    return _filename;
  }

  public String sheetname() {
    return _sheetname;
  }

  public Sc2FileReference file() {
    return _file;
  }

  public Sc2Sheet sheet() {
    return _sheet;
  }

  public DataContext addRange(DataRange range) {
    _ranges = _ranges.add(range);
    return this;
  }

  public Set<DataRange> ranges() {
    return _ranges;
  }

  public String getKey() {
    return _filename + "$$" + _sheetname;
  }

  public int rootScore() {
    return _rootScore;
  }

  public void incrementRootScore() {
    incrementRootScore(1);
  }

  public void incrementRootScore(int inc) {
    _rootScore += inc;
  }

  public void decrementRootScore() {
    decrementRootScore(1);
  }

  public void decrementRootScore(int dec) {
    _rootScore += dec;
  }

  @Override
  public String toString() {
    return toString(true);
  }

  public String toString(boolean withRange) {
    return "file="
        + _filename
        + ((_sheetname != null) ? (";sheet=" + _sheetname) : "")
        + ((withRange) ? _ranges.toString() : "");
  }

  public int maxBound() {
    return Utilities.getMaxSheetColumn(_sheet);
  }
}
