package com.google.android.exoplayer.text.webvtt;

import com.google.android.exoplayer.ParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebvttParserUtil {
    private static final Pattern COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");
    private static final Pattern CUE_HEADER = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
    private static final Pattern HEADER = Pattern.compile("^﻿?WEBVTT(( |\t).*)?$");

    private WebvttParserUtil() {
    }

    public static void validateWebvttHeaderLine(BufferedReader input) throws IOException {
        String line = input.readLine();
        if (line == null || !HEADER.matcher(line).matches()) {
            throw new ParserException("Expected WEBVTT. Got " + line);
        }
    }

    public static Matcher findNextCueHeader(BufferedReader input) throws IOException {
        while (true) {
            String line = input.readLine();
            if (line == null) {
                return null;
            }
            if (COMMENT.matcher(line).matches()) {
                while (true) {
                    line = input.readLine();
                    if (line == null || line.isEmpty()) {
                        break;
                    }
                }
            } else {
                Matcher cueHeaderMatcher = CUE_HEADER.matcher(line);
                if (cueHeaderMatcher.matches()) {
                    return cueHeaderMatcher;
                }
            }
        }
    }

    public static long parseTimestampUs(String timestamp) throws NumberFormatException {
        long value = 0;
        String[] parts = timestamp.split("\\.", 2);
        for (String parseLong : parts[0].split(":")) {
            value = (60 * value) + Long.parseLong(parseLong);
        }
        return ((value * 1000) + Long.parseLong(parts[1])) * 1000;
    }
}
