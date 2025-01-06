package com.mycompany.backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiaryServiceTests 
{
    static String userFile = "TestUsers.txt";
    static UserService userService = new UserService(userFile);
    static FileIO fileIO = new FileIO();
    
    DiaryService diaryService;
        
    @BeforeClass
    public static void setup()
    {
        //create user for test
        userService.userSignUp("TestUser1", "test1@gmail.com", "test123");
        userService.userSignUp("TestUser2", "test2@gmail.com", "test123");
    }

    // @AfterClass
    // public static void cleanUp()
    // {
    //     //clear entire file
    //     try 
    //     {
    //         //clear user file
    //         fileIO.clearFile(userFile);
    //         fileIO.clearTmpFolder();
    //     }
    //     catch (URISyntaxException e)
    //     {
    //         throw new RuntimeException(e);
    //     }
    //     catch (FileNotFoundException e)
    //     {
    //         throw new RuntimeException(e);
    //     }
    //     catch (IOException e)
    //     {
    //         throw new RuntimeException(e);
    //     }
    // }

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
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", createTime, "Today I am Happy.", Diary.Mood.HAPPY, new ArrayList<>()).isSuccessful());
  
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
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title 2");
        assertEquals(diary.getDiaryDate(), createTime);
        assertEquals(diary.getDiaryContent(), "Today I am Happy.");
        assertEquals(diary.getMood(), Diary.Mood.HAPPY);
        assertEquals(diary.getImagePaths().size(), 3);
    }

    //Test Edit Diary Entry-----------------------------------------------------------------------------------------------------------------
    @Test 
    public void testEditDiaryEntry()
    {        
        //login
        ServiceResult result = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //get datetime first
        LocalDateTime createTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();

        //check if the result of a new diary entry returns true (means operation successful)
        assertTrue(diaryService.newDiaryEntry("Test Edit Diary", createTime, "Today I am Happy.", Diary.Mood.HAPPY, images).isSuccessful());
  
        //check if content is correct
        Diary diary = diaryService.getDiaryByTitle("Test Edit Diary");
        assertEquals(diary.getDiaryDate(), createTime);
        assertEquals(diary.getDiaryContent(), "Today I am Happy.");
        assertEquals(diary.getMood(), Diary.Mood.HAPPY);
        assertEquals(diary.getImagePaths().size(), 3);

        //edit
        assertTrue(diaryService.editDiaryEntry(diary.getDiaryId(), "Edited Title", createTime, "Today I am Sad.", Diary.Mood.SAD, images).isSuccessful());

        //check if content is correct
        Diary editedDiary = diaryService.getDiaryByTitle("Edited Title");
        assertEquals(editedDiary.getDiaryDate(), createTime);
        assertEquals(editedDiary.getDiaryContent(), "Today I am Sad.");
        assertEquals(editedDiary.getMood(), Diary.Mood.SAD);
    }

    @Test 
    public void testEditAddOneImage()
    {        
        //login
        ServiceResult result = userService.userLogin("TestUser2", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //time
        LocalDateTime editedDateTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();
        try
        {
            images.add(fileIO.loadFile("testApple.jpg"));
            images.add(fileIO.loadFile("testOrange.jpg"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        //get the first diary just now to be edited
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title");

        //check if initially image count is one
        assertEquals(diary.getImagePaths().size(), 1);

        //edit
        assertTrue(diaryService.editDiaryEntry(diary.getDiaryId(), "Edited To Add One Image", editedDateTime, "Today I am Sad.", Diary.Mood.SAD, images).isSuccessful());
    
        //check if content is correct
        Diary editedDiary = diaryService.getDiaryByTitle("Edited To Add One Image");
        assertEquals(editedDiary.getDiaryDate(), editedDateTime);
        assertEquals(editedDiary.getDiaryContent(), "Today I am Sad.");
        assertEquals(editedDiary.getMood(), Diary.Mood.SAD);
        assertEquals(editedDiary.getImagePaths().size(), 2); //it should not add the apple image since it already exists
    }

    @Test 
    public void testEditRemoveOneImage()
    {        
        //login
        ServiceResult result = userService.userLogin("TestUser2", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //time
        LocalDateTime editedDateTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();
        try
        {
            images.add(fileIO.loadFile("testApple.jpg"));
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

        //get the first diary just now to be edited
        Diary diary = diaryService.getDiaryByTitle("Test Diary Title 2");

        //check if initially image count is three
        assertEquals(diary.getImagePaths().size(), 3);

        //edit
        assertTrue(diaryService.editDiaryEntry(diary.getDiaryId(), "Edited To Remove One Image", editedDateTime, "Today I am Sad.", Diary.Mood.SAD, images).isSuccessful());
    
        //check if content is correct
        Diary editedDiary = diaryService.getDiaryByTitle("Edited To Remove One Image");
        assertEquals(editedDiary.getDiaryDate(), editedDateTime);
        assertEquals(editedDiary.getDiaryContent(), "Today I am Sad.");
        assertEquals(editedDiary.getMood(), Diary.Mood.SAD);
        assertEquals(editedDiary.getImagePaths().size(), 2); //it should remove the banana image
    }
    
    @Test 
    public void testEditRemoveAndAddImage()
    {        
        //login
        ServiceResult result = userService.userLogin("TestUser2", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //time
        LocalDateTime editedDateTime = LocalDateTime.now();

        //image to add
        List<File> images = new ArrayList<>();
        try
        {
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

        //get the first diary just now to be edited
        Diary diary = diaryService.getDiaryByTitle("Edited To Remove One Image");

        //check if initially image count is two
        assertEquals(diary.getImagePaths().size(), 2);

        //edit
        assertTrue(diaryService.editDiaryEntry(diary.getDiaryId(), "Edited To Add And Remove", editedDateTime, "Today I feel Normal.", Diary.Mood.NORMAL, images).isSuccessful());
    
        //check if content is correct
        Diary editedDiary = diaryService.getDiaryByTitle("Edited To Add And Remove");
        assertEquals(editedDiary.getDiaryDate(), editedDateTime);
        assertEquals(editedDiary.getDiaryContent(), "Today I feel Normal.");
        assertEquals(editedDiary.getMood(), Diary.Mood.NORMAL);
        assertEquals(editedDiary.getImagePaths().size(), 2);
    }
    
    //Test Delete Diary Entry----------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteDiaryEntryAndRestore()
    {
        //login
        ServiceResult result = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService((String) result.getReturnObject());

        //get diary and delete it
        Diary diaryEntryToBeDeleted = diaryService.getDiaryByTitle("Edited Title");
        assertTrue(diaryService.moveEntryToBin(diaryEntryToBeDeleted).isSuccessful());

        //check if still exists
        assertNull(diaryService.getDiaryByTitle("Edited Title"));
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
            System.out.println(result.getReturnMessage());
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
