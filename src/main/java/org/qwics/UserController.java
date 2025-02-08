package org.qwics;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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

  public List<User> getUsers() {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM UEBUNG";

    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setVorname(resultSet.getString("vorname"));
        user.setNachname(resultSet.getString("nachname"));
        user.setAddress(resultSet.getString("address"));
        users.add(user);
      }

    } catch (Exception e) {
      e.printStackTrace(); // Log errors
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
}
