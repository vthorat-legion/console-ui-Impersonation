package com.legion.tests.testframework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.legion.tests.TestBase;
import com.legion.utils.MyThreadLocal;
import com.legion.utils.SimpleUtils;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.legion.test.testrail.TestRailOperation.addTestResultIntoTestRailN;
import static com.legion.utils.MyThreadLocal.*;

//import com.legion.utils.ExtentManager;

	public class LegionTestListener implements ITestListener,IInvokedMethodListener {
		
		public static ExtentTest test ;
		private static ExtentReports extent = ExtentReportManager.getInstance();
		private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();
	
		
		@Override
		public void onTestStart(ITestResult result) {  
			String testName = result.getMethod().getMethodName();
			setLoc(testName);
		}

		@Override
		public void onTestSuccess(ITestResult result) {
			ExtentTestManager.getTest().log(Status.PASS, MarkupHelper.createLabel("Test case Passed:",ExtentColor.GREEN));
		}
		@Override
		public void onTestFailure(ITestResult result) {
			// TODO Auto-generated method stub
//			SimpleUtils.addTestResultIntoTestRail(5,result.getThrowable().toString());
			ExtentTestManager.getTest().log(Status.FAIL, MarkupHelper.createLabel("Test case Failed:",ExtentColor.RED));
			String targetFile = "";
			if (MyThreadLocal.getDriver()!=null){
				targetFile = ScreenshotManager.takeScreenShot();
			} else {
				System.out.println("Session is null!");
			}

			String screenshotLoc = propertyMap.get("Screenshot_Path") + File.separator + targetFile;
			try {
				ExtentTestManager.getTest().addScreenCaptureFromPath("<a href='"+screenshotLoc+ "'>" +"Screenshots"+"</a>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				ExtentTestManager.getTest().fail(result.getThrowable());
			}

		}
	
		@Override
		public void onTestSkipped(ITestResult result) {
			// TODO Auto-generated method stub
			ExtentTestManager.getTest().log(Status.SKIP, "Test skipped");
			if(TestBase.testRailReportingFlag!=null&&MyThreadLocal.getTestCaseExistsFlag()){
				MyThreadLocal.setTestSkippedFlag(true);
			}
			if (result != null && result.getThrowable() != null) {   
				result.getThrowable().printStackTrace();
		        }
			
		}
	
		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onStart(ITestContext context) {
			// TODO Auto-generated method stub
		
		}

		@Override
		public void onFinish(ITestContext context) {
			// TODO Auto-generated method stub
/*if(getTestRailReporting()!=null){
				SimpleUtils.addNUpdateTestCaseIntoTestRun();
				SimpleUtils.addTestResultIntoTestRailN(1,5,context);
			}
*/		}

		@Override
		public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
			// TODO Auto-generated method stub
			if (getVerificationMap() == null) {
				return;
			}	
			
			if(!getVerificationMap().isEmpty() && testResult.getStatus() == ITestResult.SUCCESS){
				ITestContext testContext = Reporter.getCurrentTestResult().getTestContext();
				testContext.getPassedTests().addResult(testResult, Reporter.getCurrentTestResult().getMethod());
				testContext.getPassedTests().getAllMethods().remove(Reporter.getCurrentTestResult().getMethod());
				Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
				Reporter.getCurrentTestResult().setThrowable(new Exception("Found none-fatal error(s) at page level running test steps!"));
				testContext.getFailedTests().addResult(testResult, Reporter.getCurrentTestResult().getMethod());
			}
		}
		
		
		
	}
