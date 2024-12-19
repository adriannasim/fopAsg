package com.mycompany.backend;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FileIO 
{
    //Load file
    public File loadFile(String filename)
    {
        File file;
        if (filename.toLowerCase().contains("test"))
        {
            file = new File("src/test/resources/" + filename);
        }
        else
        {
            file = new File("src/main/resources/" + filename);
        }
        return file;

        // URL resource = getClass().getClassLoader().getResource(filename);
        // if (resource == null)
        // {
        //     throw new RuntimeException("File not found: " + filename);
        // }
        //try 
        //{
            //return new File(resource.toURI());
        //} 
        // catch (URISyntaxException e) {
        //     throw new RuntimeException(e);
        // }
    }

    //TXT file manipulation methods
    /* Asssuming we are saving user as: "username,email,password" in the txt file */
    //Read
    public List<String> readTxt(String filename) throws IOException, URISyntaxException
    {
        List<String> dataArr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(loadFile(filename)))) 
        {
            String data;
            while ((data = br.readLine()) != null) 
            {
                dataArr.add(data);
            }
        }
        return dataArr;
    }

    //Write
    public void writeTxt(String filename, List<String> lines) throws IOException 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename)))) 
        {
            for (String line : lines) 
            {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    //Append
    public void appendTxt(String filename, Object dataToAdd) throws IOException 
    {
        //add new line of data at the end of the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename), true))) 
        {
            bw.write(dataToAdd.toString());
            bw.newLine();
        }
    }

    //Edit
    //use key to find which line to replace/edit
    public void editTxt(String filename, Object dataToUpdate, String key) throws IOException, URISyntaxException
    {
        int index = -1;

        //get the whole file data first
        List<String> lines = readTxt(filename);

        //find which line to update
        for (String line : lines)
        {
            String[] data = line.split(",");
            if (data[0].equals(key))
            {
                index = lines.indexOf(line);
            }
        }

        if (index == -1 || index > lines.size()) 
        {
            throw new IllegalArgumentException("Invalid line " + index);
        } 

        lines.set(index, dataToUpdate.toString()); //replace the data at the specified line with the new data

        writeTxt(filename, lines); //rewrite entire thing back to the txt file
    }

    //Delete
    public void deleteLineTxt(String filename, String key) throws IOException, URISyntaxException 
    {
        int index = -1;

        List<String> lines = readTxt(filename);

        //find which line to update
        for (String line : lines)
        {
            String[] data = line.split(",");
            if (data[0].equals(key))
            {
                index = lines.indexOf(line);
            }
        }

        if (index == -1 || index > lines.size()) 
        {
            throw new IllegalArgumentException("Invalid line.");
        }

        lines.remove(index); //remove the line

        writeTxt(filename, lines); //rewrite entire thing back to the txt file
    }

    //CSV file manipulation methods
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
