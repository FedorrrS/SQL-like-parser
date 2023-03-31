/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package com.digdes.school;

public class StringSource implements CharSource {
    private final String string;
    private int pos;
    private int savedPos;
    private char savedChar;

    public StringSource(String string) {
        this.string = string;
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public void saveChar() {
        savedPos = pos;
        savedChar = string.charAt(pos - 1);
    }

    @Override
    public char returnChar() {
        pos = savedPos;
        return savedChar;
    }
}
