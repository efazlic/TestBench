package tests;

import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenshotUtils;
import utils.WebDriverUtils;

public class BaseTestClass extends LoggerUtils {

    protected WebDriver setUpDriver() {
        return WebDriverUtils.setUpDriver();
    }

    protected void quitDriver(WebDriver driver) {
        log.debug("quitDriver()");
        WebDriverUtils.quitDriver(driver);
    }

    private void ifFailed(WebDriver driver, ITestResult testResult, int session) {
        if(testResult.getStatus() == ITestResult.FAILURE) {

            if (PropertiesUtils.getTakeScreenshot() && !getListenerTakeScreenShot(testResult)) {
                log.info("ScreenShot from BaseTestClass!");
                String sTestName = testResult.getTestClass().getName();
                String sScreenShotName = sTestName;
                if (session > 0) {
                    sScreenShotName = sScreenShotName + "." + session;
                }
                ScreenshotUtils.takeScreenShot(driver, sScreenShotName);
            }
        }
    }

    protected void tearDown(WebDriver driver, ITestResult testResult, int session) {
        String sTestName = testResult.getTestClass().getName();
        session = Math.abs(session);
        String sSessionName = sTestName;
        if(session > 0) {
            sSessionName = sSessionName + "." + session;
        }
        log.debug("tearDown(" + sSessionName + ")");
        try {
            ifFailed(driver, testResult, session);
        } catch (AssertionError | Exception e) {
            log.error("Exception occurred in tearDown(" + sSessionName + ")! Message: " + e.getMessage());
        } finally {
            quitDriver(driver);
        }
    }

    protected void tearDown(WebDriver driver, ITestResult testResult) {
        tearDown(driver, testResult, 0);
    }

    protected void tearDown(WebDriver driver) {
        log.debug("tearDown()");
        quitDriver(driver);
    }

    private boolean getListenerTakeScreenShot(ITestResult result) {
        try {
            return (boolean) result.getTestContext().getAttribute("listenerTakeScreenShot");
        } catch (Exception e) {
            return false;
        }
    }
}
