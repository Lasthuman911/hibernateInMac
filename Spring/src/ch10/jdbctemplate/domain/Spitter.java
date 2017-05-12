package ch10.jdbctemplate.domain;

public class Spitter {
  
  private String id;
  private String username;
  private String password;
/*  private String fullName;
  private String email;
  private boolean updateByEmail;*/

  public Spitter(String id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
/*    this.fullName = fullName;
    this.email = email;
    this.updateByEmail = updateByEmail;*/
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
/*
  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public boolean isUpdateByEmail() {
    return updateByEmail;
  }*/

}
