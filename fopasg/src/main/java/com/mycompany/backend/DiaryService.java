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
                if (diaryInfo[5].equals("null"))
                {
                    Diary.Mood mood = Diary.Mood.valueOf(diaryInfo[4]);
                    diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDateTime.parse(diaryInfo[2]), diaryInfo[3], mood));
                }
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
                fileIO.editFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent, mood), diaryId);
                
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
    public ServiceResult deleteDiaryEntry(Diary diaryEntryToBeDeleted)
    {
        try 
        {
            fileIO.editFile(filename, new Diary(filename, diaryEntryToBeDeleted.getDiaryId(), diaryEntryToBeDeleted.getDiaryTitle(), diaryEntryToBeDeleted.getDiaryDate(),
                diaryEntryToBeDeleted.getDiaryContent(), diaryEntryToBeDeleted.getMood(), LocalDateTime.now()), diaryEntryToBeDeleted.getDiaryId()
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
}
