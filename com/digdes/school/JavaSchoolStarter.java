/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws RuntimeException {
        return new RequestParser(new StringSource(request)).parse();
    }
}
