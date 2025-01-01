package com.mycompany.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DiaryService
{
    private FileIO fileIO = new FileIO();
    private String filename;

    public DiaryService(String username)
    {
        //diary entries file will be saved as username
        filename = username + ".csv";
    }

    //get all diary entries for the user
    public List<Diary> getAllDiary()
    {
        List<Diary> diaryList = new ArrayList<>();
        try 
        {
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                String[] diaryInfo = line.split(",");
                diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDateTime.parse(diaryInfo[2]), diaryInfo[3]));
            }

            //done
            return diaryList;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //get diary by title (Search) TODO

    //create diary
    public ServiceResult newDiaryEntry(String diaryTitle, LocalDateTime diaryDate, String diaryContent)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null)
        {
            return new ServiceResult(false, null, "Info incomplete.");
        }
        else
        {
            //check if the file exists, if not, it means that its the user's first entry so create a new file for the user
            try 
            {
                if (!fileIO.loadFile(filename).exists())
                {
                    fileIO.createFile(filename);   
                }

                //update file
                fileIO.appendFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent));
                
                //done 
                return new ServiceResult(true, null, "Diary entry created.");

            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e) 
            {
                throw new RuntimeException(e);
            }
        }
    }

    //edit diary
    public ServiceResult editDiaryEntry(String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null)
        {
            return new ServiceResult(false, null, "Info incomplete.");
        }
        else
        {
            try 
            {
                fileIO.editFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent), diaryId);
                
                //done
                return new ServiceResult(true, null, "Diary entry edited.");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    //delete diary
    public ServiceResult deleteDiaryEntry(String diaryId)
    {
        try 
        {
            fileIO.deleteLineFile(filename, diaryId);
            
            //done 
            return new ServiceResult(true, null, "Diary entry deleted successfully.");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    // Export diary entries within a range to PDF
    public ServiceResult exportDiaryToPDF(LocalDateTime startDate, LocalDateTime endDate, String pdfFilename) {
        try {
            // Fetch all diary entries
            List<Diary> diaryList = getAllDiary();

            // Filter entries by date range
            List<String> entriesInRange = new ArrayList<>();
            for (Diary diary : diaryList) {
                if (!diary.getDiaryDate().isBefore(startDate) && !diary.getDiaryDate().isAfter(endDate)) {
                    // Format each diary entry for PDF export
                    entriesInRange.add(formatDiaryEntryForExport(diary));
                }
            }

            if (entriesInRange.isEmpty()) {
                return new ServiceResult(false, null, "No diary entries found in the specified date range.");
            }

            // Export filtered entries to PDF using FileIO
            fileIO.exportToPDFUsingPDFBox(pdfFilename, entriesInRange); // Replace with `exportToPDFUsingIText` if preferred

            return new ServiceResult(true, null, "Diary entries exported to PDF successfully.");
        } catch (Exception e) {
            return new ServiceResult(false, null, "Error exporting diary entries to PDF: " + e.getMessage());
        }
    }

// Helper method to format a diary entry for PDF export
private String formatDiaryEntryForExport(Diary diary) {
    return "Title: " + diary.getDiaryTitle() + "\n" +
           "Date: " + diary.getDiaryDate() + "\n" +
           "Content:\n" + diary.getDiaryContent() + "\n" +
           "----------------------------------------";
}   
    
}
