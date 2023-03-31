/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package com.digdes.school;

public enum Tokens {
    /* Command tokens */
    INSERT,
    UPDATE,
    DELETE,
    SELECT,
    /* Comparison tokens */
    WHERE,
    OR,
    AND,
    /* Column tokens */
    ID,
    LAST_NAME,
    AGE,
    COST,
    ACTIVE,
    /* Parsing tokens */
    EOF
}
