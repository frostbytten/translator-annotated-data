package org.agmip.translators.annotated.sidecar2.resolve;

import org.agmip.translators.annotated.sidecar2.components.Sc2File;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

import java.util.HashMap;
import java.util.Map;


public class DataRegistry {
    private final Map<String, DataContext> registry;

    public DataRegistry() {
        registry = new HashMap<>();
    }

    public boolean add(Sc2File file, Sc2Sheet sheet) {
        DataContext loc = new DataContext(file, sheet);
        DataRange range = new DataRange(sheet.getDataStartRow(), sheet.getDataEndRow());
        String key = loc.getKey();
        boolean added;
        if (registry.containsKey(key)) {
            loc = registry.get(key);
            added = loc.addRange(range);
        } else {
            loc.addRange(range);
            registry.put(key, loc);
            added = true;
        }
        return added;
    }

    public DataContext get(String key) {
        return registry.get(key);
    }
}
