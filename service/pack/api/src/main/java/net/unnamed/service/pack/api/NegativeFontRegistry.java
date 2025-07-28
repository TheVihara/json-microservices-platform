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

    // --- Updated Ranges based on provided character mappings ---

    // Note: generateRange helper is adjusted or used differently now.
    // Define the specific advances as per your description
    public static final int[] NEGATIVE_NOSPLIT_HEIGHTS = {-1, -2, -4, -8, -16, -32, -64, -128, -256, -512, -1024};
    // Assuming the negative split range also needs adjustment, or keep original if it uses \uE100-\uE1FF
    // For now, let's assume the original NEGATIVE_HEIGHTS logic for split (-3 to -258) is separate
    // and uses its own character mapping (e.g., starting at \uE100). We'll keep NEGATIVE_HEIGHTS
    // but note it might need character mapping logic separate from F801-F80F if F801-F80F is *only* nosplit.
    // Let's redefine NEGATIVE_HEIGHTS for split based on your initial description's pattern, excluding nosplit values.
    // If -3 to -258 includes others, adjust accordingly. Assuming -3,-5,...-257 (odd negatives excluding nosplit powers)
    // Or simpler, if it's just another sequential block, we might need clarification.
    // For this modification, let's assume NEGATIVE_PADDING_SPLIT covers -3 to -258 sequentially.
    // However, your description maps F801-F808 and F809-F80F specifically.
    // This implies NEGATIVE_PADDING_NOSPLIT might be the *only* one using F801-F80F.
    // Therefore, NEGATIVE_PADDING_SPLIT needs its own character set, likely starting elsewhere (like \uE100 as before,
    // unless you specify otherwise). Let's keep the original NEGATIVE_HEIGHTS definition for split for now,
    // assuming it maps to \uE100 range, and only NOSPLIT maps to \uF801.
    // If NEGATIVE_PADDING_SPLIT should also use part of F8 range, please clarify.
    // For now, sticking to original split range logic for the split font, mapping to \uE100.
    public static final int[] NEGATIVE_HEIGHTS = generateRange(-3, -258); // -3 to -258 for negative split (maps to \uE100+)
    // Positive heights: Assuming F821-F828 (1-8) and F829-F82F (16,32,...1024)
    // Need to combine these into one array for the positive font(s). Let's assume they map to one font for now.
    // If split/non-split matters for positives, adjust logic in determineFontName and registration.
    public static final int[] POSITIVE_HEIGHTS = {1, 2, 3, 4, 5, 6, 7, 8, 16, 32, 64, 128, 256, 512, 1024};

    // --- Font type definitions (names stay the same) ---
    public static final String POSITIVE_PADDING = "padding"; // Uses F821-F82F range
    public static final String NEGATIVE_PADDING_SPLIT = "negative_padding_split"; // Uses E100 range (assuming)
    public static final String NEGATIVE_PADDING_NOSPLIT = "negative_padding_nosplit"; // Uses F801-F80F range
    public static final String PADDING_SPLIT = "padding_split"; // Could potentially use F821-F82F or E300, clarify if different from POSITIVE_PADDING

    // --- Image file mappings ---
    public static final String SPLIT_IMAGE = "1.png";    // 256x256 for split variants
    public static final String NOSPLIT_IMAGE = "0.png";  // 1x1 for nosplit variants

    // --- Generate a range of integers (kept for NEGATIVE_HEIGHTS if needed elsewhere) ---
    private static int[] generateRange(int start, int end) {
        // Remove the special case for -1 to -1024 as we define it explicitly now
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
        @Getter private final String providerType; // Always "bitmap"
        @Getter private final String imageFile;

        public FontData(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
            this.fontName = fontName;
            this.namespace = namespace;
            this.isSplit = isSplit;
            this.isNegative = isNegative;
            // Store a defensive copy
            this.advances = advances != null ? Arrays.copyOf(advances, advances.length) : new int[0];
            this.providerType = "bitmap";
            this.imageFile = imageFile;
        }

        public String getFullFontName() {
            return namespace + ":" + fontName;
        }
    }

    public NegativeFontRegistry() {
        // Example namespace - change as needed or make configurable
        initializeDefaultFonts("astopia");
    }

    /**
     * Calculate the most accurate padding string for the desired value.
     * Automatically chooses the correct font and decomposes into multiple characters if necessary.
     */
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

        // 1. Try direct match
        Character exact = charMap.get(targetValue);
        if (exact != null) {
            result.append(exact);
        } else {
            int absTarget = Math.abs(targetValue);
            boolean isNegative = targetValue < 0;

            // 2. & 3. Decomposition logic for best fit
            // Use the sorted advances from the font data for decomposition
            List<Integer> sortedAdvances = Arrays.stream(fontData.getAdvances())
                    .boxed()
                    .sorted((a, b) -> Integer.compare(Math.abs(b), Math.abs(a))) // Sort by absolute value descending
                    .toList();

            int remaining = absTarget;
            boolean decompositionSuccessful = true;

            while (remaining > 0) {
                boolean found = false;
                for (int adv : sortedAdvances) {
                    int absAdv = Math.abs(adv);
                    // For negative fonts, we look for negative advances. For positive, positive advances.
                    // The targetValue dictates the sign needed.
                    if (absAdv <= remaining) {
                        // Check if the character's sign matches the required direction
                        // (This assumes the font contains advances of the correct sign)
                        // If the font is for negative values, it should contain negative advances.
                        // If the font is for positive values, it should contain positive advances.
                        // The determineFontName logic should ensure this.
                        if ((isNegative && adv < 0) || (!isNegative && adv > 0)) {
                            Character ch = charMap.get(adv);
                            if (ch != null) {
                                result.append(ch);
                                remaining -= absAdv;
                                found = true;
                                break; // Restart the search from the largest again
                            }
                        }
                    }
                }
                if (!found) {
                    // Could not find a suitable character to represent the remaining value
                    log.warn("Could not fully decompose padding value {} using font {}", targetValue, fontName);
                    decompositionSuccessful = false;
                    // Optionally, append a placeholder or stop?
                    // For now, we'll stop and return what we have.
                    break;
                }
            }

            // Optional: Handle case where decomposition wasn't fully successful
            // if (!decompositionSuccessful) { ... }
        }

        result.append("</font>");
        return result.toString();
    }


    /**
     * Initialize all default font registrations matching the examples
     */
    public void initializeDefaultFonts(String namespace) {
        // Positive heights (1-8, 16-1024) - uses characters starting at \uF821
        // Assuming this is the 'split' version for positive, or maybe non-split? Clarification needed.
        // Let's assume it's the main positive font, potentially used for split.
        registerFont(POSITIVE_PADDING, namespace, true, false, POSITIVE_HEIGHTS, SPLIT_IMAGE); // Assuming split uses SPLIT_IMAGE

        // Negative heights (-3 to -258) - Split - uses characters starting at \uE100 (original logic)
        // Note: Your description doesn't map these specific F8 chars to this range.
        // If NEGATIVE_PADDING_SPLIT should use F809-F80F, logic needs change.
        // Keeping original \uE100 mapping for now based on initial code structure.
        registerFont(NEGATIVE_PADDING_SPLIT, namespace, true, true, NEGATIVE_HEIGHTS, SPLIT_IMAGE);

        // Negative nosplit heights (-1 to -1024) - uses characters \uF801 to \uF80F
        registerFont(NEGATIVE_PADDING_NOSPLIT, namespace, false, true, NEGATIVE_NOSPLIT_HEIGHTS, NOSPLIT_IMAGE);

        // Positive split padding - If distinct from POSITIVE_PADDING, define its range and mapping.
        // If it's the same set of characters/values, maybe not needed, or needs clarification.
        // Assuming for now it's the same as POSITIVE_PADDING or uses E300.
        // Let's register it with E300 base char and same values for consistency with original code,
        // though it might overlap/need clarification.
        registerFont(PADDING_SPLIT, namespace, true, false, POSITIVE_HEIGHTS, SPLIT_IMAGE); // Using E300 base char mapping
    }

    /**
     * Register a font with its associated character mappings
     */
    public void registerFont(String fontName, String namespace, boolean isSplit, boolean isNegative, int[] advances, String imageFile) {
        FontData fontData = new FontData(fontName, namespace, isSplit, isNegative, advances, imageFile);
        fonts.put(fontName, fontData);

        HashMap<Integer, Character> charMap = new HashMap<>();
        char baseChar = getBaseCharacter(fontName);

        // --- Modified logic for character assignment based on specific ranges ---
        if (fontName.equals(NEGATIVE_PADDING_NOSPLIT)) {
            // Map advances [-1, -2, ..., -1024] to chars [\uF801, \uF802, ..., \uF80F]
            // Assuming the advances array is already ordered correctly for this mapping.
            if (advances.length > 15) { // Safety check
                log.warn("Too many advances for NEGATIVE_PADDING_NOSPLIT, truncating.");
            }
            // Map index directly to offset from baseChar (\uF801)
            for (int i = 0; i < Math.min(advances.length, 15); i++) { // Max 15 chars F801-F80F
                char character = (char) (baseChar + i); // \uF801 + 0 = \uF801, ... \uF801 + 14 = \uF80F
                charMap.put(advances[i], character);
                // Debugging log (optional)
                // log.debug("Mapped {} -> \\u{} ({})", advances[i], Integer.toHexString(character).toUpperCase(), character);
            }
        } else if (fontName.equals(POSITIVE_PADDING) || fontName.equals(PADDING_SPLIT)) {
            // Map advances [1,2,...8,16,32,...1024] to chars [\uF821,\uF822,...\uF828,\uF829,...\uF82F]
            // Assuming the advances array is ordered correctly.
            if (advances.length > 15) { // Safety check
                log.warn("Too many advances for {}, truncating.", fontName);
            }
            // Map index directly to offset from baseChar (\uF821)
            for (int i = 0; i < Math.min(advances.length, 15); i++) { // Max 15 chars F821-F82F
                char character = (char) (baseChar + i); // \uF821 + 0 = \uF821, ... \uF821 + 14 = \uF82F
                charMap.put(advances[i], character);
                // Debugging log (optional)
                // log.debug("Mapped {} -> \\u{} ({})", advances[i], Integer.toHexString(character).toUpperCase(), character);
            }
        } else {
            // Default logic for other fonts (e.g., NEGATIVE_PADDING_SPLIT using \uE100)
            // This assumes advances are sequential and map 1:1 starting from baseChar
            for (int i = 0; i < advances.length; i++) {
                char character = (char) (baseChar + i);
                charMap.put(advances[i], character);
            }
        }
        // --- End of modified logic ---

        fontCharacters.put(fontName, charMap);
        log.info("Registered bitmap font '{}' with {} characters using {} starting at \\u{}",
                fontName, advances.length, imageFile, Integer.toHexString(baseChar).toUpperCase());
    }

    // --- Other methods remain mostly the same, but might need minor adjustments based on new logic ---

    public Character getPaddingCharacter(int value, boolean split) {
        String fontName = determineFontName(value, split);
        HashMap<Integer, Character> charMap = fontCharacters.get(fontName);
        if (charMap == null) {
            return null;
        }
        return charMap.get(value);
    }

    public Character getClosestPaddingCharacter(int value, boolean split) {
        // This logic might be less relevant now with direct mapping, but kept for compatibility
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
        // Uses getClosestPaddingCharacter, so inherits its behavior (might be less precise now)
        String fontName = determineFontName(value, split);
        FontData fontData = fonts.get(fontName);
        Character paddingChar = getClosestPaddingCharacter(value, split);
        if (fontData == null || paddingChar == null) {
            return "";
        }
        return "<font:" + fontData.getFullFontName() + ">" + paddingChar + "</font>";
    }

    public String createComplexPadding(int targetValue, boolean split) {
        // This essentially duplicates calculatePadding logic now.
        // Consider deprecating or simplifying to call calculatePadding.
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

    /**
     * Determine which font to use based on value and split setting
     */
    public String determineFontName(int value, boolean split) {
        if (value < 0) {
            return split ? NEGATIVE_PADDING_SPLIT : NEGATIVE_PADDING_NOSPLIT;
        } else {
            // Assuming positive values use POSITIVE_PADDING for non-split and PADDING_SPLIT for split
            // Or maybe both use the same font? Clarification might be needed.
            // Keeping original logic for now.
            return split ? PADDING_SPLIT : POSITIVE_PADDING;
        }
    }

    /**
     * Get base Unicode character for each font type
     * Updated to match the specified character ranges.
     */
    public char getBaseCharacter(String fontName) {
        return switch (fontName) {
            case POSITIVE_PADDING -> (char) 0xE000;           // 0xE000-0xE0FF
            case NEGATIVE_PADDING_SPLIT -> (char) 0xE100;     // 0xE100-0xE1FF
            case NEGATIVE_PADDING_NOSPLIT -> (char) 0xF801;   // 0xF801-0xF80B // <-- CHANGED LINE
            case PADDING_SPLIT -> (char) 0xE300;              // 0xE300-0xE3FF
            default -> (char) 0xE000;
        };
    }
}