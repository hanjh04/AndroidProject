package org.androidtown.hello;

import java.io.Serializable;

public class Data implements Serializable {

    public String name, email, password, age, gender;

    public Data() {}

    public Data(String name, String age, String gender, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
    }

    public Data(String email){
        this.email = email;
    }
}
