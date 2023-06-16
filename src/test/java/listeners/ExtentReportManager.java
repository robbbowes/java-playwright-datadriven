package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;
import core.GlobalTestBase;
import org.apache.commons.codec.CharEncoding;
import org.testng.Assert;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentReportManager {


    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

        htmlReporter.config().setEncoding(CharEncoding.UTF_8);
        htmlReporter.config().setDocumentTitle("RJB Automation Report");
        htmlReporter.config().setReportName("Automation Test Results");
        htmlReporter.config().setTheme(Theme.DARK);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Automation Tester", "RJB");
        extent.setSystemInfo("Build Number", "12345");
        extent.setSystemInfo("Something Cool", "Cool Story Bro");

        return extent;
    }

    public static Path captureScreenshot(String testName) {
//        final String dateString = new Date().toString().replace(":", "_").replace(" ", "_");
        final String fileName = testName + ".jpg";
        final Path path = Paths.get("./reports/screenshots/" + fileName);
        GlobalTestBase.getPage().screenshot(new Page.ScreenshotOptions().setPath(path));
        return path;
    }

    public static Path getPageSource(String testName) {
        final String fileName = testName + ".html";

        final String content = GlobalTestBase.getPage().content();
        return createHTML2(fileName, content);
    }

    private static void createHTMLDocument(String fileName, String content) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path createHTML2(String fileName, String content) {
        File directory = new File("./reports/html/");
        if (! directory.exists()){
            directory.mkdir();
        }

        File file  = new File(directory + "/" + fileName);
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Path path = file.toPath();
        return path;
    }
}
