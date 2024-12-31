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
                // String[] diaryInfo = line.split(",");
                // diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDateTime.parse(diaryInfo[2]), diaryInfo[3]));
                diaryList.add(parseDiary(line));
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

    // Method to parse the diary line from csv file into diary object
    public static Diary parseDiary(String diaryString) {
        // Remove the "Diary {" prefix and "}" suffix
        String cleanString = diaryString.substring(36, diaryString.length() - 1);

        // Split fields by format (, xxx='x')
        String[] fields = cleanString.split(", (?=\\w+='.*')");

        // Extract fields
        String username = fields[0].split("=")[1].replace("'", "").trim();
        String diaryId = fields[1].split("=")[1].replace("'", "").trim();
        String diaryTitle = fields[2].split("=")[1].replace("'", "").trim();
        String diaryDateStr = fields[3].split("=")[1].replace("'", "").trim();
        String diaryContent = fields[4].split("=")[1].replaceFirst("'", "").replaceAll("'$", "").trim();

        // Parse diaryDate
        LocalDateTime diaryDate = null;
        try {
            diaryDate = LocalDateTime.parse(diaryDateStr);
        } catch (Exception e) {
            System.err.println("Invalid diaryDate: " + diaryDateStr);
        }

        // Return a new Diary object
        return new Diary(username, diaryId, diaryTitle, diaryDate, diaryContent);
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
                fileIO.editFile(filename, new Diary(filename, diaryId, diaryTitle, diaryDate, diaryContent), diaryId);
                
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

}
