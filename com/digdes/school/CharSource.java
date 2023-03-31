/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package com.digdes.school;

public interface CharSource {
    boolean hasNext();
    char next();
    void saveChar();
    char returnChar();
}
