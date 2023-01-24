package tests.register;

import data.Time;
import objects.User;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.RegisterPage;
import tests.BaseTestClass;
import utils.DateTimeUtils;
import utils.RestApiUtils;

public class SuccessfulRegister extends BaseTestClass {

    private final String sTestName = this.getClass().getSimpleName();

    private WebDriver driver;

    private User user;

    @BeforeMethod
    public void setUpTest(ITestContext testContext) {
        log.debug("[SETUP TEST] " + sTestName);

        driver = setUpDriver();
        user = User.createNewUniqueUser(sTestName);
    }

    @Test
    public void testSuccessfulRegister() {

        log.debug("[START TEST] " + sTestName);

        LoginPage loginPage = new LoginPage(driver).open();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        RegisterPage registerPage = loginPage.clickCreateAccountLink();
        DateTimeUtils.wait(Time.TIME_DEMONSTRATION);

        loginPage = registerPage.registerUser(user);

        String sActualRegisterSuccessMessage = loginPage.getSuccessMessage();
        Assert.assertEquals(sActualRegisterSuccessMessage, sExpected);

        User savedUser = RestApiUtils.getUser(user.getUsername());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(savedUser.getEmail(), user.getEmail());
        // add other fields
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownTest(ITestResult testResult) {
        tearDown(driver, testResult);
        RestApiUtils.deleteUser(user.getUsername());
    }
}
