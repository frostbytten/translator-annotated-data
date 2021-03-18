package org.agmip.translators.annotated.sidecar2.components;

import java.util.Arrays;
import java.util.HashSet;

public class Sc2Relation {
    public enum Sc2RelationKeyType {
        PRIMARY,
        FOREIGN
    }
    private final Sc2RelationPart _primary;
    private final Sc2RelationPart _foreign;
    private boolean valid;
    public Sc2Relation(Sc2RelationPart primary, Sc2RelationPart foreign){
        this._primary = primary;
        this._foreign = foreign;
        this.valid = validate();
    }

    private boolean validate() {
        return this._primary.isValid() && this._foreign.isValid();
    }

    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
    }


    public static class Sc2RelationPart {
        private final String _file;
        private final String _sheet;
        private final int[] _keys;
        private final Sc2RelationKeyType _type;
        private final StringBuilder _reason;
        private boolean valid;

        public Sc2RelationPart(String file, String sheet, int[] keys, Sc2RelationKeyType scrType) {
            this._reason = new StringBuilder();
            this._file = file;
            this._sheet = sheet;
            this._keys = keys;
            this._type = scrType;
            this.valid = validate();
        }

        private boolean validate() {
            boolean validated = true;
            if (_file == null) {
                invalidate("No file specified");
                validated = false;
            }
            if (_type == null) {
                invalidate("No relationship type defined");
                validated = false;
            }
            if (_keys.length < 1) {
                invalidate("No columns selected to use as keys");
                validated = false;
            }
            if (Arrays.stream(_keys).anyMatch(i -> i < 0)) {
                invalidate("No valid columns provided to use as keys");
                validated = false;
            }
            if (!Arrays.stream(_keys).allMatch(new HashSet<>()::add)) {
                invalidate("Duplicate columns found as a key");
                validated = false;
            }
            return validated;
        }

        public boolean isValid() {
            return valid;
        }

        private void invalidate(String reason) {
            this._reason.append("[relation] ");
            this._reason.append(reason);
            this._reason.append("\n");
            valid = false;
        }
        public String reason() {
            return _reason.toString();
        }
    }

}
