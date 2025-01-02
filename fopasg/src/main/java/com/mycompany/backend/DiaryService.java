package com.mycompany.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiaryService {
    private FileIO fileIO = new FileIO();
    private String filename;

    public DiaryService(String username) {
        filename = username + ".csv";
    }

    public List<Diary> getAllDiary() throws URISyntaxException {
        List<Diary> diaryList = new ArrayList<>();
        try {
            List<String> data = fileIO.readFile(filename);
            for (String line : data) {
                String[] diaryInfo = line.split(",");
                LocalDateTime diaryDate;
                try {
                    diaryDate = LocalDateTime.parse(diaryInfo[2]);
                } catch (Exception e) {
                    diaryDate = LocalDateTime.now(); 
                }
                diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], diaryDate, diaryInfo[3]));
            }
            return diaryList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean newDiaryEntry(String diaryTitle, LocalDateTime diaryDate, String diaryContent) {
        if (diaryTitle == null || diaryDate == null || diaryContent == null) {
            System.err.println("Info incomplete.");
            return false;
        } else {
            if (!fileIO.loadFile(filename).exists()) {
                try {
                    fileIO.createFile(filename);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                fileIO.appendFile(filename, diaryTitle + "," + diaryDate + "," + diaryContent); 
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean uploadImage(String diaryId, String imagePath) {
        try {
            List<Diary> diaries = getAllDiary();
            for (Diary diary : diaries) {
                if (diary.getDiaryId().equals(diaryId)) {
                    diary.addImagePath(imagePath); 
                    List<String> diaryData = new ArrayList<>(); 
                    for (Diary d : diaries) {
                        diaryData.add(d.getDiaryId() + "," + d.getDiaryTitle() + "," + d.getDiaryDate() + "," + d.getDiaryContent() + "," + String.join(";", d.getImagePaths()));
                    }
                    fileIO.writeFile(filename, diaryData); 
                    return true;
                }
            }
            return false;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean editDiaryEntry(String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent) {
        if (diaryTitle == null || diaryDate == null || diaryContent == null) {
            System.err.println("Info incomplete.");
            return false;
        } else {
            try {
                List<Diary> diaries = getAllDiary();
                for (Diary diary : diaries) {
                    if (diary.getDiaryId().equals(diaryId)) {
                        diary.setDiaryTitle(diaryTitle);
                        diary.setDiaryDate(diaryDate);
                        diary.setDiaryContent(diaryContent);
                        List<String> diaryData = new ArrayList<>();
                        for (Diary d : diaries) {
                            diaryData.add(d.getDiaryId() + "," + d.getDiaryTitle() + "," + d.getDiaryDate() + "," + d.getDiaryContent() + "," + String.join(";", d.getImagePaths()));
                        }
                        fileIO.writeFile(filename, diaryData);
                        return true;
                    }
                }
                return false;
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean deleteDiaryEntry(String diaryId) {
        try {
            List<Diary> diaries = getAllDiary();
            diaries.removeIf(diary -> diary.getDiaryId().equals(diaryId));
            List<String> diaryData = new ArrayList<>();
            for (Diary d : diaries) {
                diaryData.add(d.getDiaryId() + "," + d.getDiaryTitle() + "," + d.getDiaryDate() + "," + d.getDiaryContent() + "," + String.join(";", d.getImagePaths()));
            }
            fileIO.writeFile(filename, diaryData);
            return true;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
