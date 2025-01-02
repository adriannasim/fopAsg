package com.mycompany.backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Diary 
{
    public enum Mood 
    {
        HAPPY, SAD, NORMAL;
    }
    
    private String username;
    private String diaryId;
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;
    private Mood mood;
    private LocalDate deletionDate;
    private List<String> imagePaths;

    //constructors
    public Diary() 
    {
        this.deletionDate = null;
        this.imagePaths = new ArrayList();
    }

    public Diary(String username, String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Mood mood)
    {   
        this();
        this.username = username;
        this.diaryId = diaryId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
        this.mood = mood;
    }

    //getter
    public String getUsername()
    {
        return username;
    }

    public String getDiaryId()
    {
        return diaryId;
    }

    public String getDiaryTitle()
    {
        return diaryTitle;
    }

    public LocalDateTime getDiaryDate()
    {
        return diaryDate;
    }

    public String getDiaryContent()
    {
        return diaryContent;
    }

    public Mood getMood() 
    {
        return mood;
    }

    public LocalDate getDeletionDate() 
    {
        return deletionDate;
    }

    public List<String> getImagePaths()
    {
        return imagePaths;
    }

    //setter
    public void setDiaryTitle(String diaryTitle)
    {
        this.diaryTitle = diaryTitle;
    }

    public void setDiaryDate(LocalDateTime diaryDate)
    {
        this.diaryDate = diaryDate;
    }

    public void setDiaryContent(String diaryContent)
    {
        this.diaryContent = diaryContent;
    }

    public void setMood(Mood mood) 
    {
        this.mood = mood;
    }

    public void setDeletionDate(LocalDate deletionDate) 
    {
        this.deletionDate = deletionDate;
    }

    public void setImagePaths(List<String> imagePaths)
    {
        this.imagePaths = imagePaths;
    }

    public void addImagePaths(String imagePath)
    {
        this.imagePaths.add(imagePath);
    }

    public void removeImagePaths(String imagePath)
    {
        this.imagePaths.remove(imagePath);
    }

    //to string
    public String toString(){
        return diaryId +
            ", {" +
            "diaryTitle='" + diaryTitle + '\'' +
            ", diaryDate='" + (diaryDate != null ? diaryDate.toString() : "null") + '\'' +
            ", mood='" + mood + '\'' +
            ", deletionDate='" + (deletionDate == null ? "null" : deletionDate) + '\'' +
            ", imagePaths='" + (imagePaths.isEmpty() ? "null" : String.join(";", imagePaths)) + '\'' +
            ", diaryContent='" + diaryContent + '\'' +
            '}';
    }
}
    