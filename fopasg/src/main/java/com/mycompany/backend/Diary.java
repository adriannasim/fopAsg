package com.mycompany.backend;

import java.time.LocalDateTime;

public class Diary 
{
    private String username;
    private String diaryId;
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;

    //constructors
    public Diary() {}

    public Diary(String username, String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent)
    {
        this.username = username;
        this.diaryId = diaryId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
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

    public String toString(){
        return diaryId +
            ", {" +
            "username='" + username + '\'' +
            ", diaryId='" + diaryId + '\'' +
            ", diaryTitle='" + diaryTitle + '\'' +
            ", diaryDate='" + (diaryDate != null ? diaryDate.toString() : "null") + '\'' +
            ", diaryContent='" + diaryContent + '\'' +
            '}';
    }
}
