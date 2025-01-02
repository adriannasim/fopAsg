package com.mycompany.backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

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

        //login
        ServiceResult result = userService.userLogin("TestUser1", "test123");
        //pass username to diary service constructor
        diaryService = new DiaryService((String) result.getReturnObject());
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

        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", LocalDate.now(), "Today I am Happy.", Diary.Mood.HAPPY).isSuccessful());
  
        //TODO assertEquals using search 
    }

    //TODO after search is created
    @Test 
    public void testEditDiaryEntry()
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
