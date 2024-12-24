package com.mycompany.frontend.helper;

public class TextSegment {
    private String text;            // Plain text
    private boolean bold;           // Bold property
    private boolean italic;         // Italic property
    private boolean underline;      // Underline property
    private boolean strikethrough;  // Strikethrough property
    private int fontSize;           // Font size (-1 for default)
    private String color;           // Text color (e.g., "#000000")
    private String highlightColor;  // Background color (e.g., "#FFFF00")
    private String paragraphStyle;  // Paragraph style (e.g., "center", "justify")
    private String listStyle;       // List style (e.g., "bullet", "number")

    // Constructor
    public TextSegment(String text) {
        this.text = text;
        this.fontSize = -1; // Default font size
    }

    // Getters and setters
    // Add relevant methods for setting/getting properties
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isBold() { return bold; }
    public void setBold(boolean bold) { this.bold = bold; }
    public boolean isItalic() { return italic; }
    public void setItalic(boolean italic) { this.italic = italic; }
    public boolean isUnderline() { return underline; }
    public void setUnderline(boolean underline) { this.underline = underline; }
    public boolean isStrikethrough() { return strikethrough; }
    public void setStrikethrough(boolean strikethrough) { this.strikethrough = strikethrough; }
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getHighlightColor() { return highlightColor; }
    public void setHighlightColor(String highlightColor) { this.highlightColor = highlightColor; }
    public String getParagraphStyle() { return paragraphStyle; }
    public void setParagraphStyle(String paragraphStyle) { this.paragraphStyle = paragraphStyle; }
    public String getListStyle() { return listStyle; }
    public void setListStyle(String listStyle) { this.listStyle = listStyle; }
}

