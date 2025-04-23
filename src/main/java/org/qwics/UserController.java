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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@SessionScoped
@Named("userController")
@Slf4j
@Data
public class UserController implements Serializable {
  private User user;

  private int userNr;

  public int getUserNr() {
    return userNr;
  }

  public void setUserNr(int userNr) {
    this.userNr = userNr;
  }

  @Resource(lookup = "jdbc/DerbyDS")
  private DataSource dataSource;

  public UserController() {
    user = new User();
  }

  private List<User> users;
  private List<User> selectedUsers;

  @PostConstruct
  public void init() {
    getUsers();
  }

  public List<User> getUsers() {
    users = new ArrayList<>();
    String sql = "SELECT * FROM USERS";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        User localUser = new User();
        localUser.setId(resultSet.getInt("ID"));
        localUser.setVorname(resultSet.getString("VORNAME"));
        localUser.setNachname(resultSet.getString("NACHNAME"));
        localUser.setAddress(resultSet.getString("ADDRESS"));
        users.add(localUser);
      }
    } catch (Exception e) {
      log.error("Error getting users from DB {}", e.getMessage());
    }
    return users;
  }

  public String speichereUser() {
    String sql = "INSERT INTO USERS (VORNAME, NACHNAME, ADDRESS) VALUES (?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // Set parameters to prevent SQL injection
      statement.setString(1, user.getVorname());
      statement.setString(2, user.getNachname());
      statement.setString(3, user.getAddress());

      // Use executeUpdate() for INSERT, UPDATE, DELETE
      int rowsAffected = statement.executeUpdate();
      log.info("Rows inserted: " + rowsAffected);

    } catch (Exception e) {
      log.error("Error saving user {}", e.getMessage());
    }

    log.info("Vorname: "+user.getVorname());
    log.info("Nachname: "+user.getNachname());
    log.info("Address: "+user.getAddress());

    return "userAnzeigen";
  }

  public String updateUser() {
    String sql = "UPDATE USERS SET VORNAME = ?, NACHNAME = ?, ADDRESS = ? WHERE ID = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // Set parameters to prevent SQL injection
      statement.setString(1, user.getVorname());
      statement.setString(2, user.getNachname());
      statement.setString(3, user.getAddress());
      statement.setInt(4, user.getId());

      // Use executeUpdate() for INSERT, UPDATE, DELETE
      int rowsAffected = statement.executeUpdate();
      log.info("User updated: {}", rowsAffected);

    } catch (Exception e) {
      log.error("Error updating user {}", e.getMessage());
    }

    log.info("Vorname: {}", user.getVorname());
    log.info("Nachname: {}", user.getNachname());
    log.info("Address: {}", user.getAddress());

    return "userAnzeigen";
  }

  public String deleteUser(User user) {
    String sql = "DELETE FROM USERS WHERE ID = ?";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setInt(1, user.getId());

      int rowsAffected = statement.executeUpdate();
      log.info("Rows deleted: {}", rowsAffected);

      if (rowsAffected > 0) {
        users.remove(user); // Remove from the list after deletion
      }

    } catch (Exception e) {
      log.error("Error deleting user {}", e.getMessage());
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
