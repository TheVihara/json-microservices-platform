package net.unnamed.common.util;

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

}