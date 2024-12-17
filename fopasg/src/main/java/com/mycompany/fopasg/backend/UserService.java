package com.mycompany.fopasg.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UserService
{
    private FileIO fileIO = new FileIO();
    private String filename;

    //if no arg passing through constructor, default to Users.txt
    public UserService() {
        this.filename = "Users.txt";
    }

    //for testing purposes
    public UserService(String filename) {
        this.filename = filename;
    }

    //get all user
    public List<User> getAllUsers()
    {
        List<User> userList = new ArrayList<>();

        //add all users to the arraylist to be returned
        try
        {
            List<String> users = fileIO.readTxt(filename);
            for (String user : users)
            {
                String[] data = user.split(",");
                userList.add(new User(data[0], data[1], data[2]));
            }

            //done
            return userList;
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

    //get user by username
    public User getUserByUsername(String username)
    {
        List<User> users = getAllUsers();

        for (User user : users)
        {
            if (user.getUsername().equals(username))
            {
                return user;
            }
        }
        //user doesnt exists
        return null;
    }

    //login
    public boolean userLogin(String username, String password)
    {
        //read user class frm json file
        try 
        {
            List<String> userList = fileIO.readTxt(filename);

            //loop through the list
            for (String userLine : userList)
            {
                String[] userInfo = userLine.split(",");
                //check if the given username/email exists in the list
                if (userInfo[0].equals(username) || userInfo[1].equals(username))
                {
                    //then check password
                    if (userInfo[2].equals(password))
                    {
                        //login successful
                        return true;
                    }
                    //wrong password
                    else
                    {
                        System.err.println("Incorrect Password.");
                        return false;
                    }
                }
            }
            //login unsuccessful
            System.err.println("User not found.");
            return false;
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

    //sign up
    public boolean userSignUp(String username, String email, String password)
    {
        if (username == null || email == null || password == null)
        {
            System.err.println("Info incomplete.");
            return false;
        }
        else
        {
            try 
            {
                //get all user to check if user already exists
                List<String> users = fileIO.readTxt(filename);
    
                for (String userLine : users)
                {
                    String[] userInfo = userLine.split(",");
                    if (userInfo[0].equals(username) || userInfo[1].equals(email))
                    {
                        System.err.println("Username/Email already exists.");
                        return false;
                    }
                }
                
                //if no matching existing users, then we can create an account
                User newUser = new User(username, email, password);
                fileIO.appendTxt(filename, newUser);
                return true;
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

    //edit profile
    public boolean userEdit(String username, String email, String password)
    {
        if (username == null || email == null || password == null)
        {
            System.err.println("Info incomplete.");
            return false;
        }
        else
        {
            User updatedUser = new User(username, email, password);
            try 
            {
                fileIO.editTxt(filename, updatedUser, username);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
    
            return true;
        }
    }

    //delete user
    public boolean userDelete(String username)
    {
        //delete user
        try
        {
            fileIO.deleteLineTxt(filename, username);

            //done 
            return true;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        //delete all diary entries of the user (TODO)
    }
}
