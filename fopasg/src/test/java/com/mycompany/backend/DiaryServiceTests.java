package com.mycompany.backend;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.*;

public class DiaryServiceTests 
{
    String userFile = "TestUsers.txt";
    UserService userService = new UserService(userFile);
    FileIO fileIO = new FileIO();
        
    @Before
    public void setup()
    {
        //create users for test
        userService.userSignUp("TestUser1", "test1@gmail.com", "test123");
    }

    @After
    public void cleanUp()
    {
        //clear entire file
        fileIO.purgeFile(userFile);
        fileIO.purgeFile("TestUser1.csv");
    }

    //New diary entry tests---------------------------------------------------------------------------------------------------------------------------------
    @Test 
    public void testNewDiaryEntry()
    {        
        //login
        String loginUser = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService(loginUser);

        assertTrue(diaryService.newDiaryEntry("Test Diary Title", LocalDateTime.now(), "Today I am Happy."));
        
        //TODO assertEquals using search 
    }

    //TODO after search is created
    @Test 
    public void testEditDiaryEntry()
    {        

    }
    
    @Test
    public void testDeleteDiaryEntry()
    {

    }
}
