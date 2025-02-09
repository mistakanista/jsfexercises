package org.qwics;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;

@SessionScoped
@Named("userController")
public class UserController implements Serializable {
  private User user = null;

  @Resource(lookup = "jdbc/DerbyDS") // JNDI lookup for Liberty's DataSource
  private DataSource dataSource;

  public UserController() {
    user = new User();
  }

  private List<User> users;

  @PostConstruct
  public void init() {
    getUsers();
  }

  public List<User> getUsers() {
    users = new ArrayList<>();
    String sql = "SELECT * FROM UEBUNG";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("ID"));
        user.setVorname(resultSet.getString("VORNAME"));
        user.setNachname(resultSet.getString("NACHNAME"));
        user.setAddress(resultSet.getString("ADDRESS"));
        users.add(user);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return users;
  }

  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public String speichereUser() {
    String sql = "INSERT INTO UEBUNG (VORNAME, NACHNAME, ADDRESS) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // Set parameters to prevent SQL injection
      statement.setString(1, user.getVorname());
      statement.setString(2, user.getNachname());
      statement.setString(3, user.getAddress());

      // Use executeUpdate() for INSERT, UPDATE, DELETE
      int rowsAffected = statement.executeUpdate();
      System.out.println("Rows inserted: " + rowsAffected);

    } catch (Exception e) {
      e.printStackTrace(); // Log errors
    }

    System.out.println("Vorname: "+user.getVorname());
    System.out.println("Nachname: "+user.getNachname());
    System.out.println("Address: "+user.getAddress());

    return "userAnzeigen";
  }

  public String updateUser() {
    String sql = "UPDATE UEBUNG SET VORNAME = ?, NACHNAME = ?, ADDRESS = ? WHERE ID = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // Set parameters to prevent SQL injection
      statement.setString(1, user.getVorname());
      statement.setString(2, user.getNachname());
      statement.setString(3, user.getAddress());
      statement.setInt(4, user.getId());

      // Use executeUpdate() for INSERT, UPDATE, DELETE
      int rowsAffected = statement.executeUpdate();
      System.out.println("User updated: " + rowsAffected);

    } catch (Exception e) {
      e.printStackTrace(); // Log errors
    }

    System.out.println("Vorname: "+user.getVorname());
    System.out.println("Nachname: "+user.getNachname());
    System.out.println("Address: "+user.getAddress());

    return "userAnzeigen";
  }

  public String deleteUser(User user) {
    String sql = "DELETE FROM UEBUNG WHERE ID = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, user.getId());

      int rowsAffected = statement.executeUpdate();
      System.out.println("Rows deleted: " + rowsAffected);

      if (rowsAffected > 0) {
        users.remove(user); // Remove from the list after deletion
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "index?faces-redirect=true";
  }

  public String editUser(User user) {
    this.user = user;
    return "userEdit";
  }

  public String newUser() {
    this.user = new User();
    return "newUser";
  }
}
