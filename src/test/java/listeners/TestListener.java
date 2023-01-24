package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;

public class TestListener extends LoggerUtils implements ITestListener {

    private static boolean bUpdateJira = false;
    private static final boolean bListenerTakeScreenshot = PropertiesUtils.getTakeScreenshot();

    @Override
    public void onTestStart(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST STARTED] " + sTestName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SUCCESS] " + sTestName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST FAILED] " + sTestName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String sTestName = result.getTestClass().getName();
        log.info("[TEST SKIPPED] " + sTestName);
    }
}
