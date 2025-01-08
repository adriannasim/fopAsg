package com.mycompany.backend;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class StyledText {

    private final String text;
    private final String fontFamily;
    private final float fontSize;
    private final String backgroundColor; 
    private final String foregroundColor;
    private final boolean italic;
    private final boolean bold;
    private final boolean strikethrough;
    private final boolean underline;
    private final boolean bulletedList;
    private final boolean numberedList;

    // Constructor
    public StyledText(String text, String fontFamily, float fontSize, String foregroundColor,
            String backgroundColor, boolean italic, boolean bold,
            boolean strikethrough, boolean underline, boolean bulletedList, boolean numberedList) {
        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.italic = italic;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underline = underline;
        this.bulletedList = bulletedList;
        this.numberedList = numberedList;
    }

    // Getters for each property
    public String getText() {
        return text;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public float getFontSize() {
        return fontSize;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isBulletedList() {
        return bulletedList;
    }

    public boolean isNumberedList() {
        return numberedList;
    }

    @SuppressWarnings("exports")
    public PDFont getPDFont() throws IOException {
        try {
            if (bold && italic) {
                return PDType1Font.HELVETICA_BOLD_OBLIQUE;
            } else if (bold) {
                return PDType1Font.HELVETICA_BOLD;
            } else if (italic) {
                return PDType1Font.HELVETICA_OBLIQUE;
            } else {
                return PDType1Font.HELVETICA;
            }
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
            // Fall back to Helvetica as the default font in case of any errors
            return PDType1Font.HELVETICA;
        } 
    }

    public float[] parseColor(String color) {
    
        // Handle 'black' color
        if (color.equalsIgnoreCase("black")) {
            return new float[] { 0f, 0f, 0f };
        }
    
        // Remove '#' if present in hex format
        if (color.startsWith("#")) {
            color = color.substring(1); // Remove '#' character
        }
    
        // Handle RGB format (e.g., "255,0,0")
        if (color.contains(",")) {
            String[] rgb = color.split(",");
            if (rgb.length != 3) {
                throw new IllegalArgumentException("Invalid RGB color format: " + color);
            }
    
            return new float[] {
                clamp(Integer.parseInt(rgb[0].trim())) / 255f,
                clamp(Integer.parseInt(rgb[1].trim())) / 255f,
                clamp(Integer.parseInt(rgb[2].trim())) / 255f
            };
        } else {
            // Handle Hexadecimal format (e.g., "FF0000" or "FF0000FF" for RGBA)
            if (color.length() == 3) {
                // Shorthand hex format (e.g., "F00" => "FF0000")
                color = color.charAt(0) + "" + color.charAt(0) +
                        color.charAt(1) + "" + color.charAt(1) +
                        color.charAt(2) + "" + color.charAt(2);
            }
    
            // Ensure the color string is of valid length (either 6 or 8 characters for RGB or RGBA)
            if (color.length() != 6 && color.length() != 8) {
                throw new IllegalArgumentException("Invalid hex color format: " + color);
            }
    
            // If it's an RGBA color, extract the first 6 characters as RGB
            String rgbPart = color.substring(0, 6);
            float[] rgbValues = new float[] {
                clamp(Integer.parseInt(rgbPart.substring(0, 2), 16)) / 255f,
                clamp(Integer.parseInt(rgbPart.substring(2, 4), 16)) / 255f,
                clamp(Integer.parseInt(rgbPart.substring(4, 6), 16)) / 255f
            };
    
            return rgbValues;
        }
    }
    
    // Helper method to clamp values within 0-255 range
    private int clamp(int value) {
        return Math.max(0, Math.min(value, 255));
    }
     
}
