/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package src.parser;

import java.util.*;

public class RequestParser extends BaseParser {
    private static final List<Map<String, Object>> data = new ArrayList<>();
    private final List<Tokens> columnTokens = new ArrayList<>();
    private Tokens commandToken;
    private final Map<String, Object> row = new LinkedHashMap<>();
    private final StringBuilder id = new StringBuilder();
    private final StringBuilder lastName = new StringBuilder();
    private final StringBuilder cost = new StringBuilder();
    private final StringBuilder age = new StringBuilder();
    private final StringBuilder active = new StringBuilder();
    private final Map<String, String[]> sampleTokens = new HashMap<>();
    private final StringBuilder predicate = new StringBuilder();
    private final Map<ColumnType, Set<String>> invalidOperations = Map.of(
            ColumnType.STRING, new HashSet<>(Arrays.asList(">=", "<=", "<", ">")),
            ColumnType.BOOLEAN, new HashSet<>(Arrays.asList("like", "ilike", ">=", "<=", "<", ">")),
            ColumnType.DOUBLE, new HashSet<>(Arrays.asList("like", "ilike")),
            ColumnType.LONG, new HashSet<>(Arrays.asList("like", "ilike"))
    );


    public RequestParser(CharSource source) {
        super(source);
    }

    public List<Map<String, Object>> parse() {
        parseRequest();
        return data;
    }

    private void parseRequest() {
        getCommandToken();
        switch (commandToken) {
            case INSERT -> handleInsert();
            case UPDATE -> handleUpdate();
            case DELETE -> handleDelete();
            case SELECT -> handleSelect();
            default -> throw new IllegalArgumentException("Invalid command: " + commandToken);
        }
    }

    private void handleInsert() {
        tokenize();
        if (columnTokens.isEmpty()) {
            throw new IllegalArgumentException("Empty arguments cannot be inserted");
        }
        insertData(row);
        data.add(row);
    }

    private void handleUpdate() {
        tokenize();
        data.stream()
                .filter(r -> {
                    int unsatisfyingPredicates = getUnsatisfyingPredicates(r);
                    if (!sampleTokens.isEmpty()) {
                        return (predicate.toString().equals("and") && unsatisfyingPredicates == 0) ||
                                (predicate.toString().equals("or") && unsatisfyingPredicates <= 1) ||
                                sampleTokens.size() == 1 && unsatisfyingPredicates == 0;
                    } else {
                        return true;
                    }
                })
                .forEach(this::insertData);
    }

    private void handleDelete() {
        tokenize();
        if (columnTokens.isEmpty()) {
            data.clear();
        } else {
            List<Map<String, Object>> toDelete = new ArrayList<>();
            changeCollection(toDelete);
            data.removeAll(toDelete);
        }
    }

    private void handleSelect() {
        tokenize();
        if (!columnTokens.isEmpty()) {
            List<Map<String, Object>> toSelect = new ArrayList<>();
            changeCollection(toSelect);
            data.removeIf(r -> !toSelect.contains(r));
        }
    }

    private void changeCollection(List<Map<String, Object>> command) {
        data.stream()
                .filter(r -> {
                    int satisfyingPredicates = getUnsatisfyingPredicates(r);
                    return predicate.toString().equals("and") && satisfyingPredicates == 0 ||
                            (predicate.toString().equals("or") && satisfyingPredicates <= 1) ||
                            (sampleTokens.size() == 1 && satisfyingPredicates == 0);
                })
                .forEach(command::add);
    }

    private void tokenize() {
        while (commandToken != Tokens.EOF) {
            getValueToken();
        }
    }

    private int getUnsatisfyingPredicates(Map<String, Object> r) {
        int unsatisfyingPredicates = sampleTokens.keySet().size();
        List<String> collumnsChecked = new ArrayList<>();
        for (int i = unsatisfyingPredicates; i > 0; i--) {
            unsatisfyingPredicates = typeChecking(r, unsatisfyingPredicates, collumnsChecked);
        }
        return unsatisfyingPredicates;
    }

    private int typeChecking(Map<String, Object> r, int unsatisfyingPredicates, List<String> collumnsChecked) {
        for (String type : Arrays.asList("id", "age", "cost", "active", "lastName")) {
            unsatisfyingPredicates = valueCheck(type, r, collumnsChecked, unsatisfyingPredicates);
        }
        return unsatisfyingPredicates;
    }

    private int valueCheck(String col, Map<String, Object> r, List<String> columnsChecked, int unsatisfyingPredicates) {
        if (sampleTokens.containsKey(col) && r.get(col) != null && !columnsChecked.contains(col)) {
            String op = sampleTokens.get(col)[1];
            String pattern = sampleTokens.get(col)[0];
            Object value = r.get(col);
            switch (op) {
                case "=" -> {
                    if (isEquals(r, col, pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case "!=" -> {
                    if (!isEquals(r, col, pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case ">=" -> {
                    if (value instanceof Double && Double.parseDouble(value + "") >= Double.parseDouble(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    } else if (value instanceof Long && Long.parseLong(value + "") >= Long.parseLong(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case "<=" -> {
                    if (value instanceof Double && Double.parseDouble(value + "") <= Double.parseDouble(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    } else if (value instanceof Long && Long.parseLong(value + "") <= Long.parseLong(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case "<" -> {
                    if (value instanceof Double && Double.parseDouble(value + "") < Double.parseDouble(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    } else if (value instanceof Long && Long.parseLong(value + "") < Long.parseLong(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case ">" -> {
                    if (value instanceof Double && Double.parseDouble(value + "") > Double.parseDouble(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    } else if (value instanceof Long && Long.parseLong(value + "") > Long.parseLong(pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case "like" -> {
                    if (isLike(r, col, pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                case "ilike" -> {
                    if (!isLike(r, col, pattern)) {
                        unsatisfyingPredicates--;
                        columnsChecked.add(col);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid comparison operator: " + op);
            }
        }
        return unsatisfyingPredicates;
    }

    private void getCommandToken() {
        skipwhitespace();
        if (eof()) {
            commandToken = Tokens.EOF;
        } else if (take("insert")) {
            skipwhitespace();
            if (take("values")) {
                commandToken = Tokens.INSERT;
            }
        } else if (take("update")) {
            skipwhitespace();
            if (take("values")) {
                commandToken = Tokens.UPDATE;
            }
        } else if (take("delete")) {
            commandToken = Tokens.DELETE;
        } else if (take("select")) {
            commandToken = Tokens.SELECT;
        } else {
            throw new IllegalArgumentException("Invalid command token");
        }
    }

    private void getValueToken() {
        skipUntilValidToken();
        if (eof()) {
            commandToken = Tokens.EOF;
        } else if (take("'")) {
            skipwhitespace();
            if (take("lastName")) {
                skipwhitespace();
                if (take("'")) {
                    if (commandToken == Tokens.WHERE) {
                        processingSample("lastName", ColumnType.STRING);
                    } else {
                        addValue(lastName, ColumnType.STRING);
                    }
                    columnTokens.add(Tokens.LAST_NAME);
                }
            } else if (take("id")) {
                skipwhitespace();
                if (take("'")) {
                    if (commandToken == Tokens.WHERE) {
                        processingSample("id", ColumnType.LONG);
                    } else {
                        addValue(id, ColumnType.LONG);
                    }
                    columnTokens.add(Tokens.ID);
                }
            } else if (take("age")) {
                skipwhitespace();
                if (take("'")) {
                    if (commandToken == Tokens.WHERE) {
                        processingSample("age", ColumnType.LONG);
                    } else {
                        addValue(age, ColumnType.LONG);
                    }
                    columnTokens.add(Tokens.AGE);
                }
            } else if (take("cost")) {
                skipwhitespace();
                if (take("'")) {
                    if (commandToken == Tokens.WHERE) {
                        processingSample("cost", ColumnType.DOUBLE);
                    } else {
                        addValue(cost, ColumnType.DOUBLE);
                    }
                    columnTokens.add(Tokens.COST);
                }
            } else if (take("active")) {
                skipwhitespace();
                if (take("'")) {
                    if (commandToken == Tokens.WHERE) {
                        processingSample("active", ColumnType.BOOLEAN);
                    } else {
                        addValue(active, ColumnType.BOOLEAN);
                    }
                    columnTokens.add(Tokens.ACTIVE);
                }
            }
        } else if (take("where")) {
            commandToken = Tokens.WHERE;
        } else if (take("and")) {
            predicate.append("and");
        } else if (take("or")) {
            predicate.append("or");
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }


    private void processingSample(String col, ColumnType type) {
        var sb = new StringBuilder();
        StringBuilder comparisonOp = new StringBuilder();
        skipUntilComparisonOp();
        char ch = take();
        while (!Character.isWhitespace(ch)) {
            comparisonOp.append(ch);
            ch = take();
        }
        addValue(sb, type);
        if (invalidOperations.containsKey(type) && invalidOperations.get(type).contains(comparisonOp + "")) {
            if (type.contains(col)) {
                throw new IllegalArgumentException("'" + col + "' does not support this operation");
            }
        }
        sampleTokens.put(col, new String[]{sb + "", comparisonOp + ""});
    }

    private void addValue(StringBuilder sb, ColumnType type) {
        skip();
        char ch = take();
        while (ch != ',' && !Character.isWhitespace(ch) && ch != 0 && ch != '\'') {
            sb.append(ch);
            ch = take();
        }
        if (!sb.toString().contains("null")) {
            switch (type) {
                case STRING -> {
                    String s = sb + "";
                    for (int i = 0; i < sb.length(); i++) {
                        if (Character.isDigit(s.charAt(i))) {
                            throw new IllegalArgumentException("Argument should be String");
                        }
                    }
                }
                case BOOLEAN -> {
                    if (!sb.toString().equals("true") && !sb.toString().equals("false")) {
                        throw new IllegalArgumentException("Argument should be Boolean");
                    }
                }
                case DOUBLE -> {
                    try {
                        double parsed = Double.parseDouble(sb + "");
                        if (parsed < 0) {
                            throw new IllegalArgumentException("Double argument should be 0 or positive");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Argument should be Double");
                    }
                }
                case LONG -> {
                    try {
                        long parsed = Long.parseLong(sb + "");
                        if (parsed < 0) {
                            throw new IllegalArgumentException("Long argument should be 0 or positive");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Argument should be Long");
                    }
                }
            }
        }
    }

    private void insertData(Map<String, Object> r) {
        if (id.toString().contains("null") &&
                age.toString().contains("null") &&
                cost.toString().contains("null") &&
                lastName.toString().contains("null") &&
                active.toString().contains("null")) {
            throw new IllegalArgumentException("All values cannot be empty");
        }
        if (columnTokens.contains(Tokens.ID) && !id.isEmpty()) {
            if (!id.toString().contains("null")) {
                r.put("id", Long.parseLong(id + ""));
            } else {
                r.remove("id");
            }
        }
        if (columnTokens.contains(Tokens.LAST_NAME) && !lastName.isEmpty()) {
            if (!lastName.toString().contains("null")) {
                r.put("lastName", lastName);
            } else {
                r.remove("lastName");
            }
        }
        if (columnTokens.contains(Tokens.AGE) && !age.isEmpty()) {
            if (!age.toString().contains("null")) {
                r.put("age", Long.parseLong(age + ""));
            } else {
                r.remove("age");
            }
        }
        if (columnTokens.contains(Tokens.COST) && !cost.isEmpty()) {
            if (!cost.toString().contains("null")) {
                r.put("cost", Double.parseDouble(cost + ""));
            } else {
                r.remove("cost");
            }
        }
        if (columnTokens.contains(Tokens.ACTIVE) && !active.isEmpty()) {
            if (!active.toString().contains("null")) {
                r.put("active", Boolean.parseBoolean(active + ""));
            } else {
                r.remove("active");
            }
        }
    }

    private boolean like(String s, String pattern) {
        s = s.toLowerCase();
        pattern = pattern.toLowerCase();
        if (!pattern.contains("%")) {
            return s.equals(pattern);
        }
        int patternLen = pattern.length();
        int i = pattern.indexOf("%");
        int j = pattern.lastIndexOf("%");
        if (i == 0 && j == patternLen - 1) {
            return s.contains(pattern.substring(1, patternLen - 1));
        } else if (i == 0) {
            return s.endsWith(pattern.substring(1));
        } else if (j == patternLen - 1) {
            return s.startsWith(pattern.substring(0, patternLen - 1));
        } else {
            int k = pattern.indexOf("%", i + 1);
            boolean isStartsWith = s.startsWith(pattern.substring(0, i));
            boolean isEndsWith = s.endsWith(pattern.substring(j + 1));
            if (k == -1) {
                return isStartsWith && isEndsWith;
            } else {
                return isStartsWith && s.contains(pattern.substring(i + 1, k)) && isEndsWith;
            }
        }
    }

    private boolean isLike(Map<String, Object> r, String col, String pattern) {
        return like(r.get(col) + "", pattern);
    }

    private static boolean isEquals(Map<String, Object> r, String col, String pattern) {
        return r.get(col).toString().equals(pattern);
    }
}
