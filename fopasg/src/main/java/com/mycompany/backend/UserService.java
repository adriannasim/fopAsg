package com.mycompany.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.util.password.StrongPasswordEncryptor;


public class UserService
{
    private String filename;
    private FileIO fileIO = new FileIO();
    StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

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
            List<String> users = fileIO.readFile(filename);
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
    public ServiceResult userLogin(String loginInfo, String password)
    {
        //read user class frm json file
        try 
        {
            List<String> userList = fileIO.readFile(filename);

            //loop through the list
            for (String userLine : userList)
            {
                String[] userInfo = userLine.split(",");
                //check if the given username/email exists in the list
                if (userInfo[0].equals(loginInfo) || userInfo[1].equals(loginInfo))
                {
                    //then check password
                    if (passwordEncryptor.checkPassword(password, userInfo[2]))
                    {
                        //check if user has a csv file, if not create one
                        if (!fileIO.loadFile(userInfo[0] + ".csv").exists())
                        {
                            fileIO.createFile(userInfo[0] + ".csv");
                        }

                        //login successful
                        return new ServiceResult(true, userInfo[0], "Logged in successfully. Welcome " + userInfo[0] + ".");
                    }
                    //wrong password
                    else
                    {
                        return new ServiceResult(false, null, "Incorrect Password. Please try again.");
                    }
                }
            }
            //login unsuccessful
            return new ServiceResult(false, null, "User not found. Please sign up as a new user.");
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
    public ServiceResult userSignUp(String username, String email, String password)
    {
        if (username == null || email == null || password == null)
        {
            return new ServiceResult(false, null, "Info incomplete. Please fill in all of your info.");
        }
        else
        {
            try 
            {
                //get all user to check if user already exists
                List<String> users = fileIO.readFile(filename);
    
                for (String userLine : users)
                {
                    String[] userInfo = userLine.split(",");
                    if (userInfo[0].equals(username) || userInfo[1].equals(email))
                    {
                        return new ServiceResult(false, null, "Username/Email already exists. Please try again.");
                    }
                }
                
                //if no matching existing users, then we can create an account
                User newUser = new User(username, email, passwordEncryptor.encryptPassword(password));
                fileIO.appendFile(filename, newUser);
                fileIO.createFile(username + ".csv");
                return new ServiceResult(true, null, "Account created successfully. Please login.");
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
    public ServiceResult userEdit(String username, String email, String password)
    {
        if (username == null || email == null)
        {
            return new ServiceResult(false, null, "Info incomplete. Please fill in all of your info.");
        }
        else
        {
            //if use change password method
            if (password == null)
            {
                password = getUserByUsername(username).getPassword();
            }

            User updatedUser = new User(username, email, password);
            try 
            {
                fileIO.editFile(filename, updatedUser, username);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
    
            return new ServiceResult(true, null, "Your changes has been saved.");
        }
    }

    //change password
    public ServiceResult changePassword(String username, String oldPassword, String newPassword)
    {
        if (username == null || oldPassword == null || newPassword == null)
        {
            return new ServiceResult(false, null, "Info incomplete. Please fill in all of your info.");
        }
        //get user info
        User user = getUserByUsername(username);

        //if old password is not the same as the one user entered
        if (!passwordEncryptor.checkPassword(oldPassword, user.getPassword()))
        {
            return new ServiceResult(false, null, "Old password is incorrect. Please try again.");
        }
        //if old password = new password
        else if (passwordEncryptor.checkPassword(oldPassword, user.getPassword()) && passwordEncryptor.checkPassword(newPassword, user.getPassword()))
        {
            return new ServiceResult(false, null, "New password cannot be the same as the old password. Please try again.");
        }
        //if everything else is ok
        else if (passwordEncryptor.checkPassword(oldPassword, user.getPassword()) && !passwordEncryptor.checkPassword(newPassword, user.getPassword()))
        {
            User updatedUser = new User(username, user.getEmail(), passwordEncryptor.encryptPassword(newPassword));
            try 
            {
                fileIO.editFile(filename, updatedUser, username);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            catch (URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
    
            return new ServiceResult(true, null, "Password changed successfully. Please login again.");
        }
        else
        {
            return new ServiceResult(false, null, "An error occured. Please try again.");
        }
    }

    //delete user
    public ServiceResult userDelete(String username)
    {
        try
        {
            //delete user
            fileIO.deleteLineFile(filename, username);

            //then delete all diary entries of the user
            fileIO.purgeFile(username + ".csv");

            //delete all images of the user
            fileIO.purgeFolder("images/" + username);

            //done 
            return new ServiceResult(true, null, "Account deleted successfully.");
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
