import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import backend.UserService;

public class UserServiceTests 
{
    static UserServiceTests test = new UserServiceTests();
    UserService userService;
        
    public static void main(String[] args) 
    {
        //System.out.println("Sign Up Test: " + (test.testUserSignUp() ? "Passed" : "Failed"));
        System.out.println("Login Test: " + (test.testUserLoginWithUsername() ? "Passed" : "Failed"));
    }

    // public boolean testUserSignUp()
    // {
    //     boolean doneSignUp = userService.userSignUp("TestUsername", "test@gmail.com", "test123");

    //     if (doneSignUp)
    //     {
    //         List<User> users = userService.fileIO.readFromJson(User.class);
            
    //         User user = users.get(0);

    //         if (user.getUsername().equals("TestUsername") && user.getEmail().equals("test@gmail.com") && user.getPassword().equals("test123"))
    //         {
    //             return true;
    //         }
    //     }
    //     //anything fails
    //     return false;
    // }

    public boolean testUserLoginWithUsername()
    {
        try 
        {
            boolean doneSignUp = userService.userLogin("TestUsername", "test123");
            return doneSignUp;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error login.", e);
        }
    }
}
