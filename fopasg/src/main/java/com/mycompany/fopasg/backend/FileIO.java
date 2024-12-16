package com.mycompany.fopasg.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileIO 
{
    //TXT file manipulation methods for user info
    //Write
    public void writeTxt(String fileName, String[] info) 
    {
        try 
        {
            FileWriter fileWriter = new FileWriter(fileName);
            for (String line : info)
            {
                fileWriter.write();
            }
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //CSV file manipulation methods for diary entries
    //Write
    public void writeCsv(String fileName, List<String[]> data) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) 
        {
            for (String[] row : data)
            {
                String line = String.join(",", row);
                writer.write(line);
                writer.newLine();
            }
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Failed to write to CSV file.", e);
        }
    }

    //Read
    public List<String[]> readCsv(String fileName)
    {
    List<String[]> data = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) 
    {
        String line;
        while ((line = reader.readLine()) != null) 
        {
            String[] row = line.split(",");
            data.add(row);
        }
    } 
    catch (IOException e)
    {
        throw new RuntimeException("Failed to read from CSV file.", e);
    }
    return data;
}

    //Edit
    public void editCsv(String fileName, String[] row) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) 
        {
            String line = String.join(",", row);
            writer.write(line);
            writer.newLine();
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Failed to edit CSV file.", e);
        }
    }

    //Delete
    public void deleteCsv(String fileName, int keyIndex, String keyValue) 
    {
        File inputFile = new File(fileName);
        File tempFile = new File("temp_" + fileName);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] row = line.split(",");
                if (row.length > keyIndex && row[keyIndex].equals(keyValue)) 
                {
                    //skip this line so when we write the new file it would be without the data that we want to delete
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Failed to delete from CSV file.", e);
        }
    
        //replace original file with the new file
        if (!inputFile.delete()) 
        {
            throw new RuntimeException("Failed to delete the original file.");
        }
        if (!tempFile.renameTo(inputFile)) 
        {
            throw new RuntimeException("Failed to rename the temporary file.");
        }
    }
}
