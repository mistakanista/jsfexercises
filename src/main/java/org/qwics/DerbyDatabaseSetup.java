package org.qwics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DerbyDatabaseSetup {
  public static void main(String[] args) {
    String url = "jdbc:derby://localhost:1527/uebung;create=true"; // Auto-create DB
    String user = "APP";
    String password = System.getenv("DERBY_PW");

    try {
      // Register the Derby driver explicitly
      Class.forName("org.apache.derby.jdbc.ClientDriver");

      try (Connection conn = DriverManager.getConnection(url, user, password);
          Statement stmt = conn.createStatement()) {

        // Create table if not exists
        String createTableSQL = "CREATE TABLE USERS ("
            + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
            + "VORNAME VARCHAR(255) NOT NULL, "
            + "NACHNAME VARCHAR(255) NOT NULL, "
            + "ADDRESS VARCHAR(255) NOT NULL"
            + ")";
        stmt.executeUpdate(createTableSQL);
        log.info("Table USERS created successfully!");
      }

    } catch (ClassNotFoundException e) {
      log.error("Unable to load the Derby driver. Make sure derbyclient.jar is in the classpath. {}", e.getMessage());
    } catch (SQLException e) {
      log.error("SQL Exception {}", e.getMessage());
    }
  }

}
