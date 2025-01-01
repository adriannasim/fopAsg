package com.mycompany.backend;

import java.time.LocalDateTime;

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
    private LocalDateTime deletionDate;

    //constructors
    public Diary() {}

    //not deleted
    public Diary(String username, String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Mood mood)
    {   
        this.username = username;
        this.diaryId = diaryId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
        this.mood = mood;
        this.deletionDate = null;
    }

    //deleted
    public Diary(String username, String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Mood mood, LocalDateTime deletionDate)
    {   
        this(username, diaryId, diaryTitle, diaryDate, diaryContent, mood);
        this.deletionDate = deletionDate;
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

    public LocalDateTime getDeletionDate() 
    {
        return deletionDate;
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

    public void setDeletionDate(LocalDateTime deletionDate) 
    {
        this.deletionDate = deletionDate;
    }

    //to string
    public String toString()
    {
        return diaryId + "," + diaryTitle + "," + diaryDate + "," + diaryContent + "," + mood + "," + (deletionDate == null ? "null" : deletionDate);
    }
}
    