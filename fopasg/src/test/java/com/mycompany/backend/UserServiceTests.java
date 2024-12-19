package com.mycompany.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        //clear entire file
        fileIO.purgeTxt(filename);
    }

    //Sign Up tests---------------------------------------------------------------------------------------------------------------------------------
    @Test 
    public void testUserSignUp()
    {        
        //assert that this will return true (if false means test failed)
        assertTrue(userService.userSignUp("TestUsername1", "test1@gmail.com", "test123"));

        User user = userService.getUserByUsername("TestUsername1");
        
        //check if the returned user from the txt file is the same as the one we signed up
        assertEquals("test1@gmail.com", user.getEmail());
        assertEquals("test123", user.getPassword());
    }

    @Test
    public void testUserSignUpWithIncompleteInfo()
    {
        //this statement should fail so we assert it will return false
        assertFalse(userService.userSignUp("TestUsername1", "test1@gmail.com", null));
    }

    @Test
    public void testUserSignUpWithExistingUserInfo()
    {
        assertFalse(userService.userSignUp("TestUsername", "test@gmail.com", "test123"));
    }

    //Login tests---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testUserLoginWithUsername()
    {
        assertTrue(userService.userLogin("TestUsername", "test123")); 
    }

    @Test
    public void testUserLoginWithEmail()
    {
        assertTrue(userService.userLogin("test@gmail.com", "test123"));
    }

    @Test
    public void testUserLoginWithUsernameAndWrongPassword()
    {
        assertFalse(userService.userLogin("TestUsername", "test124"));
    }

    @Test
    public void testUserLoginWithEmailAndWrongPassword()
    {
        assertFalse(userService.userLogin("test@gmail.com", "test124"));
    }

    //User Edit---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testEditUserInfo()
    {
        //edit email and password (username probably we keep it as our primary key so we will try to prohibit user from editing it)
        assertTrue(userService.userEdit("TestEdit", "edited@gmail.com", "test345"));

        User user = userService.getUserByUsername("TestEdit");

        assertEquals("edited@gmail.com", user.getEmail());
        assertEquals("test345", user.getPassword());
    }

    @Test
    public void testEditUserWithIncompleteInfo()
    {
        assertFalse(userService.userEdit("TestUsername1", "edited@gmail.com", null));
    }

    //User Delete---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testDeleteUser() 
    {
        //make sure this user exists first
        assertNotNull(userService.getUserByUsername("TestDelete"));

        //then delete
        assertTrue(userService.userDelete("TestDelete"));

        //then check if it's still thr
        assertNull(userService.getUserByUsername("TestDelete"));
    }
}
