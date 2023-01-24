package tests.login;

import data.CommonStrings;
import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

public class SuccessfulLoginLogout extends BaseTestClass {

    private final String sTestName = this.getClass().getName();

    private WebDriver driver;

    private User user;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        driver = setUpDriver();
        user = User.createNewUniqueUser("SuccessLoginLogout");
        log.info("User: " + user);
        RestApiUtils.postUser(user);
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
        log.info("User: " + user);
    }

    @Test
    public void testSuccessfulLoginLogout() {

        String sExpectedLogoutSuccessMessage = CommonStrings.getLogoutSuccessMessage();

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage.typeUsername(user.getUsername());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage.typePassword(user.getPassword());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.clickLoginButton();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage = welcomePage.clickLogoutLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        String sActualLogoutSuccessMessage = loginPage.getSuccessMessage();
        Assert.assertEquals(sActualLogoutSuccessMessage, sExpectedLogoutSuccessMessage, "Wrong Logout Success Message!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
    }

}
