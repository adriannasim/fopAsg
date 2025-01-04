package com.mycompany.backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DiaryServiceTests 
{
    String userFile = "TestUsers.txt";
    UserService userService = new UserService(userFile);
    FileIO fileIO = new FileIO();
    DiaryService diaryService;
        
    @Before
    public void setup()
    {
        //create user for test
        userService.userSignUp("TestUser1", "test1@gmail.com", "test123");
        userService.userSignUp("TestUser2", "test2@gmail.com", "test123");
    }

    @After
    public void cleanUp()
    {
        //clear entire file
        try 
        {
            //clear user file
            fileIO.clearFile(userFile);
            //delete the test csv file
            fileIO.purgeFile("TestUser1.csv");
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //New diary entry tests---------------------------------------------------------------------------------------------------------------------------------
    @Test 
    public void testNewDiaryEntry()
    {        
        //login
        ServiceResult result = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //get datetime first
        LocalDateTime createTime = LocalDateTime.now();

        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", createTime, "Today I am Happy.", Diary.Mood.HAPPY, null).isSuccessful());
  
        //check if content is correct
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title");
        assertEquals(diary.getDiaryDate(), createTime);
        assertEquals(diary.getDiaryContent(), "Today I am Happy.");
        assertEquals(diary.getMood(), Diary.Mood.HAPPY);
    }

    @Test 
    public void testNewDiaryEntryWithOneImage()
    {
        //login
        ServiceResult result = userService.userLogin("TestUser2", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //get datetime first
        LocalDateTime createTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();
        try
        {
            images.add(fileIO.loadFile("testApple.jpg"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", createTime, "Today I am Happy.", Diary.Mood.HAPPY, images).isSuccessful());
  
        //check if content is correct
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title");
        assertEquals(diary.getDiaryDate(), createTime);
        assertEquals(diary.getDiaryContent(), "Today I am Happy.");
        assertEquals(diary.getMood(), Diary.Mood.HAPPY);
        assertEquals(diary.getImagePaths().size(), 1);
    }

    @Test 
    public void testNewDiaryEntryWithThreeImages()
    {
        //login
        ServiceResult result = userService.userLogin("TestUser2", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //get datetime first
        LocalDateTime createTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();
        try
        {
            images.add(fileIO.loadFile("testApple.jpg"));
            images.add(fileIO.loadFile("testBanana.jpg"));
            images.add(fileIO.loadFile("testGrape.jpg"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Diary Title 2", createTime, "Today I am Happy.", Diary.Mood.HAPPY, images).isSuccessful());
  
        //check if content is correct
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title");
        assertEquals(diary.getDiaryDate(), createTime);
        assertEquals(diary.getDiaryContent(), "Today I am Happy.");
        assertEquals(diary.getMood(), Diary.Mood.HAPPY);
        assertEquals(diary.getImagePaths().size(), 3);
    }

    //Test Edit Diary Entry-----------------------------------------------------------------------------------------------------------------
    @Test 
    public void testEditDiaryEntry()
    {        
        
    }

    @Test 
    public void testEditAddOneImage()
    {        
        
    }

    @Test 
    public void testEditRemoveOneImage()
    {        
        
    }
    
    @Test 
    public void testEditRemoveAndAddImage()
    {        
        
    }
    
    //Test Delete Diary Entry----------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteDiaryEntry()
    {

    }

    @Test
    public void testDeleteAllUserEntries()
    {
        //delete user first
        assertTrue(userService.userDelete("TestUser1").isSuccessful());
        //check if the diary file exist or not, if it does not exists means the test passed
        assertThrows(RuntimeException.class, () -> diaryService.getAllDiary());
    }
}
