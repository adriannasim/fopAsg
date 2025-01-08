package com.mycompany.backend;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jasypt.util.text.BasicTextEncryptor;

import com.google.common.io.Files;

public class FileIO {
    // Create file
    public void createFile(String filename) throws IOException {
        File file;
        if (filename.toLowerCase().contains("test")) {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/test/resources/tmp/" + filename);
        } else {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/main/resources/"
                    + filename);
        }
        System.out.println("Attempting to create file to: " + file.getAbsolutePath());

        file.createNewFile();
    }

    // Create folder
    public void createFolder(String folderName) throws IOException {
        File folder;
        if (folderName.toLowerCase().contains("test")) {
            folder = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/test/resources/tmp/" + folderName);
        } else {
            folder = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/main/resources/" + folderName);
        }
        System.out.println("Attempting to create folder at: " + folder.getAbsolutePath());

        // create the folder
        folder.mkdirs();
    }

    // Load file
    public File loadFile(String filename) throws URISyntaxException, FileNotFoundException {
        File file;
        if (filename.equals("TestUsers.txt") || filename.equals("testApple.jpg") || filename.equals("testBanana.jpg")
                || filename.equals("testGrape.jpg") || filename.equals("testOrange.jpg")) {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/test/resources/"
                    + filename);
        } else if (filename.toLowerCase().contains("test")) {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/test/resources/tmp/" + filename);
        } else {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/main/resources/"
                    + filename);
        }

        return file;

        // ClassLoader classLoader = getClass().getClassLoader();
        // URL resource = classLoader.getResource(filename);
        // if (resource == null)
        // {
        // throw new FileNotFoundException("File not found: " + filename);
        // }
        // return new File(resource.toURI());
    }

    // Load files
    public List<File> loadFiles(String folderName, String key) throws URISyntaxException, FileNotFoundException {
        List<File> files = new ArrayList<>();
        File filePath = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/main/resources/" + folderName);

        if (folderName.toLowerCase().contains("test")) {
            filePath = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/test/resources/" + folderName);
        } else {
            filePath = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/")
                    + "src/main/resources/" + folderName);
        }

        // get files that matches the key
        if (filePath.listFiles() != null) {
            for (File file : filePath.listFiles()) {
                if (file.getName().startsWith(key)) {
                    files.add(file);
                }
            }
        }

        return files;

        // ClassLoader classLoader = getClass().getClassLoader();
        // URL resource = classLoader.getResource(filename);
        // if (resource == null)
        // {
        // throw new FileNotFoundException("File not found: " + filename);
        // }
        // return new File(resource.toURI());
    }

    // TXT file manipulation methods
    // Read
    public List<String> readFile(String filename) throws IOException, URISyntaxException {
        List<String> dataArr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(loadFile(filename)))) {
            String data;
            while ((data = br.readLine()) != null) {
                dataArr.add(data);
            }
        }
        return dataArr;
    }

    // Write
    public void writeFile(String filename, List<String> lines) throws IOException, URISyntaxException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename)))) {
            for (String line : lines) {
                // only insert next line if its not the first data
                if (lines.indexOf(line) != 0) {
                    bw.newLine();
                }
                bw.write(line);
            }
        }
    }

    // Export to PDFBOX
    // public void exportToPDFUsingPDFBox(String pdfFilename, List<String> content)
    // throws IOException {
    // // Get the user's Downloads directory
    // File downloadsDir = new File(System.getProperty("user.home") + File.separator
    // + "Downloads");

    // try (PDDocument document = new PDDocument()) {
    // PDPage page = new PDPage();
    // document.addPage(page);

    // try (PDPageContentStream contentStream = new PDPageContentStream(document,
    // page)) {
    // contentStream.setFont(PDType1Font.HELVETICA, 12);
    // contentStream.beginText();
    // contentStream.setLeading(14.5f);
    // contentStream.newLineAtOffset(50, 750);

    // for (String line : content) {
    // contentStream.showText(line);
    // contentStream.newLine();

    // }

    // contentStream.endText();
    // }

    // document.save(new File(downloadsDir, pdfFilename));
    // System.out.println("PDF created successfully using PDFBox: " + pdfFilename);
    // } catch (Exception e) {
    // throw new IOException("Error creating PDF with PDFBox: " + e.getMessage(),
    // e);
    // }

    // }

    // Export to PDFBOX
    public void exportToPDFUsingPDFBox(String pdfFilename, List<StyledText> styledContent) throws IOException {

        File downloadsDir = new File(System.getProperty("user.home") + File.separator + "Downloads");
        final float PAGE_WIDTH = 595f;
        final float MARGIN = 50f;
        final float LINE_HEIGHT = 14.5f;
        final float MAX_LINE_WIDTH = PAGE_WIDTH - 2 * MARGIN;
        float xPosition = MARGIN;
        float yPosition = 750f;

        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yPosition);

            StringBuilder currentLine = new StringBuilder();
            float currentWidth = 0; // Track accumulated width

            boolean nextLine = false;

            // Inside the loop where you handle paragraphs
            for (StyledText styledText : styledContent) {
                // Declare the current line variables
                PDFont currentFont = styledText.getPDFont();
                float currentFontSize = styledText.getFontSize();

                // Apply the font and font size for this particular StyledText
                contentStream.setFont(currentFont, currentFontSize);

                // Font color (apply as needed)
                if (styledText.getForegroundColor() != null && !styledText.getForegroundColor().isEmpty()) {
                    float[] rgb = styledText.parseColor(styledText.getForegroundColor());
                    contentStream.setNonStrokingColor(rgb[0], rgb[1], rgb[2]);
                }

                // Contents
                String text = styledText.getText();

                String[] words = text.split("(?<=\\s)|(?=\\s)");

                for (int i = 0; i < words.length; i++) {

                    if (words[i].equals("\n")) {
                        // Move to next line
                        contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                    } else {

                        float wordWidth = (currentFont.getStringWidth(words[i]) / 1000) * currentFontSize;
                        float newWidth = currentWidth + wordWidth;

                        // Check if adding this word would exceed line width
                        if (currentWidth > 0 && newWidth > MAX_LINE_WIDTH) {
                            // Check y position
                            yPosition -= LINE_HEIGHT;
                            if (yPosition <= 50) {
                                // Open a new page
                                contentStream.endText();
                                contentStream.close();
                                page = new PDPage();
                                document.addPage(page);
                                contentStream = new PDPageContentStream(document, page);
                                contentStream.beginText();
                                contentStream.setFont(currentFont, currentFontSize); // Apply font on new page
                                yPosition = 750f;
                                contentStream.newLineAtOffset(xPosition, yPosition);
                            } else {
                                // Move to next line
                                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                            }

                            // currentLine.append(word);
                            currentWidth = wordWidth;
                        } else {
                            // Add word to current line
                            // currentLine.append(word);
                            currentWidth = newWidth;
                        }

                        // Write the text
                        contentStream.showText(words[i].toString());
                    }

                }
            }
            contentStream.endText();
            contentStream.close();
            document.save(new File(downloadsDir, pdfFilename));
            System.out.println("PDF created successfully: " + pdfFilename);

        } catch (

        Exception e) {
            throw new IOException("Error creating PDF: " + e.getMessage(), e);
        }
    }

    // Append
    public void appendFile(String filename, Object dataToAdd) throws IOException, URISyntaxException {
        File file = loadFile(filename);
        // add new line of data at the end of the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            // if file nt empty
            if (file.length() != 0) {
                bw.newLine();
            }
            bw.write(dataToAdd.toString());
        }
    }

    // Add files
    public void addFile(String folderToAdd, File file, String filenameToSaveAs, String fileType)
            throws IOException, URISyntaxException {
        File destination = new File(loadFile(folderToAdd), filenameToSaveAs + "." + fileType);

        // copy file to destination
        FileOutputStream fos = new FileOutputStream(destination);
        Files.copy(file, fos);
    }

    // Edit
    // use key to find which line to replace/edit
    public void editFile(String filename, Object dataToUpdate, String key) throws IOException, URISyntaxException {
        int index = -1;

        // get the whole file data first
        List<String> lines = readFile(filename);

        // find which line to update
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(key)) {
                index = lines.indexOf(line);
            }
        }

        if (index == -1 || index > lines.size()) {
            throw new IllegalArgumentException("Invalid line " + index);
        }

        lines.set(index, dataToUpdate.toString()); // replace the data at the specified line with the new data

        writeFile(filename, lines); // rewrite entire thing back to the txt file
    }

    // Delete
    public void deleteLineFile(String filename, String key) throws IOException, URISyntaxException {
        int index = -1;

        List<String> lines = readFile(filename);

        // find which line to update
        for (String line : lines) {
            String[] data = line.split(",");
            if (data[0].equals(key)) {
                index = lines.indexOf(line);
            }
        }

        if (index == -1 || index > lines.size()) {
            throw new IllegalArgumentException("Invalid line.");
        }

        lines.remove(index); // remove the line

        writeFile(filename, lines); // rewrite entire thing back to the txt file
    }

    // Clear entire file content
    public void clearFile(String filename) throws URISyntaxException, IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename)))) {
            bw.write("");
        }
    }

    // Purge file
    public void purgeFile(String filename) throws URISyntaxException, FileNotFoundException {
        File file = loadFile(filename);

        if (file.delete()) {
            System.out.println("File \"" + filename + "\" deleted successfully.");
        } else {
            System.out.println("Failed to delete file \"" + filename + "\". File may not exist or is in use.");
        }
    }

    public void purgeFileByFullPath(String filename) throws URISyntaxException, FileNotFoundException {
        File file = new File(filename);

        if (file.delete()) {
            System.out.println("File \"" + filename + "\" deleted successfully.");
        } else {
            System.out.println("Failed to delete file \"" + filename + "\". File may not exist or is in use.");
        }
    }

    // Clear tmp folder
    public void clearTmpFolder() throws URISyntaxException, IOException {
        // load folder
        File folder = new File(
                (System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/test/resources/tmp");
        if (folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                if (!file.getName().equals("images")) {
                    if (!file.delete()) {
                        continue;
                    }
                }
            }
        }
    }

    //purge folder
    public void purgeFolder(String folderName) throws IOException, URISyntaxException 
    {
        File folder = loadFile(folderName);
    
        deleteFolderContents(folder);
    
        if (folder.delete())
        {
            System.out.println("Folder \"" + folderName + "\" deleted successfully.");
        } 
        else 
        {
            System.out.println("Failed to delete folder \"" + folderName + "\". It may be in use.");
        }
    }

    //repeatly delete folder contents
    private void deleteFolderContents(File folder) 
    {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    deleteFolderContents(file);
                }
                if (file.delete()) 
                {
                    System.out.println("Deleted: " + file.getAbsolutePath());
                } 
                else 
                {
                    System.out.println("Failed to delete: " + file.getAbsolutePath());
                }
            }
        }
    }
    
}