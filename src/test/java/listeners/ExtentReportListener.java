package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;

public class ExtentReportListener implements ITestListener, ISuiteListener {

    private static final ThreadLocal<ExtentTest> ET = new ThreadLocal<>();
    private static final String FILE_NAME = "Extent_Report"
//            + new Date().toString().replace(":", "_").replace(" ", "_")
            + ".html";
    private static final ExtentReports EXTENT = ExtentReportManager.createInstance("./reports/" + FILE_NAME);

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = EXTENT.createTest(result.getTestClass().getName() + " - " + result.getMethod().getMethodName());
        ET.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        final String name = result.getMethod().getMethodName();
        final Markup label = MarkupHelper.createLabel("Test passed: " + name, ExtentColor.GREEN);
        getTest().pass(label);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        final String name = result.getMethod().getMethodName();
        final Path screenshotPath = ExtentReportManager.captureScreenshot(name).toAbsolutePath();
        final Path pageSource = ExtentReportManager.getPageSource(name).toAbsolutePath();
        final String message = "Test failed: " + name + " " + result.getThrowable().getMessage();

        getTest().fail(MarkupHelper.createLabel(message, ExtentColor.RED));
        getTest().fail("<a href=" + pageSource + ">View Page Source</a>");
        getTest().fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath.toString()).build());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String name = result.getMethod().getMethodName();
        getTest().skip(MarkupHelper.createLabel("Test skipped: " + name, ExtentColor.BLUE));
    }

    @Override
    public void onFinish(ISuite suite) {
        EXTENT.flush();
    }

    public static ExtentTest getTest() {
        return ET.get();
    }
}
