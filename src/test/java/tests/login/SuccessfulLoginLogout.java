package tests.login;

import annotations.Jira;
import data.CommonStrings;
import data.Groups;
import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import pages.LoginPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

@Jira(jiraID = "JIRA0001")
@Test(groups = {Groups.REGRESSION, Groups.LOGIN, Groups.SANITY})
public class SuccessfulLoginLogout extends BaseTestClass {

    private final String sTestName = this.getClass().getName();

    private WebDriver driver;
    //public String jiraID = "JIRA0001";

    private User user;
    private boolean bCreated = false;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        driver = setUpDriver();
        testContext.setAttribute(sTestName + ".drivers", new WebDriver[]{driver});
        testContext.setAttribute(sTestName + ".jira", "JIRA0001");

        user = User.createNewUniqueUser("SuccessLoginLogout");
        RestApiUtils.postUser(user);
        bCreated = true;
        user.setCreatedAt(RestApiUtils.getUser(user.getUsername()).getCreatedAt());
    }

    @Test
    public void testSuccessfulLoginLogout() {

        String sExpectedLogoutSuccessMessage = CommonStrings.getLogoutSuccessMessage();

        LoginPage loginPage = new LoginPage(driver).open();
        Assert.assertFalse(loginPage.isSuccessMessageDisplayed(), "Success Message should NOT be displayed!");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error Message should NOT be displayed!");

        loginPage.typeUsername(user.getUsername());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);
        loginPage.typePassword(user.getPassword());
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        WelcomePage welcomePage = loginPage.clickLoginButton();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage = welcomePage.clickLogoutLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error Message should NOT be displayed!");
        String sSuccessMessage = loginPage.getSuccessMessage();
        Assert.assertEquals(sSuccessMessage, sExpectedLogoutSuccessMessage, "Wrong Logout Success Message!");
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.debug("[END TEST] " + sTestName);
        tearDown(driver, testResult);
        if(bCreated) {
            cleanUp();
        }
    }

    private void cleanUp() {
        log.debug("cleanUp()");
        try {
            RestApiUtils.deleteUser(user.getUsername());
        } catch (AssertionError | Exception e) {
            log.error("Cleaning up failed! Message: " + e.getMessage());
        }
    }
}
