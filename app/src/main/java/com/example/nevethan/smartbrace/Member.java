package com.example.nevethan.smartbrace;

/**
 * This class is the implementation of Member. It is a POJO class, which means the get- and set methods for
 * all fields involving the Member is here.
 */
public class Member {

    int id;

    String username, password;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }





}
