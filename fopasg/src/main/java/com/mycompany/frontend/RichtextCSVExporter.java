package com.mycompany.frontend;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.model.Document;
import com.gluonhq.richtextarea.model.DecorationModel;
import com.gluonhq.richtextarea.model.Decoration;
import com.gluonhq.richtextarea.model.TextDecoration;
import com.gluonhq.richtextarea.model.ParagraphDecoration.GraphicType;
import com.gluonhq.richtextarea.model.ParagraphDecoration;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

/***
 * THIS CONTROLLER CLASS IS USED FOR EXPORT INTO CSV AND IMPORT FROM CSV
 * 
 ***/

class RichTextCSVExporter {

    /***
     * SOME FORMAT DELIMETERS.
     * 
     ***/
    private static final String DECORATION_DELIMITER = "||";
    private static final String FORMAT_PREFIX = "{{";
    private static final String FORMAT_SUFFIX = "}}";

    /***
     * METHOD TO EXPORT THE RICH TEXT AREA CONTENT INTO CSV.
     * 
     ***/
    public static String exportToCSV(RichTextArea richTextArea) throws IOException {
        Document document = richTextArea.getDocument();
        StringBuilder csvContent = new StringBuilder();

        // // Add CSV headers
        // csvContent.append("Text,Decorations,ParagraphDecorations\n");

        // Iterate through the document content
        String text = document.getText();
        List<DecorationModel> decorations = document.getDecorations();

        int currentPos = 0;
        for (DecorationModel decoration : decorations) {
            // Get text segment
            String segment = text.substring(currentPos, currentPos + decoration.getLength());

            // Encode text decorations
            String textDecorations = encodeDecorations(decoration.getDecoration());

            // Encode paragraph decorations
            String paragraphDecorations = encodeParagraphDecorations(decoration.getParagraphDecoration());

            // Escape special characters in text
            String escapedSegment = escapeCSV(segment);

            // Add to content drafted
            csvContent.append(escapedSegment).append(",")
                    .append(textDecorations).append(",")
                    .append(paragraphDecorations).append("###SPLIT###");

            currentPos += decoration.getLength();
        }

        // Write the drafted content to file using BufferedWriter
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        //     writer.write(csvContent.toString());
        // }

        // return the formatted contents
        return csvContent.toString();
    }

    /***
     * HELPER METHOD TO ENCODE THE TEXTDECORATION.
     * 
     ***/
    private static String encodeDecorations(Decoration decoration) {
        if (decoration == null)
            return "";

        StringBuilder encoded = new StringBuilder();

        encoded.append(FORMAT_PREFIX);

        // Encode each decoration attributes
        if (decoration instanceof TextDecoration) {
            TextDecoration textDecoration = (TextDecoration) decoration;

            // Font family
            if (textDecoration.getFontFamily() != null) {
                encoded.append("font:").append(textDecoration.getFontFamily())
                        .append(DECORATION_DELIMITER);
            }
            // Font size
            if (textDecoration.getFontSize() > 0) {
                encoded.append("size:").append(textDecoration.getFontSize())
                        .append(DECORATION_DELIMITER);
            }
            // Highlight color (a.k.a background color)
            if (textDecoration.getBackground() != null) {
                encoded.append("background:").append(textDecoration.getBackground())
                        .append(DECORATION_DELIMITER);
            }
            // Text color (a.k.a foreground color)
            if (textDecoration.getForeground() != null) {
                encoded.append("foreground:").append(textDecoration.getForeground())
                        .append(DECORATION_DELIMITER);
            }
            // Italic text (a.k.a font posture)
            if (textDecoration.getFontPosture() != null) {
                encoded.append("italic:").append(textDecoration.getFontPosture())
                        .append(DECORATION_DELIMITER);
            }
            // Bolded text (a.k.a font weight)
            if (textDecoration.getFontWeight() != null) {
                encoded.append("weight:").append(textDecoration.getFontWeight())
                        .append(DECORATION_DELIMITER);
            }
            // Strikethrough text
            if (textDecoration.isStrikethrough()) {
                encoded.append("strikethrough:").append(textDecoration.isStrikethrough())
                        .append(DECORATION_DELIMITER);
            }
            // Underlined text
            if (textDecoration.isUnderline()) {
                encoded.append("underline:").append(textDecoration.isUnderline())
                        .append(DECORATION_DELIMITER);
            }
        }

        encoded.append(FORMAT_SUFFIX);
        return escapeCSV(encoded.toString());
    }

    /***
     * HELPER METHOD TO ENCODE THE PARAGRAPHDECORATION.
     * 
     ***/
    private static String encodeParagraphDecorations(ParagraphDecoration paraDecoration) {
        if (paraDecoration == null)
            return "";

        StringBuilder encoded = new StringBuilder();

        encoded.append(FORMAT_PREFIX);

        // Encode paragraph decoration attributes
        if (paraDecoration.getGraphicType() != null) {
            // List style (Numbered list or Bulleted list or None)
            encoded.append("listStyle:").append(paraDecoration.getGraphicType())
                    .append(DECORATION_DELIMITER);
        }

        encoded.append(FORMAT_SUFFIX);
        return escapeCSV(encoded.toString());
    }

    /***
     * HELPER METHOD TO ESCAPE SOME SPECIAL CHARACTERS.
     * 
     ***/
    private static String escapeCSV(String value) {
        if (value == null)
            return "";

        // Replace \n with \\n to maintain user entries (with newline)
        // replace \r with empty string
        if (value.contains("\n") || value.contains("\r\n"))
            return "\"" + value.replace("\n", "\\n").replace("\r", "") + "\"";

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    /***
     * METHOD TO IMPORT THE CSV CONTENT INTO RICH TEXT AREA.
     * - Return the Document.
     * 
     ***/
    public static Document importFromCSV(String contents) throws IOException {

        Document document = new Document();

        // // Read from CSV
        // try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

        //     // Skip the header
        //     String line = reader.readLine();

        String[] line = contents.split("###SPLIT###");
        
            for (int i = 0; i< line.length; i++) {
                String[] columns = splitCSVLine(line[i]);

                // Handle case where there are not enough columns (means error occurs)
                if (columns.length < 3) {
                    continue;
                }

                // Assuming the CSV columns: Text, Decorations, ParagraphDecorations
                String text = unescapeCSV(columns[0]);
                String decorations = columns[1];
                String paragraphDecorations = columns[2];

                // Decode the decorations
                TextDecoration textDecoration = decodeTextDecorations(decorations);
                ParagraphDecoration paragraphDecoration = decodeParagraphDecorations(
                        paragraphDecorations);

                // Append the text and decorations to the RichTextArea
                document = appendTextToDocument(document, text, textDecoration, paragraphDecoration);
            }
        
        return document;
    }

    /***
     * HELPER METHOD TO UNESCAPE SOME SPECIAL CHARACTERS.
     * 
     ***/
    private static String unescapeCSV(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        // Remove surrounding quotes
        if (value.startsWith("\"") && value.endsWith("\"")) {
            if (value.length() > 1) {
                value = value.substring(1, value.length() - 1);
            }
        }

        // Restore the newline by replacing \\n with \n again
        if (value.contains("\\n")) {
            value = value.replace("\\n", "\n");
        }

        // Replace escaped quotes ("") with a single quote (")
        return value.replace("\"\"", "\"");
    }

    /***
     * HELPER METHOD TO DECODE THE TEXTDECORATION.
     * 
     ***/
    private static TextDecoration decodeTextDecorations(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return null;
        }

        TextDecoration.Builder builder = TextDecoration.builder();

        String content = encoded.substring(FORMAT_PREFIX.length() + 1, encoded.length() - FORMAT_SUFFIX.length() - 1);
        String[] parts = content.split(Pattern.quote(DECORATION_DELIMITER));

        for (String part : parts) {
            // Retrieve the font family
            if (part.startsWith("font:")) {
                builder.fontFamily(part.substring(5));
            }
            // Retrieve the font size
            else if (part.startsWith("size:")) {
                builder.fontSize(Double.parseDouble(part.substring(5)));
            }
            // Retrieve the font weight
            else if (part.startsWith("weight:")) {
                if (part.substring(7).equals("BOLD")) {
                    builder.fontWeight(FontWeight.BOLD);
                } else {
                    builder.fontWeight(FontWeight.NORMAL);
                }
            }
            // Retrieve the italic font
            else if (part.startsWith("italic:")) {
                if (part.substring(7).equals("ITALIC")) {
                    builder.fontPosture(FontPosture.ITALIC);
                } else {
                    builder.fontPosture(FontPosture.REGULAR);
                }
            }
            // Retrieve the highlight color
            else if (part.startsWith("background:")) {
                builder.background(part.substring(11));
            }
            // Retrieve the font color
            else if (part.startsWith("foreground:")) {
                builder.foreground(part.substring(11));
            }
            // Retrieve the strikethrough
            else if (part.startsWith("strikethrough:")) {
                if (part.substring(14).equals("true")) {
                    builder.strikethrough(true);
                }
            }
            // Retrieve the underline
            else if (part.startsWith("underline:")) {
                if (part.substring(10).equals("true")) {
                    builder.underline(true);
                }
            }
        }

        return builder.build();
    }

    /***
     * HELPER METHOD TO DECODE THE PARAGRAPHDECORATION.
     * 
     ***/
    private static ParagraphDecoration decodeParagraphDecorations(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return null;
        }

        ParagraphDecoration.Builder builder = ParagraphDecoration.builder();

        String content = encoded.substring(FORMAT_PREFIX.length() + 1, encoded.length() - FORMAT_SUFFIX.length() - 1);
        String[] parts = content.split(Pattern.quote(DECORATION_DELIMITER));

        for (String part : parts) {
            // Retrieve the list style
            if (part.startsWith("listStyle:")) {
                String extracted = part.substring(10).trim();
                if (extracted.equals("BULLETED_LIST")) {
                    builder.graphicType(GraphicType.BULLETED_LIST);
                } else if (extracted.equals("NUMBERED_LIST")) {
                    builder.graphicType(GraphicType.NUMBERED_LIST);
                } else {
                    builder.graphicType(GraphicType.NONE);
                }
            }

            // Default settings
            builder.spacing(0.0);
            builder.alignment(TextAlignment.LEFT);
            builder.topInset(0.0);
            builder.rightInset(0.0);
            builder.bottomInset(0.0);
            builder.leftInset(0.0);
        }

        return builder.build();
    }

    /***
     * HELPER METHOD TO SAFELY SPLIT CSV BY DELIMETER ",".
     * 
     ***/
    private static String[] splitCSVLine(String line) {
        List<String> columns = new ArrayList<>();
        int start = 0;
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            // If encounter "
            if (c == '"') {
                // Toggle the insideQuotes flag
                insideQuotes = !insideQuotes;
            }
            // If encounter , and not inside quote
            else if (c == ',' && !insideQuotes) {
                // Treat , as a delimiter
                columns.add(line.substring(start, i));
                start = i + 1;
            }
        }

        // Add the last column
        columns.add(line.substring(start));

        return columns.toArray(new String[0]);
    }

    /***
     * HELPER METHOD TO APPEND EACH LINE TO DOCUMENT.
     * 
     ***/
    private static Document appendTextToDocument(Document doc, String text, TextDecoration textDecoration,
            ParagraphDecoration paragraphDecoration) {

        // Get the existing document text and decorations
        String existingText = doc.getText();
        List<DecorationModel> existingDecorations = doc.getDecorations();

        // Combine the old text with the new text
        String updatedText = existingText + text;

        // Create a new DecorationModel for the new text to retain formatting
        DecorationModel newDecoration = new DecorationModel(
                existingText.length(), // Start position for the new text
                text.length(), // Length of the new text
                textDecoration, // New text decoration
                paragraphDecoration // New paragraph decoration
        );

        // Add the new decoration to the existing list
        List<DecorationModel> updatedDecorations = new ArrayList<>(existingDecorations);
        updatedDecorations.add(newDecoration);

        // Create a new document with updated text and decorations
        Document updatedDoc = new Document(updatedText, updatedDecorations, doc.getCaretPosition() + text.length());

        return updatedDoc;
    }
}