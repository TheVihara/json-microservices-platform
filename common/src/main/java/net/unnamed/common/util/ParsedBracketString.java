package net.unnamed.common.util;

import java.util.List;
import java.util.Objects;

public final class ParsedBracketString {
    private final String key;
    private final List<String> args;

    public ParsedBracketString(String key, List<String> args) {
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "ParsedBracketString{key='" + key + "', args=" + args + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParsedBracketString)) return false;
        ParsedBracketString that = (ParsedBracketString) o;
        return Objects.equals(key, that.key) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, args);
    }
}
