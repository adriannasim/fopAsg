package com.mycompany.fopasg;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import com.mycompany.fopasg.backend.User;
import com.mycompany.fopasg.backend.UserService;

public class UserServiceTests 
{
    static UserServiceTests test = new UserServiceTests();
    UserService userService = new UserService("TestUsers.json");
        
    public static void main(String[] args) 
    {
        System.out.println("Sign Up Test: " + (test.testUserSignUp() ? "Passed" : "Failed"));
    }

    public boolean testUserSignUp()
    {
        boolean doneSignUp = userService.userSignUp("TestUsername", "test@gmail.com", "test123");

        if (doneSignUp)
        {
            List<User> users = userService.fileIO.readFromJson(User.class);
            
            User user = users.get(0);

            if (user.getUsername().equals("TestUsername") && user.getEmail().equals("test@gmail.com") && user.getPassword().equals("test123"))
            {
                return true;
            }
        }
        //anything fails
        return false;
    }
}
