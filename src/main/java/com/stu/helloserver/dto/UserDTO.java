package com.stu.helloserver.dto;

public class UserDTO {
    private String username;
    private String password;

    // 自动生成 Getter 和 Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}