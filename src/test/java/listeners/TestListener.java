package listeners;

import annotations.Jira;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;
import utils.ScreenshotUtils;

import java.rmi.Remote;
import java.util.Arrays;
import java.util.Properties;

public class TestListener extends LoggerUtils implements ITestListener {

    private static boolean updateJira = false;
    private static final boolean bListenerTakeScreenshot = PropertiesUtils.getTakeScreenshot();

    @Override
    public void onStart(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE STARTED] " + sSuiteName);
        context.setAttribute("listenerTakeScreenshot", bListenerTakeScreenshot);
        updateJira = getUpdateJira(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE FINISHED] " + sSuiteName);
    }
    @Override
    public void onTestStart(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST STARTED] " + sTestName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SUCCESS] " + sTestName);
        if(updateJira) {
            Jira jira = getJiraDetails(result);
            String jiraID = jira.jiraID();
            String owner = jira.owner();
            String browser = PropertiesUtils.getBrowser();
            String environment = PropertiesUtils.getEnvironment();
            String appVersion = "version";
            log.info("Jira ID: " + jiraID);
            log.info("Owner: " + owner);
            // Using jira-client update Jira test case
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST FAILED] " + sTestName);

        String sScreenShotPath = "";
        if(bListenerTakeScreenshot) {
            log.info("ScreenShot from Listener!");
            WebDriver[] drivers = getWebDriverInstances(result);
            if (drivers != null) {
                for(int i = 0; i < drivers.length; i++) {
                    String sScreenShotName = sTestName;
                    if (drivers.length > 1) {
                        sScreenShotName = sScreenShotName + "." + (i+1);
                    }
                    sScreenShotPath = ScreenshotUtils.takeScreenShot(drivers[i], sScreenShotName);
                }
            }
        }
        if(updateJira) {
            Jira jira = getJiraDetails(result);
            String sBugSubject = sTestName + "_" + result.getThrowable().getMessage();
            String sBugDescription = Arrays.toString(result.getThrowable().getStackTrace());
            // Using jira-client update jira test case and create Jira Ticket (bug)
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SKIPPED] " + sTestName);
    }

    private static WebDriver[] getWebDriverInstance(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        String sDriverName = sTestName + ".drivers";
        WebDriver[] driver = (WebDriver[]) result.getTestContext().getAttribute(sDriverName);
        if(driver == null) {
            log.error("Cannot get Driver Instance for test '" + sTestName + ".");
        }
        return driver;
    }

    private static WebDriver[] getWebDriverInstances(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        String sDriverNames = sTestName + ".drivers";
        WebDriver[] drivers = (WebDriver[]) result.getTestContext().getAttribute(sDriverNames);
        if(drivers == null) {
            log.error("Cannot get Driver Instance(s) for test '" + sTestName + ".");
        }
        return drivers;
    }

    private static String getJiraID(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        String jiraID = "";
        try {
            //jiraID = (String) result.getTestClass().getRealClass().getDeclaredField("jiraID").get(result.getInstance());

            // 4
            Jira jira = result.getTestClass().getRealClass().getAnnotation(Jira.class);
            if (jira == null) {
                log.error("Listener cannot get jira details for test '" + sTestName + "!");
            } else {
                jiraID = jira.jiraID();
                String owner = jira.owner();
                log.info("JIRAID: " + jiraID);
                log.info("Owner: " + owner);
            }

        } catch (Exception e) {
            log.error("Cannot get JiraID for test '" + result.getTestClass().getName() + "! Message: " + e.getMessage());
        }
        return jiraID;
    }

    private static Jira getJiraDetails(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        Jira jira = null;
        try {
            //jiraID = (String) result.getTestClass().getRealClass().getDeclaredField("jiraID").get(result.getInstance());

            // 4
            jira = result.getTestClass().getRealClass().getAnnotation(Jira.class);

        } catch (Exception e) {
            log.error("Cannot get JiraDetails for test '" + result.getTestClass().getName() + "! Message: " + e.getMessage());
        }
        return jira;
    }

    private static boolean getUpdateJira(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        String sUpdateJira = context.getCurrentXmlTest().getParameter("updateJira");
        if(sUpdateJira == null) {
            log.warn("Parameter 'updateJira' is not set in '" + sSuiteName + "' suite!");
            return false;
        }
        else {
            if(!sUpdateJira.equalsIgnoreCase("true") && !sUpdateJira.equalsIgnoreCase("false")) {
                log.warn("Parameter 'updateJira' in '" + sSuiteName + "' suite is not recognized as boolean value!");
                return false;
            }
        }
        boolean updateJira = Boolean.parseBoolean(sUpdateJira);
        log.info("Update Jira: " + updateJira);
        return updateJira;
    }
}
