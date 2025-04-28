package com.legion.tests.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.legion.pages.DashboardPage;
import com.legion.pages.ProfileNewUIPage;
import com.legion.tests.TestBase;
import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;
import com.legion.utils.SimpleUtils;

public class ProfileNewUITest  extends TestBase{
    
	public enum profilePageTabs{
		aboutMe("About Me"),
		myWorkPreferences("My Work Preferences"),
		myTimeOff("My Time Off");
		private final String value;
		profilePageTabs(final String newValue) {
            value = newValue;
        }
        public String getValue() { return value; }
	}
	
	@Override
	@BeforeMethod
	public void firstTest(Method method, Object[] params) throws Exception {
		this.createDriver((String) params[0], "68", "Linux");
	      visitPage(method);
	      loginToLegionAndVerifyIsLoginDone((String) params[1], (String) params[2], (String) params[3]);
	}
	
	@Automated(automated =  "Automated")
	  @Owner(owner = "Naval")
	  @Enterprise(name = "KendraScott2_Enterprise")
	  @TestName(description = "TP-159: Check the loading of all the web elements on Profile tab")
	  @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	  public void validateAllWebElementLoadedAsStoreManager(String browser, String username, String password, String location)
	  		throws Exception {
				
	      DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
	      SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);  
	      
	      ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
	      profileNewUIPage.clickOnProfileConsoleMenu();
	      SimpleUtils.assertOnFail("Profile Page not loaded Successfully!",profileNewUIPage.isProfilePageLoaded() , false);
	      
	      String profilePageActiveLocation= profileNewUIPage.getProfilePageActiveLocation();
	      SimpleUtils.pass("Profile Page: Active Location '"+profilePageActiveLocation+"' loaded Successfully.");
	      Thread.sleep(1000);
	      SimpleUtils.pass("Profile Page: Active tab '"+profileNewUIPage.getProfilePageActiveTabLabel()+"' loaded Successfully.");
	      
	      boolean isEditingProfileSection = profileNewUIPage.isEditingProfileSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Editing Profile Section not loaded.",isEditingProfileSection , true);
	      boolean isPersonalDetailsSection = profileNewUIPage.isPersonalDetailsSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Personal Details Section not loaded.",isPersonalDetailsSection , true);
	      boolean isChangePasswordButton = profileNewUIPage.isChangePasswordButtonLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Change Password Button not loaded.",isChangePasswordButton , true);
	      boolean isEngagementDetrailsSection = profileNewUIPage.isEngagementDetrailsSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page: Engagement Detrails Section not loaded.",isEngagementDetrailsSection , true);
	      boolean isProfileBadgesSection = profileNewUIPage.isProfileBadgesSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Profile Badges Section not loaded.",isProfileBadgesSection , true);
	      
	      profileNewUIPage.selectProfilePageSubSectionByLabel(profilePageTabs.myWorkPreferences.getValue());
	      if(profileNewUIPage.getProfilePageActiveTabLabel().toLowerCase().
	    		  contains(profilePageTabs.myWorkPreferences.getValue().toLowerCase()))
	      SimpleUtils.pass("Profile Page: Active tab '"+profilePageTabs.myWorkPreferences.getValue()+"' loaded Successfully.");
	      else
	    	  SimpleUtils.fail("Profile Page: '"+profilePageTabs.myWorkPreferences.getValue()+"'Tab not active.", true);
	      
	      boolean isShiftPreferenceSection = profileNewUIPage.isShiftPreferenceSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Shift Preference Section not loaded on 'My Work Preferences' tab.",
	    		  isShiftPreferenceSection , true);
	  	  boolean isMyAvailabilitySection = profileNewUIPage.isMyAvailabilitySectionLoaded();
	  	  SimpleUtils.assertOnFail("Profile Page:  My Availability Section not loaded on 'My Work Preferences' tab.",
	  			  isMyAvailabilitySection , true);
	  	  
	  	profileNewUIPage.selectProfilePageSubSectionByLabel(profilePageTabs.myTimeOff.getValue());
	      if(profileNewUIPage.getProfilePageActiveTabLabel().toLowerCase().
	    		  contains(profilePageTabs.myTimeOff.getValue().toLowerCase()))
	      SimpleUtils.pass("Profile Page: Active tab '"+profilePageTabs.myTimeOff.getValue()+"' loaded Successfully.");
	      else
	    	  SimpleUtils.fail("Profile Page: '"+profilePageTabs.myTimeOff.getValue()+"'Tab not active.", true);
	      
	      boolean isCreateTimeOffButton = profileNewUIPage.isCreateTimeOffButtonLoaded();
	      SimpleUtils.assertOnFail("Profile Page: Create Time off button on 'My Time Off' tab not loaded.",
	    		  isCreateTimeOffButton , true);
	      
	  	boolean isTimeOffPendingBlock = profileNewUIPage.isTimeOffPendingBlockLoaded();
	  	SimpleUtils.assertOnFail("Profile Page: 'Time Off Pending Block' Section on 'My Time Off' tab not Loaded.",
	  			isTimeOffPendingBlock , true);
	  	
		boolean isTimeOffApprovedBlock = profileNewUIPage.isTimeOffApprovedBlockLoaded();
		SimpleUtils.assertOnFail("Profile Page: 'Time Off Approved Block' Section on 'My Time Off' tab not Loaded.",
				isTimeOffApprovedBlock , true);
		
		boolean isTimeOffRejectedBlock = profileNewUIPage.isTimeOffRejectedBlockLoaded();
		SimpleUtils.assertOnFail("Profile Page: 'Time Off Rejected Block' Section on 'My Time Off' tab not Loaded.",
				isTimeOffRejectedBlock , true);
		
		boolean isTimeOffRequestsSection = profileNewUIPage.isTimeOffRequestsSectionLoaded();
		SimpleUtils.assertOnFail("Profile Page: 'Time Off Requests Section' Section on 'My Time Off' tab not Loaded.",
				isTimeOffRequestsSection , true);
		
		int allTimeOffRequestCount = profileNewUIPage.getAllTimeOffRequestCount();
		SimpleUtils.pass("Profile Page: 'My Time Off' tab '"+allTimeOffRequestCount+"' Time off Requests found.");
	}
	
	  @Automated(automated =  "Automated")
	  @Owner(owner = "Naval")
	  @Enterprise(name = "KendraScott2_Enterprise")
	  @TestName(description = "TP-160: Automate Profile tab to give it complete functionality coverage.")
	  @Test(dataProvider = "legionTeamCredentialsByRoles", dataProviderClass=CredentialDataProviderSource.class)
	  public void updateAndValidateProfileFunctionalityCoverageAsStoreManager(String browser, String username, String password, String location)
	  		throws Exception {
				
	      DashboardPage dashboardPage = pageFactory.createConsoleDashboardPage();
	      SimpleUtils.assertOnFail("DashBoard Page not loaded Successfully!",dashboardPage.isDashboardPageLoaded() , false);  
	      
	      ProfileNewUIPage profileNewUIPage = pageFactory.createProfileNewUIPage();
	      profileNewUIPage.clickOnProfileConsoleMenu();
	      SimpleUtils.assertOnFail("Profile Page not loaded Successfully!",profileNewUIPage.isProfilePageLoaded() , false);
	      
	      String profilePageActiveLocation= profileNewUIPage.getProfilePageActiveLocation();
	      SimpleUtils.pass("Profile Page: Active Location '"+profilePageActiveLocation+"' loaded Successfully.");
	      SimpleUtils.pass("Profile Page: Active tab '"+profileNewUIPage.getProfilePageActiveTabLabel()+"' loaded Successfully.");
	      Thread.sleep(1000);
	      boolean isEditingProfileSection = profileNewUIPage.isEditingProfileSectionLoaded();
	      SimpleUtils.assertOnFail("Profile Page:  Editing Profile Section not loaded.",isEditingProfileSection , true);
	      profileNewUIPage.updateUserProfile("Jamie", "Ward", "Mr. Jamie.W","", "", "", "", "","2025550124", "jamie.w@kendrascott2.legion.co");
	      
	      TreeMap<String, String> userProfileEngagementDetails = profileNewUIPage.getUserProfileEngagementDetails();
	      String htmlText = "<table>";
	      for(Map.Entry<String, String> entry : userProfileEngagementDetails.entrySet()) {
	    	  htmlText = htmlText + "<tr><td><b>"+entry.getKey()+"</b></td> <td>"+entry.getValue()+"</td></tr>";
	      }
	      htmlText = htmlText + "</table>";
	      if(userProfileEngagementDetails.size() > 0)
	    	  SimpleUtils.pass("Profile Page User Profile EngagementDetails loaded successfully as displayed below. <br>"+ htmlText);
	      else
	    	  SimpleUtils.fail("Profile Page User Profile EngagementDetails not loaded.", true);
	      
	      // validate  Invite Team Member functionality
	      profileNewUIPage.userProfileInviteTeamMember();
	      profileNewUIPage.userProfileChangePassword("oldPassword", "newPassword", "newPassword");
	      
	      ArrayList<String> badgesDetails = profileNewUIPage.getUserProfileBadgesDetails();
	      for(String badgeText : badgesDetails) {
	    	  SimpleUtils.pass("Profile Page: Badges Section '"+ badgeText +" Badge' found under 'About Me' Tab.");
	      }
	      profileNewUIPage.selectProfilePageSubSectionByLabel(profilePageTabs.myWorkPreferences.getValue());
	      if(profileNewUIPage.getProfilePageActiveTabLabel().toLowerCase().
	    		  contains(profilePageTabs.myWorkPreferences.getValue().toLowerCase()))
	      SimpleUtils.pass("Profile Page: Active tab '"+profilePageTabs.myWorkPreferences.getValue()+"' loaded Successfully.");
	      else
	    	  SimpleUtils.fail("Profile Page: '"+profilePageTabs.myWorkPreferences.getValue()+"'Tab not active.", true);
	      
	      boolean isShiftPreferenceWindowOpen = profileNewUIPage.isShiftPreferenceCollapsibleWindowOpen();
	      if(! isShiftPreferenceWindowOpen)
	    	  profileNewUIPage.clickOnShiftPreferenceCollapsibleWindowHeader();
	      
	      boolean canReceiveOfferFromOtherLocation = true;
	      boolean isVolunteersForAdditional = true;
	      int minHoursPerShift = 20;
	      int maxHoursPerShift = 20;
	      int minShiftLength = 2;
	      int maxShiftLength = 2;
	      int minShiftsPerWeek = 3;
	      int maxShiftsPerWeek = 5;
	      profileNewUIPage.updateMyShiftPreferenceData(canReceiveOfferFromOtherLocation, isVolunteersForAdditional, minHoursPerShift, maxHoursPerShift,
	    		  minShiftLength, maxShiftLength, minShiftsPerWeek, maxShiftsPerWeek);
	      
	      // Verifying Shift Preference Data
	      HashMap<String, String> shiftPreferenceData = profileNewUIPage.getMyShiftPreferenceData();
	      if(canReceiveOfferFromOtherLocation == SimpleUtils.convertYesOrNoToTrueOrFalse(shiftPreferenceData.get("volunteerForAdditionalWork")))
	    	  SimpleUtils.pass("Shift Preference Data: 'Volunteer for additional work' value('"
	    		  +isVolunteersForAdditional+"/"+shiftPreferenceData.get("volunteerForAdditionalWork")+"') matched.");
	      else
	    	  SimpleUtils.fail("Shift Preference Data: 'Volunteer for additional work' value('"
		    		  +isVolunteersForAdditional+"/"+shiftPreferenceData.get("volunteerForAdditionalWork")+"') not matched.", true);
	      
	      if(isVolunteersForAdditional == SimpleUtils.convertYesOrNoToTrueOrFalse(shiftPreferenceData.get("otherPreferredLocations")))
	    	  SimpleUtils.pass("Shift Preference Data: 'Other preferred locations' value('"
	    		  +isVolunteersForAdditional+"/"+shiftPreferenceData.get("otherPreferredLocations")+"') matched.");
	      else
	    	  SimpleUtils.fail("Shift Preference Data: 'Other preferred locations' value('"
		    		  +isVolunteersForAdditional+"/"+shiftPreferenceData.get("otherPreferredLocations")+"') not matched." ,true);
	      
	      HashMap<String, Object> myAvailabilityData = profileNewUIPage.getMyAvailabilityData();
	      String myAvailabilityDataHTML = "";
	      
    	  myAvailabilityDataHTML = "<table>";
    	  for(Map.Entry<String, Object> data : myAvailabilityData.entrySet()) {
	    	  myAvailabilityDataHTML = myAvailabilityDataHTML + "<tr><td><b>"+data.getKey()+"</b></td> <td>"+data.getValue()+"</td></tr>";
	      }
    	  myAvailabilityDataHTML = myAvailabilityDataHTML + "</table>";
	      if(myAvailabilityData.size() > 0)
	    	  SimpleUtils.pass("Profile page: 'My Availability Data loaded as Below.<br>"+myAvailabilityDataHTML);
	      else
	    	  SimpleUtils.fail("Profile Page: 'My Availability Data not loaded.", true);
	    	
	      boolean isMyAvailabilityLocked = profileNewUIPage.isMyAvailabilityLocked();

	      if(isMyAvailabilityLocked) {
	    	  
	    	  
	    	  ArrayList<HashMap<String, ArrayList<String>>> myAvailabilityPreferredAndBusyHoursBeforeUpdate = profileNewUIPage
		    		  .getMyAvailabilityPreferredAndBusyHours();
	    	  String availabilityPreferredAndBusyHoursHTMLBefore = "<table>";
		      for(HashMap<String, ArrayList<String>> preferdAndBusyHours : myAvailabilityPreferredAndBusyHoursBeforeUpdate) {
		    	  availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<tr>";
		    	  for(Map.Entry<String, ArrayList<String>> entry : preferdAndBusyHours.entrySet()) {
		    		  if(entry.getValue().size() > 0) {
		    			  availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<td><b>"
				    			  +entry.getKey()+"</b></td>";
			    		  for(String value : entry.getValue()) {
			    			  availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "<td>"
					    			  +value+"</td>";
			    		  }
			    		  availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</td>";
		    		  }
		    	  }
		    	  availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</tr>";
		      }
		      availabilityPreferredAndBusyHoursHTMLBefore = availabilityPreferredAndBusyHoursHTMLBefore + "</table>";
		      
		      if(myAvailabilityPreferredAndBusyHoursBeforeUpdate.size() > 0)
		    	  SimpleUtils.pass("Profile page: 'My Availability Preferred & Busy Hours Duration Per Day <b>Before Updation</b> loaded as Below.<br>"
		    			  +availabilityPreferredAndBusyHoursHTMLBefore);
		      else
		    	  SimpleUtils.fail("Profile page: 'My Availability Preferred & Busy Hours Duration not loaded", true);
		      
		      //Update Preferred And Busy Hours
		      int sliderIndex = 1;
			  int durationMinutes = 120;
			  String leftOrRightDuration = "Right";
			  String hoursType = "When I prefer to work";
			  String repeatChanges = "This week only";
		      profileNewUIPage.updateLockedAvailabilityPreferredOrBusyHoursSlider(hoursType, sliderIndex, leftOrRightDuration,
		    		  durationMinutes, repeatChanges);
		      
		      hoursType = "When I prefer not to work";
		      sliderIndex = 0;
		      profileNewUIPage.updateLockedAvailabilityPreferredOrBusyHoursSlider(hoursType, sliderIndex, leftOrRightDuration,
		    		  durationMinutes, repeatChanges);
		      
		      ArrayList<HashMap<String, ArrayList<String>>> myAvailabilityPreferredAndBusyHoursAfterUpdate = profileNewUIPage
		    		  .getMyAvailabilityPreferredAndBusyHours();
	    	  String availabilityPreferredAndBusyHoursHTMLAfter = "<table>";
		      for(HashMap<String, ArrayList<String>> preferdAndBusyHours : myAvailabilityPreferredAndBusyHoursAfterUpdate) {
		    	  availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<tr>";
		    	  for(Map.Entry<String, ArrayList<String>> entry : preferdAndBusyHours.entrySet()) {
		    		  if(entry.getValue().size() > 0) {
		    			  availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<td><b>"
				    			  +entry.getKey()+"</b></td>";
			    		  for(String value : entry.getValue()) {
			    			  availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "<td>"
					    			  +value+"</td>";
			    		  }
			    		  availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</td>";
		    		  }
		    	  }
		    	  availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</tr>";
		      }
		      availabilityPreferredAndBusyHoursHTMLAfter = availabilityPreferredAndBusyHoursHTMLAfter + "</table>";
		      
		      if(myAvailabilityPreferredAndBusyHoursAfterUpdate.size() > 0)
		    	  SimpleUtils.pass("Profile page: 'My Availability Preferred & Busy Hours Duration Per Day <b>After Updation</b> loaded as Below.<br>"
		    			  +availabilityPreferredAndBusyHoursHTMLAfter);
		      else
		    	  SimpleUtils.fail("Profile page: 'My Availability Preferred & Busy Hours Duration not loaded", true);
	      }
	      else
	    	  SimpleUtils.report("Profile Page: 'My Availability Section not locked for the week '"
	    			  +profileNewUIPage.getMyAvailabilityData().get("activeWeek")+"'");
	      
	      
	      
	      // My Time Off Tab
	      profileNewUIPage.selectProfilePageSubSectionByLabel(profilePageTabs.myTimeOff.getValue());
	      if(profileNewUIPage.getProfilePageActiveTabLabel().toLowerCase().
	    		  contains(profilePageTabs.myTimeOff.getValue().toLowerCase()))
	      SimpleUtils.pass("Profile Page: Active tab '"+profilePageTabs.myTimeOff.getValue()+"' loaded Successfully.");
	      else
	    	  SimpleUtils.fail("Profile Page: '"+profilePageTabs.myTimeOff.getValue()+"'Tab not active.", true);
	      int AllTimeOffRequestCount = profileNewUIPage.getAllTimeOffRequestCount();
	      SimpleUtils.pass("My Time Off: Total time off requests are '"+AllTimeOffRequestCount+"'.");
	      HashMap<String, Integer> timeOffRequestsData = profileNewUIPage.getTimeOffRequestsStatusCount();
	      for(Map.Entry<String, Integer> entry : timeOffRequestsData.entrySet()) {
	    	  SimpleUtils.pass("My Time Off: Time off requests count found for status "+entry.getKey()+" '"+entry.getValue()+"'.");
	      }
	      
	      
	      // Create new Time off request
	      String timeOffStartDate = "Apr 5, 2019";
	      String timeOffEndDate = "Apr 6, 2019";
	      String timeOffReasonLabel = "VACATION";
	      String timeOffExplanationText = "Sample Explanation Text";
	      profileNewUIPage.createNewTimeOffRequest(timeOffReasonLabel, timeOffExplanationText);
	      String expectedRequestStatus = "Pending";
	      String requestStatus = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel,
	    		  timeOffExplanationText, timeOffStartDate, timeOffEndDate);
	      
          if(requestStatus.toLowerCase().contains(expectedRequestStatus.toLowerCase())) {
          	SimpleUtils.pass("Profile Page: New Time Off Request status is '"+requestStatus+"'.");
          	String action = "Cancel";
            profileNewUIPage.approveOrRejectTimeOffRequestFromToDoList(timeOffReasonLabel, timeOffStartDate,
          		  timeOffEndDate, action);
            String expectedStatusAfterUpdate = "CANCELLED";
            String requestStatusAfterUpdate = profileNewUIPage.getTimeOffRequestStatus(timeOffReasonLabel,
  	    		  timeOffExplanationText, timeOffStartDate, timeOffEndDate);
            if(expectedStatusAfterUpdate.toLowerCase().contains(requestStatusAfterUpdate)) {
            	SimpleUtils.pass("Profile Page: new Time Off Request status updated '"+requestStatusAfterUpdate+"'.");
            }
            else
            	SimpleUtils.fail("Profile Page: New Time Off Request status is  not updated '"+ expectedStatusAfterUpdate
	          			+"', status found '"+requestStatusAfterUpdate+"'.", true);
          }
          else
          	SimpleUtils.report("Profile Page: New Time Off Request status is  not '"+ expectedRequestStatus
	          			+"', status found '"+requestStatus+"'.");
	}
	
}
