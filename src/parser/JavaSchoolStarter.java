/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package src.parser;

import java.util.*;

public class JavaSchoolStarter {

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws RuntimeException {
        return new RequestParser(new StringSource(request)).parse();
    }
}
