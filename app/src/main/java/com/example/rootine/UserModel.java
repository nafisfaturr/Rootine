package com.example.rootine;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String id;
    private String email;
    private String username;
    private String password;
    private String role;
    private String alamat;

    public UserModel(String id,String email, String username, String password, String role, String alamat) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.alamat = alamat;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
