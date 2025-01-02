package com.mycompany.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
            if (!fileIO.loadFile(filename).exists())
            {
                    fileIO.createFile(filename);   
            }
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                // String[] diaryInfo = line.split(",");
                // if (diaryInfo[5].equals("null"))
                // {
                //     // diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDate.parse(diaryInfo[2]), diaryInfo[3], Diary.Mood.valueOf(diaryInfo[4])));
                // }
                diaryList.add(parseDiary(line, filename));
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
    public static Diary parseDiary(String diaryString, String filename) {
        // Extract the diaryId
        String diaryId = diaryString.substring(0, 36);

        // Remove the "{" prefix and "}" suffix
        String cleanString = diaryString.substring(36, diaryString.length() - 1);

        // Split fields by format (, xxx='x')
        String[] fields = cleanString.split(", (?=\\w+='.*')");

        // Extract fields
        String diaryTitle = fields[0].split("=")[1].replace("'", "").trim();
        String diaryDateStr = fields[1].split("=")[1].replace("'", "").trim();
        String mood = fields[2].split("=")[1].replace("'", "").trim();
        String deletionDateStr = fields[3].split("=")[1].replace("'", "").trim();
        String diaryContent = fields[4].split("=")[1].replaceFirst("'", "").replaceAll("'$", "").trim();

        // Parse diaryDate
        LocalDateTime diaryDate = null;
        LocalDate deletionDate = null;

        try {
            diaryDate = LocalDateTime.parse(diaryDateStr);
            if (!deletionDateStr.equals("null")){
                deletionDate = LocalDate.parse(deletionDateStr);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a new Diary object
        return new Diary(filename, diaryId, diaryTitle, diaryDate, diaryContent, Diary.Mood.valueOf(mood), deletionDate);
    }

    //get diary by title (Search) TODO

    //create diary
    public ServiceResult newDiaryEntry(String diaryTitle, LocalDateTime diaryDate, String diaryContent, Diary.Mood mood)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
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

                fileIO.appendFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent, mood));
                
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
    public ServiceResult editDiaryEntry(String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Diary.Mood mood)
    {
        
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
        {
            return new ServiceResult(false, null, "Info incomplete.");
        }
        else
        {
            try 
            {
                fileIO.editFile(filename, new Diary(filename, diaryId.toString(), diaryTitle, diaryDate, diaryContent, mood), diaryId);
                
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

    //move diary to bin
    public ServiceResult moveEntryToBin(Diary diaryEntryToBeDeleted)
    {
        try 
        {
            fileIO.editFile(filename, new Diary(filename, diaryEntryToBeDeleted.getDiaryId(), diaryEntryToBeDeleted.getDiaryTitle(), diaryEntryToBeDeleted.getDiaryDate(),
                diaryEntryToBeDeleted.getDiaryContent(), diaryEntryToBeDeleted.getMood(), LocalDate.now()), diaryEntryToBeDeleted.getDiaryId()
            );
            
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

    //restore diary from recycle bin
    public ServiceResult restoreDiaryEntry(Diary diaryEntryToBeRestored)
    {
        try 
        {
            fileIO.editFile(filename, new Diary(filename, diaryEntryToBeRestored.getDiaryId(), diaryEntryToBeRestored.getDiaryTitle(), diaryEntryToBeRestored.getDiaryDate(),
                diaryEntryToBeRestored.getDiaryContent(), diaryEntryToBeRestored.getMood(), null), diaryEntryToBeRestored.getDiaryId()
            );
            
            //done 
            return new ServiceResult(true, null, "Diary entry restored successfully.");
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

    //delete diary entry permanently
    public ServiceResult deleteDiaryEntry(String diaryId)
    {
        try 
        {
            fileIO.deleteLineFile(filename, diaryId);
            
            //done 
            return new ServiceResult(true, null, "Diary entry permanently deleted successfully.");
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

    //clear diary entries that have been in the bin for 30 days (Run when app starts)
    public void clearOldDiaryEntry(String diaryId)
    {
        try 
        {
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                // String[] diaryInfo = line.split(",");
                // if (!(diaryInfo[5].equals("null")) && LocalDate.parse(diaryInfo[5]).until(LocalDate.now(), ChronoUnit.DAYS) >= 30) //if more or equal than 30
                // {
                //     fileIO.deleteLineFile(filename, diaryInfo[0]);
                // }
                if (parseDiary(line, filename).getDeletionDate() != null && parseDiary(line, filename).getDeletionDate().until(LocalDate.now(), ChronoUnit.DAYS) >= 30) //if more or equal than 30
                {
                    fileIO.deleteLineFile(filename, parseDiary(line, filename).getDiaryId());
                }
            }
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
