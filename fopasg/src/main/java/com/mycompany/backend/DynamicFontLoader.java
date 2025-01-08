package com.mycompany.backend;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;

public class DynamicFontLoader {

    public static PDFont getPDFont(String fontFamily, boolean bold, boolean italic) throws IOException {
    try {
        // Try to get the requested font first
        File fontFile = findFontFile(fontFamily, bold, italic);
        if (fontFile != null) {
            return PDType0Font.load(new PDDocument(), fontFile);
        }
    } catch (Exception e) {
        // Log the error but continue to fallback font
        System.err.println("Failed to load font " + fontFamily + ": " + e.getMessage());
    }
    
    // Fallback to PDFBox built-in fonts which support basic characters
    if (bold && italic) {
        return PDType1Font.HELVETICA_BOLD_OBLIQUE;
    } else if (bold) {
        return PDType1Font.HELVETICA_BOLD;
    } else if (italic) {
        return PDType1Font.HELVETICA_OBLIQUE;
    } else {
        return PDType1Font.HELVETICA;
    }
}

    private static File findFontFile(String fontFamily, boolean bold, boolean italic) {
        String normalizedFontName = normalizeFontName(fontFamily);
        String fontDirectory = getSystemFontDirectory();
        File fontDir = new File(fontDirectory);
        
        if (fontDir.exists() && fontDir.isDirectory()) {
            // First try exact match
            File exactMatch = findExactFontMatch(fontDir, normalizedFontName, bold, italic);
            if (exactMatch != null) {
                return exactMatch;
            }
            
            // Then try similar match
            return findSimilarFontMatch(fontDir, normalizedFontName, bold, italic);
        }
        
        return null;
    }

    private static File findExactFontMatch(File fontDir, String fontName, boolean bold, boolean italic) {
        File[] files = fontDir.listFiles();
        if (files == null) return null;

        for (File fontFile : files) {
            String fileName = fontFile.getName().toLowerCase();
            if (!fileName.endsWith(".ttf") && !fileName.endsWith(".otf")) {
                continue;
            }

            if (fileName.contains(fontName)) {
                if ((!bold || fileName.contains("bold")) &&
                    (!italic || fileName.contains("italic"))) {
                    return fontFile;
                }
            }
        }
        return null;
    }

    private static File findSimilarFontMatch(File fontDir, String fontName, boolean bold, boolean italic) {
        File[] files = fontDir.listFiles();
        if (files == null) return null;

        File bestMatch = null;
        int highestSimilarity = 0;

        for (File fontFile : files) {
            String fileName = fontFile.getName().toLowerCase();
            if (!fileName.endsWith(".ttf") && !fileName.endsWith(".otf")) {
                continue;
            }

            int similarity = calculateSimilarity(fileName, fontName);
            if (similarity > highestSimilarity) {
                boolean hasCorrectStyle = (!bold || fileName.contains("bold")) &&
                                        (!italic || fileName.contains("italic"));
                if (hasCorrectStyle) {
                    highestSimilarity = similarity;
                    bestMatch = fontFile;
                }
            }
        }

        return bestMatch;
    }

    private static String normalizeFontName(String fontName) {
        return fontName.toLowerCase()
                      .replaceAll("[^a-z0-9]", "")
                      .replaceAll("regular", "")
                      .replaceAll("normal", "");
    }

    private static int calculateSimilarity(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        
        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }
        
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                  Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        
        return 100 - (dp[str1.length()][str2.length()] * 100 / 
                     Math.max(str1.length(), str2.length()));
    }

    private static String getSystemFontDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            return "C:\\Windows\\Fonts";
        } else if (os.contains("mac")) {
            return "/Library/Fonts";
        } else if (os.contains("nix") || os.contains("nux")) {
            return "/usr/share/fonts";
        } else {
            return "./fonts";
        }
    }

    private static PDType0Font loadDefaultFont() throws IOException {
        File helveticaFontFile = findFontFile("Helvetica", false, false);
        if (helveticaFontFile != null) {
            return PDType0Font.load(new PDDocument(), helveticaFontFile);
        }
        
        // If Helvetica not found, try Arial as a fallback
        File arialFontFile = findFontFile("Arial", false, false);
        if (arialFontFile != null) {
            return PDType0Font.load(new PDDocument(), arialFontFile);
        }
        
        // If neither found, throw an exception
        throw new IOException("Could not load default font");
    }
}