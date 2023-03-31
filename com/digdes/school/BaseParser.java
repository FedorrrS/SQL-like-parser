/*
 * @author Selischev Fedor, ITMO CT (yanerala@gmail.com)
 */

package com.digdes.school;

public class BaseParser {
    private final char END = 0;
    private final CharSource source;
    private char ch;

    public BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected boolean testIgnoreCase(char expected) {
        return Character.toLowerCase(ch) == Character.toLowerCase(expected);
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(String input) {
        source.saveChar();
        for (char c : input.toCharArray()) {
            if (!take(c)) {
                ch = source.returnChar();
                return false;
            }
        }
        return true;
    }

    protected boolean take(char expected) {
        if (testIgnoreCase(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void skipwhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    protected void skipComma() {
        while (ch == ',') {
            take();
        }
    }

    protected void skip() {
        while (Character.isWhitespace(ch) || ch == '=' || ch == '\'' || ch == ',') {
            take();
        }
    }

    protected void skipUntilValidToken() {
        skipwhitespace();
        skipComma();
        skipwhitespace();
        skipComma();
    }

    protected void skipUntilComparisonOp() {
        while (Character.isWhitespace(ch) || ch == '\'' || ch == ',') {
            take();
        }
    }

    protected boolean eof() {
        return take(String.valueOf(END));
    }
}