package net.unnamed.service.pack.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
public class NegativeFontRegistry {
    private final HashMap<String, FontData> fonts = new HashMap<>();
    private final HashMap<String, HashMap<Integer, Character>> fontCharacters = new HashMap<>();

    // Static advance values matching the example patterns
    public static final int[] POSITIVE_HEIGHTS = generateRange(1, 255);     // 1-255 for positive heights
    public static final int[] NEGATIVE_HEIGHTS = generateRange(-3, -258);   // -3 to -258 for negative heights
    public static final int[] NEGATIVE_NOSPLIT_HEIGHTS = generateRange(-1, -1024); // -1 to -1024 for negative nosplit

    // Font type definitions
    public static final String POSITIVE_PADDING = "padding"; // Positive heights 1-255
    public static final String NEGATIVE_PADDING_SPLIT = "negative_padding_split"; // Negative heights -3 to -258
    public static final String NEGATIVE_PADDING_NOSPLIT = "negative_padding_nosplit"; // Negative heights -1 to -1024
    public static final String PADDING_SPLIT = "padding_split"; // Positive heights with split

    // Image file mappings
    public static final String SPLIT_IMAGE = "1.png";    // 256x256 for split variants
    public static final String NOSPLIT_IMAGE = "0.png";  // 1x1 for nosplit variants

    // Generate a range of integers
    private static int[] generateRange(int start, int end) {
        if (start == -1 && end == -1024) {
            // Special case for negative nosplit: powers of 2
            return new int[]{-1, -2, -4, -8, -16, -32, -64, -128, -256, -512, -1024};
        }

        int length = Math.abs(end - start) + 1;
        int[] range = new int[length];
        int step = start < end ? 1 : -1;

        for (int i = 0; i < length; i++) {
            range[i] = start + (i * step);
        }
        return range;
    }

    public static class FontData {
        @Getter
        private final String fontName;
        @Getter
        private final String namespace;
        @Getter
        private final boolean isSplit;
        @Getter
        private final boolean isNegative;
        @Getter
        private final int[] advances;
        @Getter
        private final String providerType; // Always "bitmap"
        @Getter
        private final String imageFile;

        public FontData(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
            this.fontName = fontName;
            this.namespace = namespace;
            this.isSplit = isSplit;
            this.isNegative = isNegative;
            this.advances = advances.clone();
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

    /**
     * Initialize all default font registrations matching the examples
     */
    public void initializeDefaultFonts(String namespace) {
        // Positive heights (1-255) - uses 1.png
        registerFont(POSITIVE_PADDING, namespace, false, false, POSITIVE_HEIGHTS, SPLIT_IMAGE);

        // Negative heights (-3 to -258) - uses 1.png
        registerFont(NEGATIVE_PADDING_SPLIT, namespace, true, true, NEGATIVE_HEIGHTS, SPLIT_IMAGE);

        // Negative nosplit heights (-1 to -1024) - uses 0.png
        registerFont(NEGATIVE_PADDING_NOSPLIT, namespace, false, true, NEGATIVE_NOSPLIT_HEIGHTS, NOSPLIT_IMAGE);

        // Positive split padding - uses 1.png
        registerFont(PADDING_SPLIT, namespace, true, false, POSITIVE_HEIGHTS, SPLIT_IMAGE);
    }

    /**
     * Register a font with its associated character mappings
     */
    public void registerFont(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
        FontData fontData = new FontData(fontName, namespace, isSplit, isNegative, advances, imageFile);
        fonts.put(fontName, fontData);

        HashMap<Integer, Character> charMap = new HashMap<>();

        // Generate Unicode characters for each advance value
        char baseChar = getBaseCharacter(fontName);
        for (int i = 0; i < advances.length; i++) {
            char character = (char) (baseChar + i);
            charMap.put(advances[i], character);
        }

        fontCharacters.put(fontName, charMap);
        log.info("Registered bitmap font '{}' with {} characters using {}", fontName, advances.length, imageFile);
    }

    /**
     * Get the Unicode character for a specific padding value
     */
    public Character getPaddingCharacter(int value, boolean split) {
        String fontName = determineFontName(value, split);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);

        if (charMap == null) {
            return null;
        }

        return charMap.get(value);
    }

    /**
     * Get the closest matching character for a padding value
     */
    public Character getClosestPaddingCharacter(int value, boolean split) {
        String fontName = determineFontName(value, split);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);

        if (charMap == null) {
            return null;
        }

        // Try exact match first
        Character exactMatch = charMap.get(value);
        if (exactMatch != null) {
            return exactMatch;
        }

        // Find closest match
        int closest = charMap.keySet().stream()
                .min((a, b) -> Integer.compare(Math.abs(a - value), Math.abs(b - value)))
                .orElse(0);

        return charMap.get(closest);
    }

    /**
     * Create a complete padding string with font tags
     */
    public String createPaddingString(int value, boolean split) {
        String fontName = determineFontName(value, split);
        FontData fontData = fonts.get(fontName);
        Character paddingChar = getClosestPaddingCharacter(value, split);

        if (fontData == null || paddingChar == null) {
            return "";
        }

        return "<font:" + fontData.getFullFontName() + ">" + paddingChar + "</font>";
    }

    /**
     * Create complex padding by combining multiple characters for exact values
     */
    public String createComplexPadding(int targetValue, boolean split) {
        String fontName = determineFontName(targetValue, split);
        FontData fontData = fonts.get(fontName);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);

        if (fontData == null || charMap == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        result.append("<font:").append(fontData.getFullFontName()).append(">");

        // Check if we have exact match
        Character exactChar = charMap.get(targetValue);
        if (exactChar != null) {
            result.append(exactChar);
        } else {
            // For negative nosplit, decompose into powers of 2
            if (fontName.equals(NEGATIVE_PADDING_NOSPLIT)) {
                Set<Integer> availableValues = charMap.keySet();
                int[] sortedValues = availableValues.stream()
                        .sorted((a, b) -> Integer.compare(Math.abs(b), Math.abs(a)))
                        .mapToInt(Integer::intValue)
                        .toArray();

                int remaining = Math.abs(targetValue);
                for (int value : sortedValues) {
                    int absValue = Math.abs(value);
                    while (remaining >= absValue) {
                        result.append(charMap.get(value));
                        remaining -= absValue;
                    }
                }
            } else {
                // Use closest match for other fonts
                Character closest = getClosestPaddingCharacter(targetValue, split);
                if (closest != null) {
                    result.append(closest);
                }
            }
        }

        result.append("</font>");
        return result.toString();
    }

    /**
     * Get all registered font names
     */
    public Set<String> getRegisteredFontNames() {
        return fonts.keySet();
    }

    /**
     * Get advances for a specific font
     */
    public int[] getAdvancesForFont(String fontName) {
        FontData fontData = fonts.get(fontName);
        return fontData != null ? fontData.getAdvances() : new int[0];
    }

    /**
     * Get all registered fonts
     */
    public Map<String, FontData> getRegisteredFonts() {
        return new HashMap<>(fonts);
    }

    /**
     * Get all characters for a specific font
     */
    public Map<Integer, Character> getFontCharacters(String fontName) {
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);
        return charMap != null ? new HashMap<>(charMap) : new HashMap<>();
    }

    /**
     * Check if a font is registered
     */
    public boolean isFontRegistered(String fontName) {
        return fonts.containsKey(fontName);
    }

    /**
     * Determine which font to use based on value and split setting
     */
    private String determineFontName(int value, boolean split) {
        if (value < 0) {
            return split ? NEGATIVE_PADDING_SPLIT : NEGATIVE_PADDING_NOSPLIT;
        } else {
            return split ? PADDING_SPLIT : POSITIVE_PADDING;
        }
    }

    /**
     * Get base Unicode character for each font type
     */
    public char getBaseCharacter(String fontName) {
        return switch (fontName) {
            case POSITIVE_PADDING -> (char) 0xE000;           // 0xE000-0xE0FF
            case NEGATIVE_PADDING_SPLIT -> (char) 0xE100;     // 0xE100-0xE1FF
            case NEGATIVE_PADDING_NOSPLIT -> (char) 0xE200;   // 0xE200-0xE2FF
            case PADDING_SPLIT -> (char) 0xE300;              // 0xE300-0xE3FF
            default -> (char) 0xE000;
        };
    }
}