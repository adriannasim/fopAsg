package com.mycompany.backend;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class FileIO 
{
    //Create file
    public void createFile(String filename) throws IOException
    {
        File file;
        if (filename.toLowerCase().contains("test"))
        {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/test/resources/" + filename);
        }
        else
        {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/main/resources/" + filename);
        }
        System.out.println("Attempting to create file to: " + file.getAbsolutePath());
        
        file.createNewFile();
    }

    //Load file
    public File loadFile(String filename) throws URISyntaxException, FileNotFoundException
    {
        File file;
        if (filename.toLowerCase().contains("test"))
        {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/test/resources/" + filename);
        }
        else
        {
            file = new File((System.getProperty("user.dir").contains("fopasg") ? "" : "fopasg/") + "src/main/resources/" + filename);
        }

        return file;

        // ClassLoader classLoader = getClass().getClassLoader();
        // URL resource = classLoader.getResource(filename);
        // if (resource == null) 
        // {
        //     throw new FileNotFoundException("File not found: " + filename);
        // }
        // return new File(resource.toURI());
    }

    //TXT file manipulation methods
    //Read
    public List<String> readFile(String filename) throws IOException, URISyntaxException
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
    public void writeFile(String filename, List<String> lines) throws IOException, URISyntaxException
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename)))) 
        {
            for (String line : lines) 
            {
                //only insert next line if its not the first data
                if (lines.indexOf(line) != 0)
                {
                    bw.newLine();
                }
                bw.write(line);
            }
        }
    }

    //Append
    public void appendFile(String filename, Object dataToAdd) throws IOException, URISyntaxException
    {
        File file = loadFile(filename);
        //add new line of data at the end of the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) 
        {
            //if file nt empty
            if (file.length() != 0) 
            {
                bw.newLine();
            }
            bw.write(dataToAdd.toString());
        }
    }

    //Add files (TODO)


    //Edit
    //use key to find which line to replace/edit
    public void editFile(String filename, Object dataToUpdate, String key) throws IOException, URISyntaxException
    {
        int index = -1;

        //get the whole file data first
        List<String> lines = readFile(filename);

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

        writeFile(filename, lines); //rewrite entire thing back to the txt file
    }

    //Delete
    public void deleteLineFile(String filename, String key) throws IOException, URISyntaxException 
    {
        int index = -1;

        List<String> lines = readFile(filename);

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

        writeFile(filename, lines); //rewrite entire thing back to the txt file
    }

    //Clear entire file content
    public void clearFile(String filename) throws URISyntaxException, IOException
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(loadFile(filename)))) 
        {
            bw.write("");
        }
    }

    //Purge file
    public void purgeFile(String filename) throws URISyntaxException, FileNotFoundException
    {
        File file = loadFile(filename);

        if (file.delete()) 
        {
            System.out.println("File \"" + filename + "\" deleted successfully.");
        } 
        else 
        {
            System.out.println("Failed to delete file \""+ filename + "\". File may not exist or is in use.");
        }
    }
}