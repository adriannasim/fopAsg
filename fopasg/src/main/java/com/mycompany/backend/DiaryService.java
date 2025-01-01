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
                Diary.Mood mood = Diary.Mood.valueOf(diaryInfo[4]);
                diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDateTime.parse(diaryInfo[2]), diaryInfo[3], mood));
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
    public boolean newDiaryEntry(String diaryTitle, LocalDateTime diaryDate, String diaryContent, Diary.Mood mood)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
        {
            System.err.println("Info incomplete.");
            return false;
        }
        else
        {
            //check if the file exists, if not, it means that its the user's first entry so create a new file for the user
            if (!fileIO.loadFile(filename).exists())
            {
                try 
                {
                    fileIO.createFile(filename);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }

            try 
            {
                fileIO.appendFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent, mood));
                
                //done 
                return true;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    //edit diary
    public boolean editDiaryEntry(String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Mood mood)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
        {
            System.err.println("Info incomplete.");
            return false;
        }
        else
        {
            try 
            {
                fileIO.editFile(filename, new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent, mood), diaryId);
                
                //done 
                return true;
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
    public boolean deleteDiaryEntry(String diaryId)
    {
        try 
        {
            fileIO.deleteLineFile(filename, diaryId);
            
            //done 
            return true;
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
