package tests.login;

import objects.User;
import org.testng.annotations.Test;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.RestApiUtils;

public class DemoTest extends LoggerUtils {

    public void setUpTest() {
        log.info("[SETUP TEST] " + sTestName);
        driver = setUpDriver;
        sUsername = PropertiesUtils.getAdminUsername();
        sPassword = PropertiesUtils.getAdminPassword();
    }

    @Test
    public void testDemoTest() {

        User user1 = User.createNewUniqueUser("janko");
        User user2 = User.createNewUniqueUser("drasko");

        log.info(user1);
        log.info(user2);

        RestApiUtils.checkIfUserExists(user1.getUsername());
    }
}
