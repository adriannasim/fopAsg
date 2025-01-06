package com.mycompany.backend;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.io.Files;



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
                if (!images.isEmpty())
                {
                    diary.setImagePaths(addOrRemovePic(images, diary.getDiaryId()));
                }
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

    
    // Export diary entries within a range to PDF
    public ServiceResult exportDiaryToPDF(LocalDateTime startDate, LocalDateTime endDate, String pdfFilename, String rangeType) {
    try {
        // Fetch all diary entries
        List<Diary> diaryList = getAllDiary();

        LocalDateTime now = LocalDateTime.now();

        switch (rangeType.toLowerCase()) {
            case "week":
                startDate = now.minusDays(7);
                endDate = now;
                break;
            case "month":
                startDate = now.minusMonths(1);
                endDate = now;
                break;
            case "day":
                break;
            default:
                return new ServiceResult(false, null, "Invalid rangeType. Please choose 'week', 'month', 'day', or 'custom'.");
        }


            // Filter entries by date range
            List<String> entriesInRange = new ArrayList<>();
            for (Diary diary : diaryList) {
                if (!diary.getDiaryDate().isBefore(startDate) && !diary.getDiaryDate().isAfter(endDate)) {
                    // Format each diary entry for PDF export
                    entriesInRange.add(formatDiaryEntryForExport(diary));
                }
            }

            if (entriesInRange.isEmpty()) {
                return new ServiceResult(false, null, "No diary entries found in the specified date range.");
            }

            // Export filtered entries to PDF using FileIO
            fileIO.exportToPDFUsingPDFBox(pdfFilename, entriesInRange); // Replace with `exportToPDFUsingIText` if preferred
      
            return new ServiceResult(true, null, "Diary entries exported to PDF successfully.");
        } catch (Exception e) {
            return new ServiceResult(false, null, "Error exporting diary entries to PDF: " + e.getMessage());
        }
    }

    // Helper method to format a diary entry for PDF export
    private String formatDiaryEntryForExport(Diary diary) {
        return "Title: " + diary.getDiaryTitle() + "\n" +
              "Date: " + diary.getDiaryDate() + "\n" +
              "Content:\n" + diary.getDiaryContent() + "\n" +
              "----------------------------------------";
    }   
    

  
    //image methods
    public List<String> addOrRemovePic(List<File> newImages, String diaryId)
    {
        List<String> imagePaths = new ArrayList<>();
        try 
        {
            //get existing images in user folder
            List<File> existingImages = fileIO.loadFiles(imageFolder, diaryId);
    
            //check existing images and updated images by comparing image hashes (to see if user removed any images)
            for (File existingImage : existingImages)
            {
                for (File newImage : newImages)
                {
                    //if the incoming image is already in the existing image list (means user didnt remove it)
                    if (Files.asByteSource(existingImage).contentEquals(Files.asByteSource(newImage)))
                    {
                        break; //break out of the inner loop to continue checking if other existing images have been deleted or not
                    }
                }
                //if no matches (means user deleted it), then delete it from folder
                fileIO.purgeFile(existingImage.getAbsolutePath());
            }
    
            //rename all the image to diaryId + index and save it into user folder
            for (File newImage : newImages)
            {
                imagePaths.add(diaryId + "-" + (newImages.indexOf(newImage) + 1) + ".jpg");
                fileIO.addFile(imageFolder, newImage, diaryId + "-" + (newImages.indexOf(newImage) + 1), "jpg");
            }

            //lastly return the list of imagePaths
            return imagePaths;
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
