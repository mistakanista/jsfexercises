package org.qwics;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named("userController")
public class UserController implements Serializable {
  private User user = null;
  public UserController() {
    user = new User();
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public String speichereUser() {
    System.out.println("Vorname: "+user.getVorname());
    System.out.println("Nachname: "+user.getNachname());
    return "userAnzeigen";
  } }
