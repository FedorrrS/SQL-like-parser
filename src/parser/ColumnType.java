/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package src.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ColumnType {
    STRING("lastName"),
    BOOLEAN("active"),
    DOUBLE("cost"),
    LONG("id", "age");

    private final Set<String> columns;

    ColumnType(String... columns) {
        this.columns = new HashSet<>(Arrays.asList(columns));
    }

    public boolean contains(String col) {
        return columns.contains(col);
    }
}
