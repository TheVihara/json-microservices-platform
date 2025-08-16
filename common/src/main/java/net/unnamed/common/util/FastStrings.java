package net.unnamed.common.util;

import java.util.ArrayList;
import java.util.List;

public final class FastStrings {
    private FastStrings() {
        throw new UnsupportedOperationException();
    }

    private final static ThreadLocal<StringBuilder> CACHE = ThreadLocal.withInitial(StringBuilder::new);

    public static StringBuilder getEmptyBuilder() {
        StringBuilder sb = CACHE.get();
        sb.setLength(0);

        return sb;
    }

    public static ParsedBracketString parseBracketString(String input) {
        if (input == null) throw new IllegalArgumentException("Input cannot be null");

        int open = input.indexOf('[');
        int close = input.indexOf(']');

        if (open != 0 || close <= open + 1 || close >= input.length()) {
            throw new IllegalArgumentException("Invalid format: " + input);
        }

        String key = input.substring(open + 1, close);
        List<String> args = fastSplit(input, close + 1);

        return new ParsedBracketString(key, args);
    }

    private static List<String> fastSplit(String input, int start) {
        List<String> result = new ArrayList<>();
        int len = input.length();
        int i = start;

        while (i < len) {
            while (i < len && input.charAt(i) == ' ') i++;

            int startWord = i;
            while (i < len && input.charAt(i) != ' ') i++;

            if (startWord < i) {
                result.add(input.substring(startWord, i));
            }
        }

        return result;
    }
}