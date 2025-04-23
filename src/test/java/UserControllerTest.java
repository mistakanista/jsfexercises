import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qwics.User;
import org.qwics.UserController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testGetUsers() throws Exception {
        // Mock ResultSet
        when(connection.prepareStatement("SELECT * FROM USERS")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock ResultSet data
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("ID")).thenReturn(1, 2);
        when(resultSet.getString("VORNAME")).thenReturn("Max", "Anna");
        when(resultSet.getString("NACHNAME")).thenReturn("Mustermann", "Müller");
        when(resultSet.getString("ADDRESS")).thenReturn("Musterstraße 1", "Beispielweg 2");

        // Call method
        List<User> users = userController.getUsers();

        // Assertions
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Max", users.get(0).getVorname());
        assertEquals("Anna", users.get(1).getVorname());
    }

    @Test
    public void testSpeichereUser() throws Exception {
        // Mock User
        User user = new User();
        user.setVorname("Max");
        user.setNachname("Mustermann");
        user.setAddress("Musterstraße 1");
        userController.setUser(user);

        // Mock PreparedStatement
        when(connection.prepareStatement("INSERT INTO USERS (VORNAME, NACHNAME, ADDRESS) VALUES (?, ?, ?)"))
                .thenReturn(preparedStatement);

        // Call method
        String result = userController.speichereUser();

        // Verify
        verify(preparedStatement).setString(1, "Max");
        verify(preparedStatement).setString(2, "Mustermann");
        verify(preparedStatement).setString(3, "Musterstraße 1");
        verify(preparedStatement).executeUpdate();

        // Assertions
        assertEquals("userAnzeigen", result);
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Mock User
        User user = new User();
        user.setId(1);

        // Mock PreparedStatement
        when(connection.prepareStatement("DELETE FROM USERS WHERE ID = ?")).thenReturn(preparedStatement);

        // Call method
        String result = userController.deleteUser(user);

        // Verify
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();

        // Assertions
        assertEquals("index?faces-redirect=true", result);
    }
}