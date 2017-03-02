package org.androidtown.hello;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    public String email, password;

    public LoginInfo() {}

    public LoginInfo(String email, String password){
        this.email = email;
        this.password = password;
    }
}

