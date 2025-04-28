package com.legion.tests.testframework;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {
	
	public static ExtentReports extent;
//    private static final String OUTPUT_FOLDER = "test-output/";
    private static final String OUTPUT_FOLDER = ".//Reports//";

    private static final String FILE_NAME = "Impersonation.html";
    
    public static ExtentReports getInstance() {
    	
		if (extent == null)
    		createInstance(OUTPUT_FOLDER);
        return extent;
    }
    
    public static synchronized ExtentReports createInstance(String fileName) {
        //create output folder if the file not exist
        File reportDir= new File(OUTPUT_FOLDER);
        if(!reportDir.exists()&& !reportDir .isDirectory()){
            reportDir.mkdir();
        }
//        String reportPath = createReportPath(fileName);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
//        htmlReporter.config().setDocumentTitle("UI Automation Test Report");
//        htmlReporter.config().setReportName("UI Automation Test Report");
      //  htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
       // htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(fileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("UI Automation Test Report");
     // add custom css
        htmlReporter.config().setCSS("css-string");

        // add custom javascript
        htmlReporter.config().setJS("js-string");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }
    
    public static String createReportPath(String fileName){
    	
    	Date date=new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");  
		String strDate = formatter.format(date);  
		String strDateFinal = strDate.replaceAll(" ", "_");
		String reportName=date.toString().replace(":", "_").replace(" ", "_")+".html";
		File file = new File(fileName + File.separator + "AutomationReport" + File.separator + strDateFinal);
		if (!file.exists()) {
			boolean flag=file.mkdirs();
		}	
		String reportPath =fileName + File.separator + "AutomationReport" + File.separator + strDateFinal + File.separator + reportName;
		return reportPath;
    }

}
