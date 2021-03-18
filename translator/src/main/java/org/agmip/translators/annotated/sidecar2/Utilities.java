package org.agmip.translators.annotated.sidecar2;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum Utilities {
    INSTANCE;
    public final static ObjectMapper mapper = new ObjectMapper();
    public static Integer convertStringToInteger(String val) {
        Integer retval = null;
        try {
            retval = Integer.valueOf(val);
        } catch (NumberFormatException _ex) {
            // Log the error
        }
        return retval;
    }
}
