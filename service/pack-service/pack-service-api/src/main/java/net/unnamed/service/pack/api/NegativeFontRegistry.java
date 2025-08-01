package net.unnamed.service.pack.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Getter
public class NegativeFontRegistry {

    private final HashMap<String, FontData> fonts = new HashMap<>();
    private final HashMap<String, HashMap<Integer, Character>> fontCharacters = new HashMap<>();
    
    public static final int[] NEGATIVE_NOSPLIT_HEIGHTS = {-1, -2, -4, -8, -16, -32, -64, -128, -256, -512, -1024};
    public static final int[] NEGATIVE_HEIGHTS = generateRange(-3, -258);
    public static final int[] POSITIVE_HEIGHTS = {1, 2, 3, 4, 5, 6, 7, 8, 16, 32, 64, 128, 256, 512, 1024};
    
    public static final String POSITIVE_PADDING = "padding"; 
    public static final String NEGATIVE_PADDING_SPLIT = "negative_padding_split"; 
    public static final String NEGATIVE_PADDING_NOSPLIT = "negative_padding_nosplit"; 
    public static final String PADDING_SPLIT = "padding_split";
    
    public static final String SPLIT_IMAGE = "1.png";    
    public static final String NOSPLIT_IMAGE = "0.png";  
    
    private static int[] generateRange(int start, int end) {
        int length = Math.abs(end - start) + 1;
        int[] range = new int[length];
        int step = start < end ? 1 : -1;
        for (int i = 0; i < length; i++) {
            range[i] = start + (i * step);
        }
        return range;
    }

    public static class FontData {
        @Getter private final String fontName;
        @Getter private final String namespace;
        @Getter private final boolean isSplit;
        @Getter private final boolean isNegative;
        @Getter private final int[] advances;
        @Getter private final String providerType;
        @Getter private final String imageFile;

        public FontData(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
            this.fontName = fontName;
            this.namespace = namespace;
            this.isSplit = isSplit;
            this.isNegative = isNegative;
            this.advances = advances != null ? Arrays.copyOf(advances, advances.length) : new int[0];
            this.providerType = "bitmap";
            this.imageFile = imageFile;
        }

        public String getFullFontName() {
            return namespace + ":" + fontName;
        }
    }

    public NegativeFontRegistry() {
        initializeDefaultFonts("astopia");
    }

    public String calculatePadding(int targetValue, boolean split) {
        String fontName = determineFontName(targetValue, split);
        FontData fontData = fonts.get(fontName);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);

        if (fontData == null || charMap == null || charMap.isEmpty()) {
            log.warn("Font data or characters not found for padding: {}", fontName);
            return "";
        }

        StringBuilder result = new StringBuilder();
        result.append("<font:").append(fontData.getFullFontName()).append(">");

        Character exact = charMap.get(targetValue);
        if (exact != null) {
            result.append(exact);
        } else {
            int absTarget = Math.abs(targetValue);
            boolean isNegative = targetValue < 0;

            List<Integer> sortedAdvances = Arrays.stream(fontData.getAdvances())
                    .boxed()
                    .sorted((a, b) -> Integer.compare(Math.abs(b), Math.abs(a)))
                    .toList();

            int remaining = absTarget;
            boolean decompositionSuccessful = true;

            while (remaining > 0) {
                boolean found = false;
                for (int adv : sortedAdvances) {
                    int absAdv = Math.abs(adv);
                    if (absAdv <= remaining) {
                        if ((isNegative && adv < 0) || (!isNegative && adv > 0)) {
                            Character ch = charMap.get(adv);
                            if (ch != null) {
                                result.append(ch);
                                remaining -= absAdv;
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                    log.warn("Could not fully decompose padding value {} using font {}", targetValue, fontName);
                    decompositionSuccessful = false;
                    break;
                }
            }
        }

        result.append("</font>");
        return result.toString();
    }

    public void initializeDefaultFonts(String namespace) {
        registerFont(POSITIVE_PADDING, namespace, true, false, POSITIVE_HEIGHTS, SPLIT_IMAGE);
        registerFont(NEGATIVE_PADDING_SPLIT, namespace, true, true, NEGATIVE_HEIGHTS, SPLIT_IMAGE);
        registerFont(NEGATIVE_PADDING_NOSPLIT, namespace, false, true, NEGATIVE_NOSPLIT_HEIGHTS, NOSPLIT_IMAGE);
        registerFont(PADDING_SPLIT, namespace, true, false, POSITIVE_HEIGHTS, SPLIT_IMAGE);
    }

    public void registerFont(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
        FontData fontData = new FontData(fontName, namespace, isSplit, isNegative, advances, imageFile);
        fonts.put(fontName, fontData);

        HashMap<Integer, Character> charMap = new HashMap<>();
        char baseChar = getBaseCharacter(fontName);

        if (fontName.equals(NEGATIVE_PADDING_NOSPLIT)) {
            if (advances.length > 15) {
                log.warn("Too many advances for NEGATIVE_PADDING_NOSPLIT, truncating.");
            }
            for (int i = 0; i < Math.min(advances.length, 15); i++) {
                char character = (char) (baseChar + i);
                charMap.put(advances[i], character);
            }
        } else if (fontName.equals(POSITIVE_PADDING) || fontName.equals(PADDING_SPLIT)) {
            if (advances.length > 15) {
                log.warn("Too many advances for {}, truncating.", fontName);
            }
            for (int i = 0; i < Math.min(advances.length, 15); i++) {
                char character = (char) (baseChar + i);
                charMap.put(advances[i], character);
            }
        } else {
            for (int i = 0; i < advances.length; i++) {
                char character = (char) (baseChar + i);
                charMap.put(advances[i], character);
            }
        }

        fontCharacters.put(fontName, charMap);
/*        log.info("Registered bitmap font '{}' with {} characters using {} starting at \\u{}",
                fontName, advances.length, imageFile, Integer.toHexString(baseChar).toUpperCase());*/
    }

    public Character getPaddingCharacter(int value, boolean split) {
        String fontName = determineFontName(value, split);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);
        if (charMap == null) {
            return null;
        }
        return charMap.get(value);
    }

    public Character getClosestPaddingCharacter(int value, boolean split) {
        String fontName = determineFontName(value, split);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);
        if (charMap == null) {
            return null;
        }
        Character exactMatch = charMap.get(value);
        if (exactMatch != null) {
            return exactMatch;
        }

        return charMap.keySet().stream()
                .min(Comparator.comparingInt(a -> Math.abs(a - value)))
                .map(charMap::get)
                .orElse(null);
    }

    public String createPaddingString(int value, boolean split) {
        String fontName = determineFontName(value, split);
        FontData fontData = fonts.get(fontName);
        Character paddingChar = getClosestPaddingCharacter(value, split);
        if (fontData == null || paddingChar == null) {
            return "";
        }
        return "<font:" + fontData.getFullFontName() + ">" + paddingChar + "</font>";
    }

    public String createComplexPadding(int targetValue, boolean split) {
        return calculatePadding(targetValue, split);
    }

    public Set<String> getRegisteredFontNames() {
        return fonts.keySet();
    }

    public int[] getAdvancesForFont(String fontName) {
        FontData fontData = fonts.get(fontName);
        return fontData != null ? fontData.getAdvances() : new int[0];
    }

    public Map<String, FontData> getRegisteredFonts() {
        return new HashMap<>(fonts);
    }

    public Map<Integer, Character> getFontCharacters(String fontName) {
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);
        return charMap != null ? new HashMap<>(charMap) : new HashMap<>();
    }

    public boolean isFontRegistered(String fontName) {
        return fonts.containsKey(fontName);
    }

    public String determineFontName(int value, boolean split) {
        if (value < 0) {
            return split ? NEGATIVE_PADDING_SPLIT : NEGATIVE_PADDING_NOSPLIT;
        } else {
            return split ? PADDING_SPLIT : POSITIVE_PADDING;
        }
    }

    public char getBaseCharacter(String fontName) {
        return switch (fontName) {
            case POSITIVE_PADDING -> (char) 0xE000;
            case NEGATIVE_PADDING_SPLIT -> (char) 0xE100;
            case NEGATIVE_PADDING_NOSPLIT -> (char) 0xF801;
            case PADDING_SPLIT -> (char) 0xE300;
            default -> (char) 0xE000;
        };
    }
}