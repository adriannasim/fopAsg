package com.mycompany.backend;

import static org.junit.Assert.*;
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
        fileIO.purgeFile(filename);
    }

    //Sign Up tests---------------------------------------------------------------------------------------------------------------------------------
    @Test 
    public void testUserSignUp()
    {        
        //assert that this will return true (if false means test failed)
        assertTrue((boolean) userService.userSignUp("TestUsername1", "test1@gmail.com", "test123").getReturnObject());

        User user = userService.getUserByUsername("TestUsername1");
        
        //check if the returned user from the txt file is the same as the one we signed up
        assertEquals("test1@gmail.com", user.getEmail());
        assertEquals("test123", user.getPassword());
    }

    @Test
    public void testUserSignUpWithIncompleteInfo()
    {
        //this statement should fail so we assert it will return false
        assertFalse((boolean) userService.userSignUp("TestUsername1", "test1@gmail.com", null).getReturnObject());
    }

    @Test
    public void testUserSignUpWithExistingUserInfo()
    {
        assertFalse((boolean) userService.userSignUp("TestUsername", "test@gmail.com", "test123").getReturnObject());
    }

    //Login tests---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testUserLoginWithUsername()
    {
        String returnValue = (String) userService.userLogin("TestUsername", "test123").getReturnObject();
        assertEquals(returnValue, "TestUsername");
    }

    @Test
    public void testUserLoginWithEmail()
    {
        String returnValue = (String) userService.userLogin("test@gmail.com", "test123").getReturnObject();
        assertEquals(returnValue, "TestUsername");
    }

    @Test
    public void testUserLoginWithUsernameAndWrongPassword()
    {
        String returnValue = (String) userService.userLogin("TestUsername", "test124").getReturnObject();
        assertEquals(returnValue, null);
    }

    @Test
    public void testUserLoginWithEmailAndWrongPassword()
    {
        String returnValue = (String) userService.userLogin("test@gmail.com", "test124").getReturnObject();
        assertEquals(returnValue, null);
    }

    //User Edit---------------------------------------------------------------------------------------------------------------------------------
    @Test
    public void testEditUserInfo()
    {
        //edit email and password (username probably we keep it as our primary key so we will try to prohibit user from editing it)
        assertTrue((boolean) userService.userEdit("TestEdit", "edited@gmail.com", "test345").getReturnObject());

        User user = userService.getUserByUsername("TestEdit");

        assertEquals("edited@gmail.com", user.getEmail());
        assertEquals("test345", user.getPassword());
    }

    @Test
    public void testEditUserWithIncompleteInfo()
    {
        assertFalse((boolean) userService.userEdit("TestUsername1", "edited@gmail.com", null).getReturnObject());
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
