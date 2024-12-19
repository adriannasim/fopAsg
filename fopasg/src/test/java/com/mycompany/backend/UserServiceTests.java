package com.mycompany.backend;

import org.junit.*;

public class UserServiceTests 
{
    static UserServiceTests test = new UserServiceTests();
    UserService userService = new UserService("TestUsers.txt");
    FileIO fileIO = new FileIO();
         
    @Test
    public void main() 
    {
        //Tests output----------------------------------------------------------------------------------------------------------------------------------------
        //Sign Up Tests
        System.out.println("1. Test Sign Up: " + (test.testUserSignUp() ? "Passed" : "Failed"));
        System.out.println("2. Test Sign Up with Incomplete Info: " + (test.testUserSignUpWithIncompleteInfo() ? "Passed" : "Failed"));
        System.out.println("3. Test Sign Up with Existing User info: " + (test.testUserSignUpWithExistingUserInfo() ? "Passed" : "Failed"));
        //Login Tests
        System.out.println("4. Test Login with Username: " + (test.testUserLoginWithUsername() ? "Passed" : "Failed"));
        System.out.println("5. Test Login with Email: " + (test.testUserLoginWithEmail() ? "Passed" : "Failed"));
        System.out.println("6. Test Login with Username and Wrong Password: " + (test.testUserLoginWithUsernameAndWrongPassword() ? "Passed" : "Failed"));
        System.out.println("7. Test Login with Email and Wrong Password: " + (test.testUserLoginWithEmailAndWrongPassword() ? "Passed" : "Failed"));
        //Edit Tests
        System.out.println("8. Test Edit User: " + (test.testEditUserInfo() ? "Passed" : "Failed"));
        System.out.println("9. Test Edit User with Incomplete Info: " + (test.testEditUserWithIncompleteInfo() ? "Passed" : "Failed"));
        //Delete Tests
        System.out.println("10. Test Delete User: " + (test.testDeleteUser() ? "Passed" : "Failed"));
    }

    //Sign Up tests---------------------------------------------------------------------------------------------------------------------------------
    public boolean testUserSignUp()
    {
        boolean doneSignUp = userService.userSignUp("TestUsername", "test@gmail.com", "test123");

        if (doneSignUp)
        {
            User user = userService.getUserByUsername("TestUsername");
            if (user != null)
            {
                if (user.getEmail().equals("test@gmail.com") && user.getPassword().equals("test123"))
                {
                    return true;
                }
            }
        }
        //anything fails
        return false;
    }

    public boolean testUserSignUpWithIncompleteInfo()
    {
        //return true if this fails
        return !userService.userSignUp("TestUsername1", "test1@gmail.com", null);
    }

    public boolean testUserSignUpWithExistingUserInfo()
    {
        //try exisitng username
        boolean doneSignUp = userService.userSignUp("TestUsername", "test1@gmail.com", "test123");

        //if cannot sign up with exisitng username
        if (!doneSignUp)
        {
            //try exisitng email
            boolean doneSignUp1 = userService.userSignUp("TestUsername1", "test@gmail.com", "test123");
            
            //if cannot sign up with existing email
            if (!doneSignUp1)
            {
                return true;
            }
        }
        //anything fails
        return false;
    }

    //Login tests---------------------------------------------------------------------------------------------------------------------------------
    public boolean testUserLoginWithUsername()
    {
        return userService.userLogin("TestUsername", "test123");
    }

    public boolean testUserLoginWithEmail()
    {
        return userService.userLogin("test@gmail.com", "test123");
    }

    public boolean testUserLoginWithUsernameAndWrongPassword()
    {
        //opposite of result (means if its a fail, return true since we are making sure that wrong password = fail)
        return !userService.userLogin("TestUsername", "test124");
    }

    public boolean testUserLoginWithEmailAndWrongPassword()
    {
        return !userService.userLogin("test@gmail.com", "test124");
    }

    //User Edit---------------------------------------------------------------------------------------------------------------------------------
    public boolean testEditUserInfo()
    {
        //create a new user to test the edit
        boolean doneSignUp = userService.userSignUp("TestUsername1", "test1@gmail.com", "test123");

        if (doneSignUp)
        {
            //edit email and password (username probably we keep it as our primary key so we will try to prohibit user from editing it)
            boolean doneEdit = userService.userEdit("TestUsername1", "edited@gmail.com", "test345");

            if (doneEdit)
            {
                User user = userService.getUserByUsername("TestUsername1");
                if (user != null)
                {
                    if (user.getEmail().equals("edited@gmail.com") && user.getPassword().equals("test345"))
                    {
                        return true;
                    }
                }
            }
        }

        //anything fails
        return false;
    }

    public boolean testEditUserWithIncompleteInfo()
    {
        return !userService.userEdit("TestUsername1", "edited@gmail.com", null);
    }

    //User Delete---------------------------------------------------------------------------------------------------------------------------------
    public boolean testDeleteUser() 
    {
        //delete all created test users
        return userService.userDelete("TestUsername") && userService.userDelete("TestUsername1");
    }
}
