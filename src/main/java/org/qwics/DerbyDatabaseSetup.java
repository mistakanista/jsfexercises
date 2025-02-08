package org.qwics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DerbyDatabaseSetup {
  public static void main(String[] args) {
    String url = "jdbc:derby://localhost:1527/uebung;create=true"; // Auto-create DB
    String user = "APP";
    String password = "APP";

    try {
      // Register the Derby driver explicitly
      Class.forName("org.apache.derby.jdbc.ClientDriver");

      try (Connection conn = DriverManager.getConnection(url, user, password);
          Statement stmt = conn.createStatement()) {

        // Create table if not exists
        String createTableSQL = "CREATE TABLE UEBUNG ("
            + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
            + "VORNAME VARCHAR(255) NOT NULL, "
            + "NACHNAME VARCHAR(255) NOT NULL, "
            + "ADDRESS VARCHAR(255) NOT NULL"
            + ")";
        stmt.executeUpdate(createTableSQL);

        System.out.println("Table UEBUNG created successfully!");
      }

    } catch (ClassNotFoundException e) {
      System.err.println("Unable to load the Derby driver. Make sure derbyclient.jar is in the classpath.");
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
