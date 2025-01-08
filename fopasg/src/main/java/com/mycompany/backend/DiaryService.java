package com.mycompany.backend;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gluonhq.richtextarea.model.TextDecoration;
import com.google.common.io.Files;

import com.mycompany.frontend.RichTextCSVExporter;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class DiaryService
{
    private FileIO fileIO = new FileIO();
    private String filename;
    private String imageFolder;

    public DiaryService(String username)
    {
        //diary entries file will be saved as username
        filename = username + ".csv";
        //user image folder
        imageFolder = "images/" + username;
    }

    //get all diary entries for the user
    public List<Diary> getAllDiary()
    {   
        Diary diaryToBeAdded;
        List<Diary> diaryList = new ArrayList<>();
        try 
        {
            if (!fileIO.loadFile(filename).exists())
            {
                fileIO.createFile(filename);   
            }
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                // String[] diaryInfo = line.split(",");
                // if (diaryInfo[5].equals("null"))
                // {
                //     // diaryList.add(new Diary(filename, diaryInfo[0], diaryInfo[1], LocalDate.parse(diaryInfo[2]), diaryInfo[3], Diary.Mood.valueOf(diaryInfo[4])));
                // }
                diaryToBeAdded = parseDiary(line, filename);
                if (diaryToBeAdded.getDeletionDate() == null)
                {
                    diaryList.add(diaryToBeAdded);
                }
            }

            //done
            return diaryList;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //get diaries in recycle bin
    public List<Diary> getAllDiariesInBin()
    {   
        Diary diaryToBeAdded;
        List<Diary> diaryList = new ArrayList<>();
        try 
        {
            if (!fileIO.loadFile(filename).exists())
            {
                    fileIO.createFile(filename);   
            }
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                diaryToBeAdded = parseDiary(line, filename);
                if (diaryToBeAdded.getDeletionDate() != null)
                {
                    diaryList.add(diaryToBeAdded);
                }
            }

            //done
            return diaryList;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Method to parse the diary line from csv file into diary object
    public static Diary parseDiary(String diaryString, String filename) {
        // Extract the diaryId
        String diaryId = diaryString.substring(0, 36);

        // Remove the "{" prefix and "}" suffix
        String cleanString = diaryString.substring(36, diaryString.length() - 1);

        // Split fields by format (, xxx='x')
        String[] fields = cleanString.split(", (?=\\w+='.*')");

        // Extract fields
        String diaryTitle = fields[0].split("=")[1].replace("'", "").trim();
        String diaryDateStr = fields[1].split("=")[1].replace("'", "").trim();
        String mood = fields[2].split("=")[1].replace("'", "").trim();
        String deletionDateStr = fields[3].split("=")[1].replace("'", "").trim();
        
        //images
        List<String> imagePaths = new ArrayList<>();
        //loop to save images
        for (String imagePath : fields[4].split("=")[1].replace("'","").trim().split(";"))
        {
            imagePaths.add(imagePath);
        }

        String diaryContent = fields[5].split("=")[1].replaceFirst("'", "").replaceAll("'$", "").trim();

        // Parse diaryDate
        LocalDateTime diaryDate = null;

        try {
            diaryDate = LocalDateTime.parse(diaryDateStr);

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a new Diary object
        Diary diary = new Diary(filename, diaryId, diaryTitle, diaryDate, diaryContent, Diary.Mood.valueOf(mood));
        diary.setDeletionDate(deletionDateStr.equals("null") ? null : LocalDate.parse(deletionDateStr));
        diary.setImagePaths(imagePaths);

        return diary;
    }

    //get diary by title
    public Diary getDiaryByTitle(String diaryTitle)
    {
        List<Diary> diaries = getAllDiary();

        for (Diary diary : diaries)
        {
            if (diary.getDiaryTitle().equals(diaryTitle))
            {
                return diary;
            }
        }
        //diary doesnt exists
        return null;
    }

    //get a list of diary by search matches (Search)
    public List<Diary> searchDiariesByTitle(String searchInput)
    {
        List<Diary> diaries = getAllDiary();
        List<Diary> searchResult = new ArrayList<>();

        for (Diary diary : diaries)
        {
            if (diary.getDiaryTitle().contains(searchInput))
            {
                searchResult.add(diary);
            }
        }

        //return list of results
        return searchResult;
    }

    //create diary
    public ServiceResult newDiaryEntry(String diaryTitle, LocalDateTime diaryDate, String diaryContent, Diary.Mood mood, List<File> images)
    {
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
        {
            return new ServiceResult(false, null, "Info incomplete.");
        }
        else
        {
            try 
            {
                //check if the file exists, if not, it means that its the user's first entry so create a new file for the user
                if (!fileIO.loadFile(filename).exists())
                {
                    fileIO.createFile(filename);   
                }

                //check if user's image folder exists, if not, create one
                if (!fileIO.loadFile(imageFolder).exists())
                {
                    fileIO.createFolder(imageFolder);  
                }

                Diary diary = new Diary(filename, UUID.randomUUID().toString(), diaryTitle, diaryDate, diaryContent, mood);
                //add images if imagePaths is not empty
                if (!images.isEmpty())
                {
                    diary.setImagePaths(addOrRemovePic(images, diary.getDiaryId()));
                }
                fileIO.appendFile(filename, diary);
                
                //done 
                return new ServiceResult(true, null, "Diary entry created.");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e) 
            {
                throw new RuntimeException(e);
            }
        }
    }

    //edit diary
    public ServiceResult editDiaryEntry(String diaryId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, Diary.Mood mood, List<File> images)
    {
        
        if (diaryTitle == null || diaryDate == null || diaryContent == null || mood == null)
        {
            return new ServiceResult(false, null, "Info incomplete.");
        }
        else
        {
            try 
            {
                Diary diary = new Diary(filename, diaryId, diaryTitle, diaryDate, diaryContent, mood);
                //add images if imagePaths is not empty
                // if (!images.isEmpty())
                // {
                    diary.setImagePaths(addOrRemovePic(images, diary.getDiaryId()));
                // }
                fileIO.editFile(filename, diary, diaryId);
                
                //done
                return new ServiceResult(true, null, "Diary entry edited.");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    //move diary to bin
    public ServiceResult moveEntryToBin(Diary diaryEntryToBeDeleted)
    {
        try 
        {
            //set deletion date to now
            diaryEntryToBeDeleted.setDeletionDate(LocalDate.now());

            fileIO.editFile(filename, diaryEntryToBeDeleted, diaryEntryToBeDeleted.getDiaryId());
            
            //done 
            return new ServiceResult(true, null, "Diary entry deleted successfully.");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //restore diary from recycle bin
    public ServiceResult restoreDiaryEntry(Diary diaryEntryToBeRestored)
    {
        try 
        {
            diaryEntryToBeRestored.setDeletionDate(null);

            fileIO.editFile(filename, diaryEntryToBeRestored, diaryEntryToBeRestored.getDiaryId());
            
            //done 
            return new ServiceResult(true, null, "Diary entry restored successfully.");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //delete diary entry permanently
    public ServiceResult deleteDiaryEntry(String diaryId)
    {
        try 
        {
            fileIO.deleteLineFile(filename, diaryId);
            
            //done 
            return new ServiceResult(true, null, "Diary entry permanently deleted successfully.");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //clear diary entries that have been in the bin for 30 days (Run when app starts)
    public void clearOldDiaryEntry(String diaryId)
    {
        try 
        {
            List<String> data = fileIO.readFile(filename);

            for (String line : data)
            {
                // String[] diaryInfo = line.split(",");
                // if (!(diaryInfo[5].equals("null")) && LocalDate.parse(diaryInfo[5]).until(LocalDate.now(), ChronoUnit.DAYS) >= 30) //if more or equal than 30
                // {
                //     fileIO.deleteLineFile(filename, diaryInfo[0]);
                // }
                if (parseDiary(line, filename).getDeletionDate() != null && parseDiary(line, filename).getDeletionDate().until(LocalDate.now(), ChronoUnit.DAYS) >= 30) //if more or equal than 30
                {
                    fileIO.deleteLineFile(filename, parseDiary(line, filename).getDiaryId());
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    //Export diary to pdf
    public ServiceResult exportDiaryToPDF(List<Diary> diaries, String pdfFilename)
    {
        List<String> entries = new ArrayList<>();
        //format diary
        try
        {
            for (Diary diary : diaries)
            {
                entries.addAll(formatDiaryEntryForExport(diary));
            }

            if (!entries.isEmpty())
            {
                fileIO.exportToPDFUsingPDFBox(pdfFilename + ".pdf", entries);
                return new ServiceResult(true, null, "Diary entries exported to PDF successfully.");
            }
            else
            {
                return new ServiceResult(false, null, "Failed to export diary entries to PDF.");
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    // Export diary entries within a date range to PDF
    public ServiceResult exportDiaryToPDF(LocalDate startDate, LocalDate endDate, String pdfFilename) {
        List<Diary> filteredDiaries = new ArrayList<>();
        try {
            //Fetch all diary entries
            List<Diary> diaryList = getAllDiary();
            
            // Filter entries by date range
            for (Diary diary : diaryList) {
                if (diary.getDiaryDate().toLocalDate().isAfter(startDate) && diary.getDiaryDate().toLocalDate().isBefore(endDate)) {
                    filteredDiaries.add(diary);
                }
            }

            if (!diaryList.isEmpty())
            {
                return exportDiaryToPDF(filteredDiaries, pdfFilename);
            }
            else
            {
                return new ServiceResult(false, null, "No diary entries found within the specified date range.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //export diary entries by month and year
    public ServiceResult exportDiaryToPDF(Month month, int year, String pdfFilename) 
    {
        List<Diary> filteredDiaries = new ArrayList<>();
        try 
        {
            // Fetch all diary entries
            List<Diary> diaryList = getAllDiary();

            // Filter entries
            for (Diary diary : diaryList) 
            {
                if (diary.getDiaryDate().getMonth() == month  && diary.getDiaryDate().getYear() == year) 
                {
                    filteredDiaries.add(diary);
                }
            }
            if (!diaryList.isEmpty())
            {
                return exportDiaryToPDF(filteredDiaries, pdfFilename);
            }
            else
            {
                return new ServiceResult(false, null, "No diary entries found within the specified month.");
            }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
    }

    //export diary entries by start date and weeks
    public ServiceResult exportDiaryToPDF(LocalDate startDate, int weeks, String pdfFilename) 
    {
        List<Diary> filteredDiaries = new ArrayList<>();
        try 
        {
            // Fetch all diary entries
            List<Diary> diaryList = getAllDiary();

            // Filter entries
            for (Diary diary : diaryList) 
            {
                if (diary.getDiaryDate().toLocalDate().isAfter(startDate) && diary.getDiaryDate().toLocalDate().isBefore(startDate.plusWeeks(weeks))) 
                {
                    filteredDiaries.add(diary);
                }
            }
            if (!diaryList.isEmpty())
            {
                return exportDiaryToPDF(filteredDiaries, pdfFilename);
            }
            else
            {
                return new ServiceResult(false, null, "No diary entries found within the specified week range.");
            }
        } 
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    //format diary for export
    private List<String> formatDiaryEntryForExport(Diary diary) throws IOException{
        List<String> formattedEntry = new ArrayList<>();
        formattedEntry.add("Title: " + diary.getDiaryTitle());
        formattedEntry.add("Date: " + diary.getDiaryDate().toLocalDate());
        formattedEntry.add("Mood: " + diary.getMood());
        formattedEntry.add("Content: " + RichTextCSVExporter.importFromCSV(diary.getDiaryContent()).getText());
        formattedEntry.add("----------------------------------------");
        return formattedEntry;
    }
  
    //image methods
    // public List<String> addOrRemovePic(List<File> newImages, String diaryId)
    // {
    //     List<String> imagePaths = new ArrayList<>();
    //     List<File> existing = new ArrayList<>();
        
    //     try 
    //     {
    //         //get existing images in user folder
    //         List<File> existingImages = fileIO.loadFiles(imageFolder, diaryId);
    
    //         //check existing images and updated images by comparing image hashes (to see if user removed any images)
    //         for (File existingImage : existingImages)
    //         {
    //             boolean matched = false;
    //             for (File newImage : newImages)
    //             {
    //                 //if the incoming image is already in the existing image list (means user didnt remove it)
    //                 if (Files.asByteSource(existingImage).contentEquals(Files.asByteSource(newImage)))
    //                 {
    //                     existing.add(existingImage);
    //                     matched = true;
    //                     break; //break out of the inner loop to continue checking if other existing images have been deleted or not
    //                 }
    //             }
    //             if(!matched){
    //                 //if no matches (means user deleted it), then delete it from folder
    //                 fileIO.purgeFileByFullPath(existingImage.getAbsolutePath());
    //             }
                
    //         }
    
    //         //rename all the image to diaryId + index and save it into user folder
    //         for (File newImage : newImages)
    //         {
    //             boolean isAlreadyAdded = false;

    //             // Check if this image already exists (by comparing byte content with already added files)
    //             for (File e : existing)
    //             {
    //                 if (Files.asByteSource(e).contentEquals(Files.asByteSource(newImage)))
    //                 {
    //                     isAlreadyAdded = true;
    //                     break; // Skip adding this image if it's already in the 'existing' list
    //                 }
    //             }

    //             // Use the next available index for new images
    //             String username = filename.replaceFirst("[.][^.]+$", "");
    //             int index = newImages.indexOf(newImage) + 1; // Using index of newImage for unique naming
    //             String imagePath = "src/main/resources/images/" + username + "/" + diaryId + "-" + index + ".jpg";

    //             // imagePaths.add(diaryId + "-" + (newImages.indexOf(newImage) + 1) + ".jpg");

    //             // Add the image path to the list
    //             imagePaths.add(imagePath);

    //             // If the image hasn't been added before, add it now
    //             if (!isAlreadyAdded)
    //             {
    //                 // Add the new image to the folder
    //                 fileIO.addFile(imageFolder, newImage, diaryId + "-" + index, "jpg");  
    //                 // fileIO.addFile(imageFolder, newImage, diaryId + "-" + (newImages.indexOf(newImage) + 1), "jpg");
            
    //             }
                
    //         }

    //         //lastly return the list of imagePaths
    //         return imagePaths;
    //     }
    //     catch (IOException e)
    //     {
    //         throw new RuntimeException(e);
    //     }
    //     catch (URISyntaxException e)
    //     {
    //         throw new RuntimeException(e);
    //     }
    // }

    public List<String> addOrRemovePic(List<File> newImages, String diaryId) {
        
        List<String> imagePaths = new ArrayList<>();
        List<File> existing = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
            
        try {
            // Get existing images in user folder
            List<File> existingImages = fileIO.loadFiles(imageFolder, diaryId);

            // Delete all existing files if current newImages is empty
            if (newImages == null || newImages.isEmpty()) {
                for (File existingImage : existingImages) {
                    fileIO.purgeFileByFullPath(existingImage.getAbsolutePath());
                }
                return imagePaths; // Return empty list since all images were deleted
            }
            
            // First pass: Mark which existing images to keep
            for (File existingImage : existingImages) {
                boolean matched = false;
                for (File newImage : newImages) {
                    if (Files.asByteSource(existingImage).contentEquals(Files.asByteSource(newImage))) {
                        existing.add(existingImage);
                        matched = true;
                        String fileName = existingImage.getName();
                        int currentIndex = Integer.parseInt(fileName.substring(fileName.lastIndexOf('-') + 1, fileName.lastIndexOf('.')));
                        indexes.add(currentIndex);
                        break;
                    }
                }
                if (!matched) {
                    // Delete file if it's not in new images
                    fileIO.purgeFileByFullPath(existingImage.getAbsolutePath());
                }
            }
    
            // Second pass: Add new images, reusing existing indices where possible
            int newIndex = 1; // Start from 1 if no existing images found
    
            for (File newImage : newImages) {
                boolean isAlreadyAdded = false;
                String currentIndex = null;
    
                // Check if image already exists
                for (File e : existing) {
                    if (Files.asByteSource(e).contentEquals(Files.asByteSource(newImage))) {
                        // Extract index from existing file name
                        String fileName = e.getName();
                        currentIndex = fileName.substring(fileName.lastIndexOf('-') + 1, fileName.lastIndexOf('.'));
                        isAlreadyAdded = true;
                        break;
                    }
                }

                Collections.sort(indexes); 
                for (int index:indexes){
                    if (newIndex == index){
                        newIndex++;
                    } else {
                        break;
                    }
                }
    
                String username = filename.replaceFirst("[.][^.]+$", "");
                String index = isAlreadyAdded ? currentIndex : String.valueOf(newIndex);
                String imagePath = "src/main/resources/images/" + username + "/" + diaryId + "-" + index + ".jpg";
                
                imagePaths.add(imagePath);
    
                if (!isAlreadyAdded) {
                    fileIO.addFile(imageFolder, newImage, diaryId + "-" + index, "jpg");
                    newIndex++;
                }
            }
    
            return imagePaths;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
  
    //Mood Tracker
    public int[] getMoodByDate(LocalDate startDate, LocalDate endDate)
    {
        //index: 0 = happy, 1 = normal, 2 = sad
        int happy = 0, normal = 0, sad = 0;
        List<Diary> diaries = getAllDiary();
        for (Diary diary : diaries)
        {
            if (diary.getDiaryDate().toLocalDate().isAfter(startDate) && diary.getDiaryDate().toLocalDate().isBefore(endDate))
            {
                switch(diary.getMood())
                {
                    case HAPPY:
                        happy++;
                        break;
                    case NORMAL:
                        normal++;
                        break;
                    case SAD:
                        sad++;
                        break;
                }
            }
        }

        return new int[] {happy, normal, sad};
    }
}
