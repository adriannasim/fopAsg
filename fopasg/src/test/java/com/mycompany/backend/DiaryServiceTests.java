package com.mycompany.backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import org.junit.*;

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
        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", LocalDateTime.now(), "Today I am Happy.").isSuccessful());
        
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
    
    @Test
    public void testExportDiaryToPDF() {
        try {
            // Create sample diary entries
            diaryService.newDiaryEntry("First Entry", LocalDateTime.of(2024, 1, 10, 10, 0), "This is the first entry.");
            diaryService.newDiaryEntry("Second Entry", LocalDateTime.of(2024, 1, 15, 12, 0), "This is the second entry.");
            diaryService.newDiaryEntry("Third Entry", LocalDateTime.of(2024, 1, 20, 14, 0), "This is the third entry.");

            // Export entries within a date range to PDF
            LocalDateTime startDate = LocalDateTime.of(2024, 1, 10, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 1, 18, 23, 59);
            String pdfFilename = "TestExportDiary.pdf";

            ServiceResult result = diaryService.exportDiaryToPDF(startDate, endDate, pdfFilename);

            // Verify the operation was successful
            assertTrue(result.isSuccessful());

            // Use the correct method to access the message
            assertEquals("Diary entries exported to PDF successfully.", result.getReturnMessage());

            // Check if the file was created
            assertTrue(fileIO.loadFile(pdfFilename).exists());

            // Clean up the PDF file after test
            fileIO.purgeFile(pdfFilename);
        } catch (Exception e) {
            fail("Exception during export test: " + e.getMessage());
        }
    }

    
}
