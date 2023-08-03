import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SocialAppTest {

    // Test main login
    @Test
    public void testMainLogin() {
        String username = "Main";
        Assertions.assertTrue(SocialApp.isValidUsername(username));
    }

    // Test user1 login
    @Test
    public void testUser1Login() {
        String username = "User1";
        Assertions.assertTrue(SocialApp.isValidUsername(username));
    }

    // Test user2 login
    @Test
    public void testUser2Login() {
        String username = "User2";
        Assertions.assertTrue(SocialApp.isValidUsername(username));
    }

    // Test invalid login
    @Test
    public void testInvalidLogin() {
        String username = "InvalidUser";
        Assertions.assertFalse(SocialApp.isValidUsername(username));
    }
}
