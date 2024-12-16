package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileIO 
{
    //TXT file manipulation methods
    /* Asssuming we are saving user as: "username, email, password"  */
    //Read
    public List<String> readTxt(String filePath) throws IOException 
    {
        List<String> dataArr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) 
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
    public void writeTxt(String filePath, List<String> lines) throws IOException 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) 
        {
            for (String line : lines) 
            {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    //Append
    public void appendTxt(String filePath, String data) throws IOException 
    {
        //add new line of data at the end of the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) 
        {
            bw.write(data);
            bw.newLine();
            }
    }

    //Edit
    public void editTxt(String filePath, String data, int lineNo) throws IOException 
    {
        //get the whole file data first
        List<String> lines = readTxt(filePath);
        if (lineNo < 1 || lineNo > lines.size()) 
        {
            throw new IllegalArgumentException("Invalid line.");
        }

        lines.set(lineNo, data); //replace the data at the specified line with the new data

        writeTxt(filePath, lines); //rewrite entire thing back to the txt file

    }

    //Delete
    public void deleteLineTxt(String filePath, int lineNo) throws IOException 
    {
        List<String> lines = readTxt(filePath);
        if (lineNo < 1 || lineNo > lines.size()) 
        {
            throw new IllegalArgumentException("Invalid line.");
        }

        lines.remove(lineNo - 1); //remove the line

        
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
