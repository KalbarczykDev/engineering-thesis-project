package dev.kalbarczyk.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s");

    private SlugUtil() {
    }

    public static String toSlug(final String input) {
        var noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        var normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        var slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
