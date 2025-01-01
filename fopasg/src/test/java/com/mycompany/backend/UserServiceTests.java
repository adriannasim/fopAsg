package com.mycompany.backend;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.*;

public class UserServiceTests 
{
    String filename = "TestUsers.txt";
    UserService userService = new UserService(filename);
    FileIO fileIO = new FileIO();
        
    @Before
    public void setup()
    {
        //create users for test
        userService.userSignUp("TestUsername", "test@gmail.com", "test123");
        userService.userSignUp("TestEdit", "testEdit@gmail.com", "test123");
        userService.userSignUp("TestDelete", "testDelete@gmail.com", "test123");
    }

    @After
    public void cleanUp()
    {
        try 
        {
            //clear entire file
            fileIO.clearFile(filename);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //Sign Up tests---------------------------------------------------------------------------------------------------------------------------------
    @Test 
    public void testUserSignUp()
    {        
        //assert that this will return true (if false means test failed)
        assertTrue((boolean) userService.userSignUp("TestUsername1", "test1@gmail.com", "test123").isSuccessful());

        User user = userService.getUserByUsername("TestUsername1");
        
        //check if the returned user from the txt file is the same as the one we signed up
        assertEquals("test1@gmail.com", user.getEmail());
        assertEquals("test123", user.getPassword());
    }

    @Test
    public void testUserSignUpWithIncompleteInfo()
    {
        //this statement should fail so we assert it will return false
        assertFalse((boolean) userService.userSignUp("TestUsername1", "test1@gmail.com", null).isSuccessful());
    }

    @Test
    public void testUserSignUpWithExistingUserInfo()
    {
        assertFalse((boolean) userService.userSignUp("TestUsername", "test@gmail.com", "test123").isSuccessful());
    }

    //Login tests---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testUserLoginWithUsername()
    {
        //get the result of the operation
        ServiceResult result = userService.userLogin("TestUsername", "test123");
        
        //check if its successful
        assertTrue(result.isSuccessful());
        //check if the username matches
        assertEquals(result.getReturnObject(), "TestUsername");
    }

    @Test
    public void testUserLoginWithEmail()
    {
        ServiceResult result = userService.userLogin("test@gmail.com", "test123");
        assertTrue(result.isSuccessful());
        assertEquals(result.getReturnObject(), "TestUsername");
    }

    @Test
    public void testUserLoginWithUsernameAndWrongPassword()
    {
        assertFalse(userService.userLogin("TestUsername", "test124").isSuccessful());
    }

    @Test
    public void testUserLoginWithEmailAndWrongPassword()
    {
        assertFalse(userService.userLogin("test@gmail.com", "test124").isSuccessful());
    }

    //User Edit---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testEditUserInfo()
    {
        //edit email and password (username probably we keep it as our primary key so we will try to prohibit user from editing it)
        assertTrue(userService.userEdit("TestEdit", "edited@gmail.com", "test345").isSuccessful());

        User user = userService.getUserByUsername("TestEdit");

        assertEquals("edited@gmail.com", user.getEmail());
        assertEquals("test345", user.getPassword());
    }

    @Test
    public void testEditUserWithIncompleteInfo()
    {
        assertFalse(userService.userEdit("TestUsername1", "edited@gmail.com", null).isSuccessful());
    }

    //User Delete---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteUser() 
    {
        //make sure this user exists first
        assertNotNull(userService.getUserByUsername("TestDelete"));

        //then delete
        assertTrue(userService.userDelete("TestDelete").isSuccessful());

        //then check if it's still thr
        assertNull(userService.getUserByUsername("TestDelete"));
    }
}