package com.mycompany.fopasg.backend;

import java.io.IOException;
import java.util.List;

import com.mycompany.fopasg.backend.FileIO;

public class UserService
{
    public FileIO fileIO;

    //if no arg passing through constructor, default to Users.json
    public UserService() {
        this.fileIO = new FileIO("Users.json");
    }

    //for testing purposes
    public UserService(String fileName) {
        this.fileIO = new FileIO(fileName);
    }

    //incase we are using it for something
    // //get all user
    // public List<User> getAllUsers()
    // {
    //     return (List<User>) fileIO.readFromJson(fileName, List.class);
    // }

    // //get user by username
    // public User getUserByUsername(String username)
    // {
    //     for (User user : getAllUsers())
    //     {
    //         //if found user using username
    //         if (user.getUsername().equals(username))
    //         {
    //             return user;
    //         }
    //     }
    //     //user doesnt exists
    //     return null;
    // }

    //login
    public boolean userLogin(String username, String password)
    {
        //read user class frm json file
        List<User> users = fileIO.readFromJson(List.class);

        //loop through the list
        for (User user : users)
        {
            //check if the given username/email exists in the list
            if (user.getUsername().equals(username) || user.getEmail().equals(username))
            {
                //then check password
                if (user.getPassword().equals(password))
                {
                    //login successful
                    return true;
                }
                //wrong password
                else
                {
                    System.err.println("Incorrect Password.");
                    break;
                }
            }
        }
        //login unsuccessful
        System.err.println("User not found.");
        return false;
    }

    //sign up
    public boolean userSignUp(String username, String email, String password)
    {
        User newUser = new User(username, email, password);
        return fileIO.appendToJson(newUser, newUser.getClass());
    }

    //edit profile
    public boolean userEdit(String username, String email, String password)
    {
        User updatedUser = new User(username, email, password);
        return fileIO.editJson(updatedUser, username, updatedUser.getClass());
    }

    //delete user
    public boolean userDelete(String username)
    {
        //delete user
        boolean userDeleted = fileIO.deleteJson(username, User.class);

        //delete all diary entries of the user (tbc)

        if (userDeleted)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
