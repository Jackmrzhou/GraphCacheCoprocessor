package org.gilmour.coprocessor.utils;

import org.apache.hadoop.hbase.Cell;

public class Utils {
    public static Cell.Type codeToType(int code) {
        if (code == 4)
            return Cell.Type.Put;
        if (code == 8)
            return Cell.Type.Delete;
        if (code == 10)
            return Cell.Type.DeleteFamilyVersion;
        if (code == 12)
            return Cell.Type.DeleteColumn;
        if (code == 14)
            return Cell.Type.DeleteFamily;
        return null;
    }
}
