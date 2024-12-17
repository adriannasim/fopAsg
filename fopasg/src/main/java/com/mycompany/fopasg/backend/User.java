package com.mycompany.fopasg.backend;

public class User
{
    private String username;
    private String email;
    private String password;

    //constructor
    public User(){}

    public User(String username, String email, String password) 
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //getter
    public String getUsername()
    {
        return username;
    }
    
    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    //setter
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    //to string
    public String toString()
    {
        return username + "," + email + "," + password;
    }
}