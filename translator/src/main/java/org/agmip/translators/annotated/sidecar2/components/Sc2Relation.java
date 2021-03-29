package org.agmip.translators.annotated.sidecar2.components;

import java.util.Arrays;
import java.util.HashSet;

public class Sc2Relation {
    private final Sc2RelationPart _primary;
    private final Sc2RelationPart _foreign;
    private boolean valid;
    public Sc2Relation(Sc2RelationPart primary, Sc2RelationPart foreign){
        this._primary = primary;
        this._foreign = foreign;
        this.valid = validate();
    }

    public Sc2RelationPart getPrimary() {
        return _primary;
    }

    public Sc2RelationPart getForeign() {
        return _foreign;
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
        private final StringBuilder _reason;
        private boolean valid;

        public Sc2RelationPart(String file, String sheet, int[] keys) {
            this._reason = new StringBuilder();
            this._file = file;
            this._sheet = sheet;
            this._keys = keys;
            this.valid = validate();
        }

        private boolean validate() {
            boolean validated = true;
            if (_file == null) {
                invalidate("No file specified");
                validated = false;
            }
            if (_keys.length < 1) {
                invalidate("No columns selected to use as keys");
                validated = false;
            }
            if (Arrays.stream(_keys).anyMatch(i -> i < 0)) {
                invalidate("Invalid columns provided to use as keys");
                validated = false;
            }
            if (!Arrays.stream(_keys).allMatch(new HashSet<>()::add)) {
                invalidate("Duplicate columns found as a key");
                validated = false;
            }
            return validated;
        }

        public String getFile() {
            return _file;
        }

        public String getSheet() {
            return _sheet;
        }

        public int[] getKeys() {
            return _keys;
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

        @Override
        public int hashCode() {
            int result = 13;

            result = ((_file != null) ? 53 * result + _file.hashCode() : 47 * result );
            result = ((_sheet != null) ? 53 * result + _sheet.hashCode() : 47 * result );
            result = 59 * result + Arrays.hashCode(_keys);
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
            if(o.getClass() != this.getClass()) {
                return false;
            }
            final Sc2RelationPart other = (Sc2RelationPart) o;
            if (this._file == null ? other.getFile() != null : !this._file.equals(other.getFile())) return false;
            if (this._sheet == null ? other.getSheet() != null : !this._sheet.equals(other.getSheet())) return false;
            return Arrays.equals(this._keys, other.getKeys());
        }

        @Override
        public String toString() {
            return _file + ((_sheet == null) ? "" : ("["+_sheet+"]")) + Arrays.toString(_keys);
        }
    }
}
