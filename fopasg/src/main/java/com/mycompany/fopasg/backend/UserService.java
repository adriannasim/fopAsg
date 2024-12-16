package backend;

import java.io.IOException;
import java.util.List;

public class UserService
{
    private FileIO fileIO;
    private String filename;

    //if no arg passing through constructor, default to Users.json
    public UserService() {
        this.filename = "User.txt";
    }

    //for testing purposes
    public UserService(String filename) {
        this.filename = filename;
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
    public boolean userLogin(String username, String password) throws IOException
    {
        //read user class frm json file
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
                    break;
                }
            }
        }
        //login unsuccessful
        System.err.println("User not found.");
        return false;
    }

    // //sign up
    // public boolean userSignUp(String username, String email, String password)
    // {
    //     User newUser = new User(username, email, password);
    //     return fileIO.appendToJson(newUser, newUser.getClass());
    // }

    // //edit profile
    // public boolean userEdit(String username, String email, String password)
    // {
    //     User updatedUser = new User(username, email, password);
    //     return fileIO.editJson(updatedUser, username, updatedUser.getClass());
    // }

    // //delete user
    // public boolean userDelete(String username)
    // {
    //     //delete user
    //     boolean userDeleted = fileIO.deleteJson(username, User.class);

    //     //delete all diary entries of the user (tbc)

    //     if (userDeleted)
    //     {
    //         return true;
    //     }
    //     else
    //     {
    //         return false;
    //     }
    // }
}
