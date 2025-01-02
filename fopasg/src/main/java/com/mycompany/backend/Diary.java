package com.mycompany.backend;

import java.net.URISyntaxException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Diary {
    private String username;
    private String diaryId;
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;
    private List<String> imagePaths; 

    
    public Diary() {
        this.imagePaths = new ArrayList<>(); 
    }

    public Diary(String username, String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent) {
        this.imagePaths = new ArrayList<>(); 
        this.username = username;
        this.diaryId = diaryId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public String getDiaryTitle() {
        return diaryTitle;
    }

    public LocalDateTime getDiaryDate() {
        return diaryDate;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public List<String> getImagePaths() {
        return imagePaths; 
    }

    public void addImagePath(String imagePath) {
        this.imagePaths.add(imagePath); 
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    public void setDiaryDate(LocalDateTime diaryDate) {
        this.diaryDate = diaryDate;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }
}