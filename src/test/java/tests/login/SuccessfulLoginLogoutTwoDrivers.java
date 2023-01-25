package tests.login;

import annotations.Jira;
import data.CommonStrings;
import data.Groups;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PracticePage;
import pages.UsersPage;
import pages.WelcomePage;
import tests.BaseTestClass;
import utils.PropertiesUtils;

@Jira(jiraID = "JIRA0003")
@Test(groups = {Groups.REGRESSION, Groups.LOGIN})
public class SuccessfulLoginLogoutTwoDrivers extends BaseTestClass {

    private final String sTestName = this.getClass().getName();
    private WebDriver driver1;
    private WebDriver driver2;

    private String sUsername;
    private String sPassword;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.info("[SETUP TEST] " + sTestName);
        driver1 = setUpDriver();
        driver2 = setUpDriver();
        sUsername = PropertiesUtils.getAdminUsername();
        sPassword = PropertiesUtils.getAdminPassword();
    }

    @Test
    public void testSuccessfulLoginLogout() {

        String sExpectedLogoutSuccessMessage = CommonStrings.getLogoutSuccessMessage();

        log.info("[START TEST] " + sTestName);
        LoginPage loginPage1 = new LoginPage(driver1).open();
        LoginPage loginPage2 = new LoginPage(driver1).open();

        WelcomePage welcomePage1 = loginPage1.login(sUsername, sPassword);
        WelcomePage welcomePage2 = loginPage2.login(sUsername, sPassword);

        UsersPage usersPage1 = welcomePage1.clickUsersTab();
        PracticePage practicePage2 = welcomePage2.clickPracticeTab();

        Assert.fail("ddd");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        log.info("[END TEST] " + sTestName);
        tearDown(driver1, testResult);
        tearDown(driver2, testResult);
    }
}
