package com.mycompany.backend;

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List; 

import org.junit.*;

public class DiaryServiceTests {
    String userFile = "TestUsers.txt";
    UserService userService = new UserService(userFile);
    FileIO fileIO = new FileIO();

    @Before
    public void setup() {
        userService.userSignUp("TestUser1", "test1@gmail.com", "test123");
    }

    @After
    public void cleanUp() {
        fileIO.purgeFile(userFile);
        fileIO.purgeFile("TestUser1.csv");
    }

    @Test
    public void testNewDiaryEntry() throws URISyntaxException {
        String loginUser = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService(loginUser);
        assertTrue(diaryService.newDiaryEntry("Test Diary Title", LocalDateTime.now(), "Today I am Happy."));
    }

    @Test
    public void testUploadImage() throws URISyntaxException {
        String loginUser = userService.userLogin("TestUser1", "test123");
        DiaryService diaryService = new DiaryService(loginUser);
        diaryService.newDiaryEntry("Test Diary Title", LocalDateTime.now(), "Today I am Happy.");
        List<Diary> diaries = diaryService.getAllDiary();
        String diaryId = diaries.get(0).getDiaryId();
        assertTrue(diaryService.uploadImage(diaryId, "path/to/image.jpg")); 
    }

    @Test
    public void testEditDiaryEntry() {
        
    }

    @Test
    public void testDeleteDiaryEntry() {
        
    }
}
