package com.legion.tests.testframework.bdd.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.legion.tests.testframework.PropertyMap;
import com.legion.utils.MyThreadLocal;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import gherkin.ast.Examples;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import gherkin.ast.Tag;
import gherkin.pickles.PickleTag;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExtentCucumberHelper {
    private static ExtentReports extent;
    private static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> featureTest = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutline = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenario = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<PickleStepTestStep>> stepList = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> stepTest = new InheritableThreadLocal<>();
    private static Map<String, ExtentTest> mapFeature = new HashMap<>();

    private static Logger logger4j = Logger.getLogger(ExtentCucumberHelper.class);

    private static String reportPath = "target".concat(File.separator).concat(PropertyMap.get("legion.cucumber.extent.report.name"));

    private static ExtentHtmlReporter getExtentHtmlReport() {
        if (htmlReporter != null) {
            return htmlReporter;
        }

        File file = new File(reportPath.concat(File.separator)
                .concat(PropertyMap.get("legion.cucumber.extent.report.name"))
                .concat(".html"));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        htmlReporter = new ExtentHtmlReporter(file);
        //logger4j.info("#####Extent xml configuration : " + System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        //htmlReporter.loadConfig(System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        return htmlReporter;
    }

    private static void htmlReporSetup() {
        // htmlReporter.config().setChartVisibilityOnOpen(true);
        // report title
        if (PropertyMap.get("legion.cucumber.extent.report.title") != null) {
            htmlReporter.config().setDocumentTitle(PropertyMap.get("legion.cucumber.extent.report.title"));
        }
        // encoding, default = UTF-8
        if (PropertyMap.get("legion.cucumber.extent.report.encoding")!= null) {
            htmlReporter.config().setEncoding(PropertyMap.get("legion.cucumber.extent.report.encoding"));
        }
        // protocol (http, https)
        if (PropertyMap.get("legion.cucumber.extent.report.protocol")!= null) {
            htmlReporter.config().setProtocol(Protocol.valueOf(PropertyMap.get("legion.cucumber.extent.report.protocol")));
        }
        // report or build name
        if (PropertyMap.get("legion.cucumber.extent.report.name")!= null) {
            htmlReporter.config().setReportName(PropertyMap.get("legion.cucumber.extent.report.name"));
        }
        // theme - standard, dark
        if (PropertyMap.get("legion.cucumber.extent.report.theme") != null) {
            htmlReporter.config().setTheme(Theme.valueOf(PropertyMap.get("legion.cucumber.extent.report.theme").toUpperCase()));
        }

        // set timeStamp format
        if (PropertyMap.get("legion.cucumber.extent.report.timestampformat") != null) {
            htmlReporter.config().setTimeStampFormat(PropertyMap.get("legion.cucumber.extent.report.timestampformat"));
        }
        // add custom css
        htmlReporter.config().setCSS("css-string");
        // add custom javascript
        htmlReporter.config().setJS("js-string");
        //setting UTF8 encoding
        htmlReporter.config().setEncoding("UTF-8");
        //appending existing report
        logger4j.info("--------EXTENTREPORT   Setting Append Existing to true -------------------------------");
        /*htmlReporter.setAppendExisting(true);*/

    }

    private static synchronized ExtentReports getExtent() {
        if (extent == null) {
            logger4j.info("------ creating extent report infor --------------------------------");
            extent = new ExtentReports();
            extent.setSystemInfo("Host Name", PropertyMap.get("legion.cucumber.extent.report.host.name"));
            extent.setSystemInfo("Environment", PropertyMap.get("legion.cucumber.extent.report.environment"));
            extent.setSystemInfo("User Name", PropertyMap.get("legion.cucumber.extent.report.user.name"));
            extent.attachReporter(getExtentHtmlReport());
            htmlReporSetup();
        }


        return extent;
    }

    private static synchronized ExtentTest getFeature(String featurName, List<Tag> tags) {
        if (mapFeature.containsKey(featurName)) {
            featureTest.set(mapFeature.get(featurName));
            return mapFeature.get(featurName);
        }
        ExtentTest feature = getExtent().createTest(Feature.class, featurName);
        featureTest.set(feature);
        for (Tag tag : tags) {
            featureTest.get().assignCategory(tag.getName());
        }
        mapFeature.put(featurName, feature);
        return feature;
    }

    public static void flush() {
        logger4j.info("------flushing Thread : " + Thread.currentThread().getId());
        getExtent().flush();
    }


    public static void createFeature(String storyFeatureName, List<Tag> tags) {
        logger4j.info("------Creating Test Feature with Name : " + storyFeatureName + " " + Thread.currentThread().getId());
        getFeature(storyFeatureName, tags);
    }

    public static void createScenarioOutlineNode(String scenarioName) {
        logger4j.info("------Creating Scenario Outline Node : " + Thread.currentThread().getId());
        //if(firstFeature.get() != null && firstFeature.get()){
        ExtentTest node = featureTest.get()
                .createNode(ScenarioOutline.class, scenarioName);
        scenarioOutline.set(node);
        //}
    }

    public static void createExampleCucumber(Examples examples) {
        logger4j.info("------Creating Scenario example cucumber : " + Thread.currentThread().getId());
        ExtentTest test = scenarioOutline.get();

        String[][] data = null;
        List<TableRow> rows = examples.getTableBody();
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            TableRow examplesTableRow = rows.get(i);
            List<TableCell> cells = examplesTableRow.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j).getValue();
            }
        }
        test.info(MarkupHelper.createTable(data));

    }


    public static void createResult(Result result) {
        logger4j.info("------Creating Result cucumber : " + Thread.currentThread().getId());
        if (Result.Type.PASSED.equals(result.getStatus())) {
            ExtentCucumberHelper.addCucumberPassStep();
        } else if (Result.Type.FAILED.equals(result.getStatus())) {
            ExtentCucumberHelper.addCucumberFailStep(result.getError());
        } else if (Result.Type.SKIPPED.equals(result.getStatus())) {
            ExtentCucumberHelper.addCucumberSkipStep();
        } else if (Result.UNDEFINED.equals(result.getStatus())) {
            ExtentCucumberHelper.addCucumberUndefinedStep();
        }
    }


    public static void createScenarioOutlineOrStandard(String scenarioName, String keyword, List<PickleTag> tags) {
        logger4j.info("------ Create Scenario Outline Or Standard : " + Thread.currentThread().getId());
        ExtentTest scenarioNode;
        if (scenarioOutline.get() != null && keyword.trim()
                .equalsIgnoreCase("Scenario Outline")) {
            scenarioNode =
                    scenarioOutline.get().createNode(Scenario.class, scenarioName);
        } else {
            scenarioNode =
                    featureTest.get().createNode(Scenario.class, scenarioName);
        }
        scenario.set(scenarioNode);

        for (PickleTag tag : tags) {
            scenarioNode.assignCategory(tag.getName());
        }
    }

    public static void addCucumberStep(PickleStepTestStep step) {

        stepList().get().add(step);
    }

    private static ThreadLocal<LinkedList<PickleStepTestStep>> stepList() {

        if (stepList.get() == null) {
            stepList.set(new LinkedList<>());
        }
        return stepList;

    }

    public static void addCucumberPassStep() {
        stepTest.get().pass(Result.Type.PASSED.toString());
        String screenShotOnSuccessStep = PropertyMap.get("legion.cucumber.extent.report.screenshotOnSuccess");
        if (screenShotOnSuccessStep != null && !screenShotOnSuccessStep.isEmpty()) {
            try {
                stepTest.get().addScreenCaptureFromPath(takeScreenshot());
            } catch (IOException e) {
                logger4j.error(e.getStackTrace());
            }
        }
    }

    public static void addCucumberFailStep(Throwable error) {
        String stack = PropertyMap.get("legion.cucumber.extent.report.stacktrace.detail");
        if (stack != null && !stack.isEmpty() && stack.equalsIgnoreCase("true")) {
            stepTest.get().fail(error);
        } else {
            stepTest.get().fail(error.getMessage());
        }

        Boolean screenshotDisable = new Boolean(PropertyMap.get("legion.cucumber.extent.report.disable_screenshot_on_failure"));
        if (screenshotDisable.equals(Boolean.FALSE)) {
            try {
                stepTest.get().addScreenCaptureFromPath(takeScreenshot());
            } catch (IOException e) {
                logger4j.error(e.getStackTrace());
            }
        }
    }

    private static void addCucumberSkipStep() {
        stepTest.get().skip(Result.Type.SKIPPED.toString());
    }

    public static PickleStepTestStep pollCucumberStep() {
        return stepList().get().poll();
    }

    private static void addCucumberUndefinedStep() {
        stepTest.get().skip(Result.Type.UNDEFINED.toString());
    }

    public static void matchCucumberStep(String keyword, String name) {
        ExtentTest scenarioTest = scenario.get();
        ExtentTest stepTest = null;
        try {
            stepTest = scenarioTest.createNode(new GherkinKeyword(keyword), name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        String data[][] = null;


        if (data != null) {
            Markup table = MarkupHelper.createTable(data);
            stepTest.info(table);
        }

        ExtentCucumberHelper.stepTest.set(stepTest);
    }

    private static String takeScreenshot() throws IOException {
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) MyThreadLocal.getDriver()).getScreenshotAs(OutputType.FILE);
        name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
        fileLocation = reportPath.concat(File.separator).concat(name);
        FileUtils.copyFile(screenshot, new File(fileLocation));
        return name;
    }

}
