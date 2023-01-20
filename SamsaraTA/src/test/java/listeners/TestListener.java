package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.LoggerUtils;
import utils.PropertiesUtils;

public class TestListener extends LoggerUtils implements ITestListener {

    private static boolean bUpdateJira = false;
    private static final boolean bListenerTakeScreenshot = PropertiesUtils.getTakeScreenshot();

    @Override
    public void onTestStart(ITestContext context) {
        String sSuiteName = context.getSuite().getName();
        log.info("[SUITE STARTED] " + sSuiteName);
        context.setAttribute("listenerTakeScreenshot", bListenerTakeScreenshot);
        bUpdateJira = getUpdateJira(context);
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


}
