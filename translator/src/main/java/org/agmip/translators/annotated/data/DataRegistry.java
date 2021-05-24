package org.agmip.translators.annotated.data;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.agmip.translators.annotated.sidecar2.components.Sc2FileReference;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class DataRegistry {
  private Map<String, DataContext> registry;

  public DataRegistry() {
    registry = HashMap.empty();
  }

  public void add(Sc2FileReference file, Sc2Sheet sheet) {
    DataContext loc = new DataContext(file, sheet);
    DataRange range = new DataRange(sheet.getDataStartRow(), sheet.getDataEndRow());
    String key = loc.getKey();
    DataContext entry = registry.getOrElse(key, loc).addRange(range);
    registry = registry.put(key, entry);
  }

  public Option<DataContext> get(String key) {
    return registry.get(key);
  }

  public Set<String> keys() {
    return registry.keySet();
  }

  public Seq<DataContext> contexts() {
    return registry.values();
  }
}
