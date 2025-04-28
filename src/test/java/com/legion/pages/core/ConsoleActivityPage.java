package com.legion.pages.core;

import com.legion.pages.ActivityPage;
import com.legion.pages.BasePage;
import com.legion.pages.DashboardPage;
import com.legion.tests.core.ActivityTest;
import com.legion.utils.SimpleUtils;
import cucumber.api.java.hu.Ha;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.ClickElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;
import static com.legion.utils.MyThreadLocal.loc;

public class ConsoleActivityPage extends BasePage implements ActivityPage {

	public ConsoleActivityPage() {
		PageFactory.initElements(getDriver(), this);
	}

	// Added by Nora
	@FindBy (className = "bell-container")
	private WebElement activityBell;
	@FindBy (className = "notification-bell-popup-filters-filter")
	private List<WebElement> activityFilters;
	@FindBy (className = "notification-bell-popup-filters-filter-title")
	private WebElement filterTitle;
	@FindBy (className = "notification-container")
	private List<WebElement> activityCards;

	@FindBy (css = "[ng-click=\"close()\"]")
	private WebElement closeActivityFeedBtn;
	@FindBy (className = "notification-bell-popup-container")
	private WebElement activityContainer;

	@Override
	public List<String> getShiftSwapDataFromActivity(String requestUserName, String respondUserName) throws Exception {
		List<String> swapData = new ArrayList<>();
		if (areListElementVisible(activityCards, 5)) {
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().contains(requestUserName) && message.getText().contains(respondUserName)) {
				List<WebElement> swapResults = activityCards.get(0).findElements(By.cssSelector("[ng-repeat*=\"swapData\"]"));
				if (swapResults != null && swapResults.size() > 0) {
					for(WebElement swapResult : swapResults) {
						WebElement date = swapResult.findElement(By.className("date"));
						WebElement nameAndTitle = swapResult.findElement(By.className("name-and-title"));
						if (date != null && nameAndTitle != null) {
							swapData.add(nameAndTitle.getText() + "\n" + date.getText());
							SimpleUtils.report("Get the swap date: " + date.getText() + " and swap name title: " + nameAndTitle.getText() + " Successfully!");
						}else {
							SimpleUtils.fail("Failed to find the date and name elements!", false);
						}
					}
				}else {
					SimpleUtils.fail("Failed to find the swap elements!", false);
				}
			}
		}
		if (swapData.size() != 2) {
			SimpleUtils.fail("Failed to get the swap data!", false);
		}
		return swapData;
	}

	@Override
	public boolean isActivityContainerPoppedUp() throws Exception {
		boolean isLoaded = false;
		if (isElementLoaded(activityContainer, 5)) {
			isLoaded = true;
			SimpleUtils.pass("Activity pop up Container loaded Successfully!");
		}
		return isLoaded;
	}

	@Override
	public void verifyFiveActivityButtonsLoaded() throws Exception {
		if (areListElementVisible(activityFilters, 10) && activityFilters.size() == 5) {
			if (activityFilters.get(0).getAttribute("src").contains("time-off")) {
				SimpleUtils.pass("Find the first filter 'Time Off' Successfully!");
			}else {
				SimpleUtils.fail("Filter 'Time Off' not loaded Successfully!", false);
			}
			if (activityFilters.get(1).getAttribute("src").contains("offer")) {
				SimpleUtils.pass("Find the second filter 'Shift Offer' Successfully!");
			}else {
				SimpleUtils.fail("Filter 'Shift Offer' not loaded Successfully!", false);
			}
			if (activityFilters.get(2).getAttribute("src").contains("shift-swap")) {
				SimpleUtils.pass("Find the third filter 'Shift Swap' Successfully!");
			}else {
				SimpleUtils.fail("Filter 'Shift Swap' not loaded Successfully!", false);
			}
			if (activityFilters.get(3).getAttribute("src").contains("team")) {
				SimpleUtils.pass("Find the forth filter 'Profile Update' Successfully!");
			}else {
				SimpleUtils.fail("Filter 'Profile Update' not loaded Successfully!", false);
			}
			if (activityFilters.get(4).getAttribute("src").contains("calendar")) {
				SimpleUtils.pass("Find the fifth filter 'Schedule' Successfully");
			}else {
				SimpleUtils.fail("Filter 'Schedule' not loaded Successfully!", false);
			}
		}
	}

	@Override
	public void verifyActivityBellIconLoaded() throws Exception {
		if (isElementLoaded(activityBell, 10)) {
			SimpleUtils.pass("Activity Bell Icon Loaded Successfully!");
		}else {
			SimpleUtils.fail("Activity Bell Icon not Loaded Successfully!", false);
		}
	}

	@Override
	public void verifyClickOnActivityIcon() throws Exception {
		if (isElementLoaded(activityBell, 10)) {
			clickTheElement(activityBell);
			waitForSeconds(3);
			if (areListElementVisible(activityFilters, 10)) {
				SimpleUtils.pass("Click on Activity Bell icon Successfully!");
			}else {
				SimpleUtils.fail("Activity Layout failed to load!", false);
			}
		}else {
			SimpleUtils.fail("Activity Bell Icon not Loaded Successfully!", false);
		}
	}

	@Override
	public void clickActivityFilterByIndex(int index, String filterName) throws Exception {
		if (areListElementVisible(activityFilters, 10)) {
			if (index < activityFilters.size()) {
				clickTheElement(activityFilters.get(index));
				waitForSeconds(5);
				if (isElementLoaded(filterTitle, 10)) {
					if (filterName.equalsIgnoreCase(filterTitle.getText().replaceAll("\\s*", ""))) {
						SimpleUtils.pass("Switch to :" + filterTitle.getText() + " tab Successfully!");
					}else {
						SimpleUtils.fail("Failed to switch to: " + filterName + " page, current page is: "
								+ filterTitle.getText(), false);
					}
				}
			}else {
				SimpleUtils.fail("Index: " + index + " is out of range, the size of Activity Filter is: " +
						activityFilters.size(), false);
			}
		}else {
			SimpleUtils.fail("Activity Filters failed to load!", false);
		}
	}

	@Override
	public void verifyNewShiftSwapCardShowsOnActivity(String requestUserName, String respondUserName, String actionLabel,
													  boolean isNewLabelShows, String location) throws Exception {
		String newStatus = "New";
		String expectedMessage = actionLabel + " to swap shifts";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			if (isNewLabelShows) {
				WebElement newLabel = activityCards.get(0).findElement(By.className("notification-new-label"));
				if (newLabel != null && newLabel.getText().equalsIgnoreCase(newStatus)) {
					SimpleUtils.pass("Verified 'New' label shows correctly");
				}else {
					SimpleUtils.fail("Failed to find a new business profile update activity!", false);
				}
			}
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().contains(requestUserName) && message.getText().contains(respondUserName)
					&& message.getText().toLowerCase().contains(expectedMessage)
					&& isElementLoaded(activityCards.get(0).findElement(By.cssSelector(".notification-content .location")))
					&& activityCards.get(0).findElement(By.cssSelector(".notification-content .location")).getText().toLowerCase().contains("@"+location.toLowerCase())) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + message.getText(), false);
			}
		}else {
			SimpleUtils.fail("Shift Swap Activity failed to Load!", false);
		}
	}

	@Override
	public void approveOrRejectShiftSwapRequestOnActivity(String requestUserName, String respondUserName, String action) throws Exception {
		WebElement shiftSwapCard = activityCards.get(0);
		if (shiftSwapCard != null) {
			List<WebElement> actionButtons = shiftSwapCard.findElements(By.className("notification-buttons-button"));
			if (actionButtons != null && actionButtons.size() == 2) {
				for (WebElement button : actionButtons) {
					if (action.equalsIgnoreCase(button.getText())) {
						click(button);
						break;
					}
				}
				// Wait for the card to change the status message, such as approved or rejected
				waitForSeconds(5);
				clickTheElement(activityFilters.get(2));
				waitForSeconds(1);
				clickTheElement(activityFilters.get(2));
				if (areListElementVisible(activityCards, 15)) {
					WebElement approveOrRejectMessage = activityCards.get(0).findElement(By.cssSelector(".notification-approved"));
					if (approveOrRejectMessage != null && approveOrRejectMessage.getText().toLowerCase().contains(action.toLowerCase())) {
						SimpleUtils.pass(action + " the shift swap request for: " + requestUserName + " and " + respondUserName + " Successfully!");
					} else {
						SimpleUtils.fail(action + " message failed to load!", false);
					}
				}
			}else {
				SimpleUtils.fail("Action buttons: Approve and Reject failed to load!", false);
			}
		}else {
			SimpleUtils.fail("Failed to find a new Shift Swap activity!", false);
		}
	}

	//Added by Estelle
	@Override
	public void verifyActivityOfPublishSchedule(String requestUserName) throws Exception {
		String expectedMessage = "published the schedule for";
		WebElement shiftSwapCard = null;
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().contains(requestUserName) && message.getText().toLowerCase().contains(expectedMessage)) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
				shiftSwapCard = activityCards.get(0);
			}else if( message.getText().toLowerCase().contains("no activities available for the selected filter")){
				SimpleUtils.report("No activities available for the selected filter");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + requestUserName + ", "
						+  expectedMessage + "! Actual card is: " + message.getText(), false);
			}

		}else {
			SimpleUtils.fail("Schedule Activity failed to Load", false);
		}

	}

	@Override
	public void verifyActivityOfUpdateSchedule(String requestUserName) throws Exception {
		String expectedMessage = "updated the schedule for";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().contains(requestUserName) && message.getText().toLowerCase().contains(expectedMessage)) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
			}else if( message.getText().toLowerCase().contains("no activities available for the selected filter")){
				SimpleUtils.report("No activities available for the selected filter");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + requestUserName + ", "
						+  expectedMessage + "! Actual card is: " + message.getText(), false);
			}
		}else {
			SimpleUtils.fail("Schedule Activity failed to Load", false);
		}

	}
	@Override
	public void verifyClickOnActivityCloseButton() throws Exception {
		waitForSeconds(10);
		if (isElementLoaded(activityBell, 15)) {
			clickTheElement(activityBell);
			SimpleUtils.pass("Click on Activity Close Button Successfully!");
		}else {
			SimpleUtils.fail("Activity Close Button failed to load!", false);
		}

	}

	@Override
	public void verifyActivityOfShiftOffer(String requestUserName, String location) throws Exception {
		String expectedMessage = "open shift";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			String messageText = message.getText();
			if (message != null && messageText.contains(requestUserName)
					&& messageText.toLowerCase().contains(expectedMessage) && messageText.toLowerCase().contains("@"+location.toLowerCase())) {
				SimpleUtils.pass("Find Card: " + messageText + " Successfully!");
			}else if( messageText.toLowerCase().contains("no activities available for the selected filter")) {
				SimpleUtils.report("No activities available for the selected filter");
			}else{
				SimpleUtils.fail("Failed to find the card that is new and contain: " + requestUserName + ", "
						+ expectedMessage + "! Actual card is: " + messageText, false);
			}
		}else {
			SimpleUtils.fail("Shift Offer Activity failed to Load!", false);
		}
	}

	@FindBy (css = ".notification-approved")
	private List<WebElement> approvedSignature;

	@Override
	public void approveOrRejectShiftOfferRequestOnActivity(String requestUserName, String action) throws Exception {
		if (areListElementVisible(activityCards, 15)) {
			SimpleUtils.pass("The request was received by SM!");
		} else {
			SimpleUtils.fail("There's no approve request found!", false);
		}
		WebElement shiftSwapCard = activityCards.get(0);
		if (shiftSwapCard != null) {
			List<WebElement> actionButtons = shiftSwapCard.findElements(By.className("notification-buttons-button"));
			if (actionButtons != null && actionButtons.size() == 2) {
				for (WebElement button : actionButtons) {
					if (action.equalsIgnoreCase(button.getText().trim())) {
						clickTheElement(button);
						break;
					}
				}
				// Wait for the card to change the status message, such as approved or rejected
				waitForSeconds(5);
				if (areListElementVisible(activityCards, 15) && areListElementVisible(approvedSignature, 15)) {
					WebElement approveOrRejectMessage = activityCards.get(0).findElement(By.className("notification-approved"));
					if (approveOrRejectMessage != null && approveOrRejectMessage.getText().toLowerCase().contains(action.toLowerCase())) {
						SimpleUtils.pass(action + " the shift offer request for: " + requestUserName +  " Successfully!");
					} else {
						SimpleUtils.fail(action + " message failed to load!", false);
					}
				}
			}else {
				SimpleUtils.fail("Action buttons: Approve and Reject failed to load!", false);
			}
		}else {
			SimpleUtils.fail("Failed to find a new Shift Offer activity!", false);
		}
	}

	@Override
	public boolean isApproveRejectBtnsLoaded(int index) throws Exception {
		WebElement shiftSwapCard = activityCards.get(index);
		if (shiftSwapCard != null) {
			List<WebElement> actionButtons = shiftSwapCard.findElements(By.className("notification-buttons-button"));
			if (areListElementVisible(actionButtons, 10)) {
				return true;
			}
		}
		return false;
	}
	@FindBy(css = ".lg-toast p")
	private WebElement msgOnTop;
	@Override
	public void verifyApproveShiftOfferRequestAndGetErrorOnActivity(String requestUserName, String expectedMessage) throws Exception {
		if (activityCards.size()>0) {
			for (int i = 0; i<activityCards.size(); i++){
				WebElement shiftSwapCard = activityCards.get(i);
//				if (i>3){
//					SimpleUtils.fail("Didn't find the right notification!", false);
//				}
				List<WebElement> actionButtons = shiftSwapCard.findElements(By.className("notification-buttons-button"));
				WebElement message = shiftSwapCard.findElement(By.className("notification-content-message"));
				if (actionButtons != null && actionButtons.size() == 2 && message.getText().contains(requestUserName)) {
					for (WebElement button : actionButtons) {
						if ("approve".equalsIgnoreCase(button.getText())) {
							clickTheElement(button);
//							waitForSeconds(1);
							if (isElementLoaded(msgOnTop, 20)) {
								String errorMessage = msgOnTop.getText();
								if (errorMessage.contains(expectedMessage)) {
									SimpleUtils.pass("Verified Message shows correctly!");
								}else {
									SimpleUtils.fail("Message on top is incorrect, expected is: " + expectedMessage + ", but actual is: " + message, false);
								}
							}else {
								SimpleUtils.fail("Message on top not loaded Successfully!", false);
							}
							break;
						}
					}
					// check the status of the card.
					SimpleUtils.assertOnFail("Approve and Reject buttons should be there!", areListElementVisible(activityCards.get(i).findElements(By.className("notification-buttons-button"))), false);
				}
			}
		}else {
			SimpleUtils.fail("Failed to find a new Shift Offer activity!", false);
		}
	}


    @Override
    public void verifyNewBusinessProfileCardShowsOnActivity(String userName, boolean isNewLabelShows) throws Exception {
		boolean isFound = false;
        String newStatus = "New";
        String expectedMessage = userName + " updated business profile photo.";
        waitForSeconds(5);
        if (areListElementVisible(activityCards, 15)) {
        	for (WebElement activityCard : activityCards) {
				WebElement message = activityCard.findElement(By.className("notification-content-message"));
				if (message != null && message.getText().equals(expectedMessage)) {
					List<WebElement> actionButtons = activityCard.findElements(By.className("notification-buttons-button"));
					if (actionButtons != null && actionButtons.size() == 2) {
						if (actionButtons.get(0).getText().equals("Approve") && actionButtons.get(1).getText().equals("Reject")) {
							SimpleUtils.pass("Approve and Reject buttons loaded Successfully on Business Profile Update Card!");
							if (isNewLabelShows) {
								WebElement newLabel = activityCard.findElement(By.className("notification-new-label"));
								if (newLabel != null && newLabel.getText().equalsIgnoreCase(newStatus) && message != null) {
									SimpleUtils.pass("Verified 'New' label shows correctly");
								} else {
									SimpleUtils.fail("Failed to find a new business profile update activity!", false);
								}
							}
							isFound = true;
							break;
						} else {
							SimpleUtils.fail("Approve and Reject buttons are not loaded on Business Profile Update Card!", false);
						}
					}
				}
			}
        	if (!isFound) {
				SimpleUtils.fail("Failed to find the card with the message: " + expectedMessage, false);
			}
        }else {
            SimpleUtils.fail("Business Profile Update Activity failed to Load!", false);
        }
    }

    @Override
    public void verifyNewWorkPreferencesCardShowsOnActivity(String userName) throws Exception {
        String expectedMessage = userName + " updated work preferences.";
        waitForSeconds(5);
        if (areListElementVisible(activityCards, 15)) {
            WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
            if (message != null && message.getText().equals(expectedMessage)) {
                SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
            }else {
                SimpleUtils.fail("Failed to find the card that contains: " + expectedMessage
                        + "! Actual card is: " + message.getText(), false);
            }
        }else {
            SimpleUtils.fail("Profile Update Activity failed to Load!", false);
        }
    }

    /*
     * Added by Haya
     * Verify the notification message and detail for time off request
     * */
    public void verifyTheNotificationForReqestTimeOff(String requestUserName, String startTime, String endTime,String timeOffAction) throws Exception {
        boolean isFound = false;
        String expectedCancelInfo = "Cancelled on ";
    	String expectedMessage = "";
    	if (startTime.equalsIgnoreCase(endTime)) {
			expectedMessage = requestUserName +" requested time off on " + startTime.replace(",","") + ".";
		} else {
			expectedMessage = requestUserName +" requested time off on " + startTime.replace(",","") +" - " + endTime.replace(",","") + ".";
		}

        String actualMessage = "";
        waitForSeconds(5);
        if (areListElementVisible(activityCards, 15)) {
        	for (WebElement activityCard : activityCards) {
				actualMessage = activityCard.findElement(By.className("notification-content-message")).getText();
				if (actualMessage != null && actualMessage.equals(expectedMessage)) {
					SimpleUtils.pass("Find Card: " + actualMessage + " Successfully!");
					isFound = true;
					if (timeOffAction.toLowerCase().contains("cancel")) {
						waitForSeconds(5);
						String cancelInfo = activityCard.findElement(By.cssSelector(".notification-approved")).getText();
						if (cancelInfo.contains(expectedCancelInfo)) {
							SimpleUtils.pass("Cancel Info load!");
						} else {
							SimpleUtils.fail("Cancel Info is not loaded!", false);
						}
					}
					//check the detail
					if (timeOffAction.equals("requested")) {
						waitForSeconds(3);
						WebElement detail = activityCard.findElement(By.cssSelector("div[ng-if=\"canShowDetails()\"]"));
						if (isElementLoaded(detail, 10)) {
							click(detail);
							click(detail);
							SimpleUtils.pass("detail load!");
						} else {
							SimpleUtils.fail("detail is not loaded!", false);
						}
					}
					break;
				}
			}
        }else {
            SimpleUtils.fail("Time Off Request Activity failed to Load!", false);
        }
        if (!isFound) {
			SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + actualMessage, false);
		}
    }

	@FindBy(xpath = "//span[contains(text(),'Work Preferences')]")
	WebElement workPreferTab;
    @Override
	public void goToProfileLinkOnActivity() throws Exception {
		WebElement timeOffCard = activityCards.get(0);
		String approveOrRejectMessage = "";
		if (timeOffCard != null) {
			//check the go to profile link
			if (isElementLoaded(timeOffCard.findElement(By.cssSelector(".pushout-button")))) {
				SimpleUtils.pass("The go to pofile link loaded Successfully!");
				clickTheElement(timeOffCard.findElement(By.cssSelector(".pushout-button")));
				if(isElementLoaded(workPreferTab))
					SimpleUtils.pass("The TM's prifile page loaded Successfully!");
				else
					SimpleUtils.fail("The TM's prifile page failed to load!", false);
			} else {
				SimpleUtils.fail("The go to profile link failed to load!", false);
			}
		} else {
			SimpleUtils.fail("Failed to find a new activity!", false);
		}
	}


    @Override
    public void approveOrRejectTTimeOffRequestOnActivity(String requestUserName, String respondUserName, String action) throws Exception {
        WebElement timeOffCard = activityCards.get(0);
        String approveOrRejectMessage = "";
        if (timeOffCard != null) {
            List<WebElement> actionButtons = timeOffCard.findElements(By.className("notification-buttons-button"));
            if (actionButtons != null && actionButtons.size() == 2) {
                for (WebElement button : actionButtons) {
                    if (action.equalsIgnoreCase(button.getText())) {
                        click(button);
                        break;
                    }
                }
                // Wait for the card to change the status message, such as approved or rejected
                waitForSeconds(3);
                //refresh the filter and can see the approved or reject info.
				clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
				clickActivityFilterByIndex(ActivityTest.indexOfActivityType.ProfileUpdate.getValue(), ActivityTest.indexOfActivityType.ProfileUpdate.name());
                if (areListElementVisible(activityCards, 15)) {
                    approveOrRejectMessage = activityCards.get(0).findElement(By.className("notification-approved")).getText();
                    if (approveOrRejectMessage != null && approveOrRejectMessage.toLowerCase().contains(action.toLowerCase())) {
                        SimpleUtils.pass(action + " the request for: " + requestUserName + " and " + respondUserName + " Successfully!");
                    } else {
                        SimpleUtils.fail(action + " message failed to load!", false);
                    }
                }
            }else {
                SimpleUtils.fail("Action buttons: Approve and Reject failed to load!", false);
            }
        }else {
            SimpleUtils.fail("Failed to find a new activity!", false);
        }
    }

    @Override
    public void closeActivityWindow() throws Exception {
        if (isElementLoaded(activityBell, 10)) {
            clickTheElement(activityBell);
        }else {
            SimpleUtils.fail("Close button is not Loaded Successfully!", false);
        }
    }

    @Override
    public void verifyNoNotificationForActivateTM() throws Exception {
        String actualMessage = "";
        if (areListElementVisible(activityCards, 15)) {
            actualMessage = activityCards.get(0).findElement(By.className("notification-content-message")).getText();
            if (actualMessage != null && !actualMessage.toLowerCase().contains("activated")) {
                SimpleUtils.pass("No message for activating team menmber!");
            }else {
                SimpleUtils.fail("There is message for activating team member! : " + actualMessage, false);
            }
        }else {
            SimpleUtils.fail("Notifications failed to Load!", false);
        }
    }

    @Override
    public void verifyNotificationForUpdateAvailability(String requestName,String isApprovalRequired,String requestOrCancelLabel,String weekInfo,String repeatChange) throws Exception {
        String expectedMessage = requestName+" updated availability.";
        String actualMessage = "";
        if (isApprovalRequired.toLowerCase().contains("not required")){
            if (areListElementVisible(activityCards, 15)) {
                actualMessage = activityCards.get(0).findElement(By.className("notification-content-message")).getText();
                if (actualMessage != null && actualMessage.equals(expectedMessage)) {
                    SimpleUtils.pass("Find Card: " + actualMessage + " Successfully!");
                }else {
                    SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + actualMessage, false);
                }
            }else {
                SimpleUtils.fail("Profile update Activity failed to Load!", false);
            }
        } else if (requestOrCancelLabel.toLowerCase().contains("requested")) {// approval required!
                if (areListElementVisible(activityCards, 15)) {
                    expectedMessage = requestName+" requested an availability change.";
                    actualMessage = activityCards.get(0).findElement(By.className("notification-content-message")).getText();
                    if (actualMessage != null && actualMessage.equals(expectedMessage)) {
                        SimpleUtils.pass("Find Card: " + actualMessage + " Successfully!");
                        waitForSeconds(3);
                        WebElement detail = activityCards.get(0).findElement(By.cssSelector("div[ng-if=\"canShowDetails()\"]"));
                        if (isElementLoaded(detail,10)){
                            click(detail);
                            verifyAvailabilityNotificationDetail(weekInfo,repeatChange);
                            click(detail);
                            SimpleUtils.pass("detail load!");
                        }else{
                            SimpleUtils.fail("detail is not loaded!",true);
                        }
                    }else {
                        SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + actualMessage, false);
                    }
                }else {
                    SimpleUtils.fail("Profile update Activity failed to Load!", false);
                }
        }
    }

    private void verifyAvailabilityNotificationDetail(String weekInfo,String repeatChange) throws Exception{
        verifyAvailabilityChangeHourDetail();
        String dateInfo = activityCards.get(0).findElement(By.cssSelector("div[class=\"notification-details-table\"] .date")).getText();
        if (repeatChange.toLowerCase().contains("this week only")){
            if (dateInfo.replace(" - ", "-").contains(weekInfo.replace(" - ", "-"))){
                SimpleUtils.pass("Week info of availability change is correct! WeekInfo: "+dateInfo);
            } else {
                SimpleUtils.fail("week info is not correct! expected weekinfo is: "+weekInfo+" actual is: "+dateInfo,false);
            }
        } else if (repeatChange.toLowerCase().contains("repeat forward")){
            if (dateInfo.replace(" - ", "-").contains(weekInfo.replace(" - ", "-"))){
                SimpleUtils.pass("Week info of availability change is correct! WeekInfo: "+dateInfo);
            } else {
                SimpleUtils.fail("week info is not correct! actual result is: " + dateInfo, false);
            }
        } else{
            SimpleUtils.fail("repeat change label doesn't match anyone", false);
        }
    }

    //added by Haya
    private String changeDateFormat(String dateMMMddString) throws Exception{
        String result = dateMMMddString;
        if (dateMMMddString != null){
            if (dateMMMddString.split(" ")[1].length()==1){
                result = dateMMMddString.split(" ")[0]+" 0"+dateMMMddString.split(" ")[1];
                return result;
            }
        }
        return result;
    }

    //added by Haya
    private void verifyAvailabilityChangeHourDetail() throws Exception{
        double hoursTotal =0;
        double hoursDetail = 0;
        WebElement currentAvailability = activityCards.get(0).findElement(By.cssSelector("div[class=\"notification-details-table\"] .availability .current"));
        WebElement newAvailability = activityCards.get(0).findElement(By.cssSelector("div[class=\"notification-details-table\"] .availability .new"));
        double a=Double.parseDouble(newAvailability.getText().replace("\n"," ").split(" ")[2]);
        double b=Double.parseDouble(currentAvailability.getText().split(" ")[2]);
        hoursTotal = a - b;
        List <WebElement> availabilityChangeDays= activityCards.get(0).findElements(By.cssSelector("div[class=\"notification-details-table\"] .availability-week-day-change"));
        if (areListElementVisible(availabilityChangeDays,5) && availabilityChangeDays.size() > 0){
           for (WebElement availabilityChangeDay:availabilityChangeDays) {
               WebElement iconTriage = availabilityChangeDay.findElement(By.cssSelector("div[class=\"notification-details-table\"] .availability-week-day-change div[ng-if=\"dayAvailability.icon\"] "));
               if (iconTriage.getAttribute("class").toLowerCase().contains("triangle-down")){
                   double down = Double.parseDouble(availabilityChangeDay.getText().trim());
                   hoursDetail = hoursDetail - down;
               } else {
                   hoursDetail = hoursDetail + Double.parseDouble(availabilityChangeDay.getText().trim());
               }
           }
           if (hoursDetail==hoursTotal){
               SimpleUtils.pass("total availability change hour match summary of every day change hour!");
           } else{
               SimpleUtils.fail("total availability change hour doesn't match summary of every day change hour!", true);
           }
        } else{
            SimpleUtils.fail("No availability change in detail info!",true);
        }
    }

    //Added By Julie
    @FindBy (className = "notification-bell-popup-header-container")
    public WebElement notificationBellPopupHeader;
    @FindBy (css = ".notification-bell-popup-notifications-container.empty")
    public WebElement notificationsContainerEmpty;
	@FindBy (css = ".notification-bell-popup-notifications-container")
	public WebElement notificationsContainer;

    @Override
    public boolean isActivityBellIconLoaded() throws Exception {
        boolean isLoaded = false;
        if (isElementLoaded(activityBell, 10))
            isLoaded = true;
        return isLoaded;
    }

    @Override
    public void verifyTheContentOnActivity() throws Exception {
        if (isElementEnabled(notificationBellPopupHeader, 10)) {
            if (notificationBellPopupHeader.getText().contains("Activities") && activityFilters.size() == 5 ) {
                SimpleUtils.pass("Activities window appears successfully, the content in every activity tab will be verified in other test cases");
            } else {
                SimpleUtils.fail("Activities window's content is not as expected",true);
            }
        } else {
            SimpleUtils.fail("Failed to load Activities window",true);
        }
    }

    @Override
    public void verifyTheContentOfShiftSwapActivity(String location) throws Exception {
    	waitForSeconds(3);
        if (isElementLoaded(filterTitle,10) && (isElementLoaded(notificationsContainer, 10) || isElementLoaded(notificationsContainerEmpty, 10))) {
            if (filterTitle.getText().contains("Shift Swap")) {
                if ((notificationsContainer.getText().contains("requested to swap shifts") || notificationsContainer.getText().contains("agreed to cover")) && notificationsContainer.getText().toLowerCase().contains("@"+location.toLowerCase())) {
                    SimpleUtils.pass("The content of shift swap activity displays successfully");
                } else if (notificationsContainerEmpty.getText().contains("No activities available")) {
                    SimpleUtils.pass("No activities available for the selected filter");
                } else SimpleUtils.fail("The content of shift swap activity displays incorrectly", false);
            } else SimpleUtils.fail("The content of Shift Swap Activity is incorrect", true);
        } else SimpleUtils.fail("Shift Swap Activity failed to Load",true);
    }

    @Override
    public WebElement verifyNewShiftCoverCardShowsOnActivity(String requestUserName, String respondUserName, String location) throws Exception {
        String expectedMessage = "agreed to cover";
        WebElement shiftCoverCard = null;
        waitForSeconds(5);
        Boolean isShiftCoverCardPresent = false;
        WebElement message = null;
        if (areListElementVisible(activityCards, 15)) {
            for (WebElement activityCard: activityCards) {
                message = activityCard.findElement(By.className("notification-content-message"));
                if (message != null && message.getText().toLowerCase().contains(requestUserName.toLowerCase()) && message.getText().contains(respondUserName)
                        && message.getText().toLowerCase().contains(expectedMessage) && message.getText().toLowerCase().contains("@" + location.toLowerCase())) {
                    SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
                    shiftCoverCard = activityCard;
                    isShiftCoverCardPresent = true;
                    break;
                }
            }
            if (isShiftCoverCardPresent)
                SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
            else
                SimpleUtils.fail("Failed to find the card that is new and contain: " + requestUserName + ", "
                        + respondUserName + ", " + expectedMessage + "! Actual card is: " + message.getText(), false);
        } else {
            SimpleUtils.fail("Shift Swap Activity failed to Load", true);
        }
        return shiftCoverCard;
    }

	@Override
    public void verifyCancelledMessageOnTheBottomOfTheNotification() throws Exception{
		if (areListElementVisible(activityCards, 15)) {
			WebElement canceledMessage = activityCards.get(0).findElement(By.className("notification-approved"));
			if (canceledMessage != null && canceledMessage.getText().toLowerCase().contains("cancelled")) {
				SimpleUtils.pass("Canceled message load successfully!");
			} else {
				SimpleUtils.fail("Cancelled message failed to load!", false);
			}
		}
	}

    @Override
    public void approveOrRejectShiftCoverRequestOnActivity(String requestUserName, String respondUserName, String action, String location) throws Exception {
        WebElement shiftCoverCard = verifyNewShiftCoverCardShowsOnActivity(requestUserName, respondUserName, location);
        if (shiftCoverCard != null) {
            List<WebElement> actionButtons = shiftCoverCard.findElements(By.className("notification-buttons-button"));
            if (actionButtons != null && actionButtons.size() == 2) {
                for (WebElement button : actionButtons) {
                    if (action.equalsIgnoreCase(button.getText())) {
                        click(button);
                        break;
                    }
                }
                // Wait for the card to change the status message, such as approved or rejected
				waitForSeconds(10);
				clickTheElement(activityFilters.get(2));
				waitForSeconds(1);
				clickTheElement(activityFilters.get(2));
                if (areListElementVisible(activityCards, 15)) {
                    WebElement approveOrRejectMessage = activityCards.get(0).findElement(By.className("notification-approved"));
                    if (approveOrRejectMessage != null && approveOrRejectMessage.getText().toLowerCase().contains(action.toLowerCase())) {
                        SimpleUtils.pass(action + " the shift cover request for: " + requestUserName + " and " + respondUserName + " Successfully!");
                    } else {
                        SimpleUtils.fail(action + " message failed to load!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Action buttons: Approve and Reject failed to load!", false);
            }
        } else {
            SimpleUtils.fail("Failed to find a new Shift Cover activity!", false);
        }
    }

	@FindBy (className = "notification-buttons-button")
	private List<WebElement> allActivityButtons;

	@FindBy (css = ".notification-container.unread")
	private List<WebElement> unreadActivityCards;
	@Override
	public void approveOrRejectMultipleShiftOfferRequestOnActivity(String requestUserName, String action, int count) throws Exception {
    	if (areListElementVisible(unreadActivityCards, 5)
				&& areListElementVisible(unreadActivityCards, 5)
				&& unreadActivityCards.size() >= count
				&& allActivityButtons.size() >=count*2) {
			int i = 0;

    		for (int j=0; j< activityCards.size(); j++) {
    			while (i< count) {
					String activityMessage = activityCards.get(j).findElement(By.className("notification-content-message")).getText();
    				if (activityCards.get(j).getAttribute("class").contains("unread") && activityMessage.contains(requestUserName)) {
    					i++;
						List<WebElement> actionButtons = activityCards.get(j).findElements(By.className("notification-buttons-button"));
						if (actionButtons != null && actionButtons.size() == 2) {
							for (WebElement button : actionButtons) {
								if (action.equalsIgnoreCase(button.getText())) {
									scrollToElement(button);
									clickTheElement(button);
									break;
								}
							}
							// Wait for the card to change the status message, such as approved or rejected
							waitForSeconds(3);
							scrollToElement(activityCards.get(j));
							WebElement approveOrRejectMessage = activityCards.get(j).findElement(By.className("notification-approved"));
							if (approveOrRejectMessage != null && approveOrRejectMessage.getText().toLowerCase().contains(action.toLowerCase())) {
								SimpleUtils.pass(action + " the shift offer request for: " + requestUserName +  " Successfully!");
							} else {
								SimpleUtils.fail(action + " message failed to load!", false);
							}
						}else {
							SimpleUtils.report("Action buttons: Approve and Reject failed to load!");
						}
					} else {
    					break;
					}
				}
			}
		}
	}



	@FindBy (css = "[ng-repeat=\"a in notification.details.data.timeoff.accrued.accrued\"]")
	private List<WebElement> balanceHrsInActivity;
	@Override
	public HashMap<String, String> getBalanceHrsFromActivity() throws Exception {
		HashMap<String, String> balanceHrs = new HashMap<>();
		if (areListElementVisible(balanceHrsInActivity, 10)
				&& balanceHrsInActivity.size()>0) {
			for (int i = 0; i < balanceHrsInActivity.size(); i++) {
				String hrs = balanceHrsInActivity.get(i).getText().split(":")[1].replace("Hrs", "").trim();
				String timeOffType = balanceHrsInActivity.get(i).getText().split(":")[0];
				balanceHrs.put(timeOffType, hrs);
			}
		}
		return balanceHrs;
	}



	@FindBy (css = "div[ng-if=\"canShowDetails()\"]")
	private List<WebElement> detailLinksInActivities;
	@Override
	public void clickDetailLinksInActivitiesByIndex(int index) throws Exception {
		HashMap<String, String> balanceHrs = new HashMap<>();
		if (areListElementVisible(detailLinksInActivities, 5)
				&& detailLinksInActivities.size()>index) {
			clickTheElement(detailLinksInActivities.get(index));
			SimpleUtils.pass("Click the detail link successfully! ");
		} else
			SimpleUtils.fail("The detail links fail to load in activities! ", false);
	}


	@Override
	public void verifyNewClaimOpenShiftCardShowsOnActivity(String requestUserName, String workRole, String shiftDateAndTime, String location) throws Exception {
		String expectedMessage = requestUserName + " claimed the "+workRole+ " open shift on "+shiftDateAndTime+ " @"+location+".";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().equalsIgnoreCase(expectedMessage)) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + message.getText(), false);
			}
		}else {
			SimpleUtils.fail("Shift Swap Activity failed to Load!", false);
		}
	}


	@Override
	public void verifyNewShiftSwapCardWithTwoLocationsShowsOnActivity(String requestUserName, String respondUserName, String actionLabel,
													  boolean isNewLabelShows, String location1, String location2) throws Exception {
		String newStatus = "New";
		String expectedMessage = actionLabel + " to swap shifts";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			if (isNewLabelShows) {
				WebElement newLabel = activityCards.get(0).findElement(By.className("notification-new-label"));
				if (newLabel != null && newLabel.getText().equalsIgnoreCase(newStatus)) {
					SimpleUtils.pass("Verified 'New' label shows correctly");
				}else {
					SimpleUtils.fail("Failed to find a new business profile update activity!", false);
				}
			}
			WebElement message = activityCards.get(0).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().contains(requestUserName) && message.getText().contains(respondUserName)
					&& message.getText().toLowerCase().contains(expectedMessage)
					&& isElementLoaded(activityCards.get(0).findElement(By.cssSelector(".notification-content .location")))
					&& activityCards.get(0).findElements(By.cssSelector(".notification-content .location")).get(0).getText().toLowerCase().contains("@"+location1.toLowerCase())
					&& activityCards.get(0).findElements(By.cssSelector(".notification-content .location")).get(1).getText().toLowerCase().contains("@"+location2.toLowerCase())) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + message.getText(), false);
			}
		}else {
			SimpleUtils.fail("Shift Swap Activity failed to Load!", false);
		}
	}

	@Override
	public void verifyNewClaimOpenShiftGroupCardShowsOnActivity(int requestNum, String requestUserName, String workRole, String shiftDateAndTime, String location) throws Exception {
		String expectedMessage = requestUserName + " claimed the "+workRole+ " open shift group on "+shiftDateAndTime+ " @"+location+".";
		waitForSeconds(5);
		if (areListElementVisible(activityCards, 15)) {
			WebElement message = activityCards.get(requestNum).findElement(By.className("notification-content-message"));
			if (message != null && message.getText().equalsIgnoreCase(expectedMessage)) {
				SimpleUtils.pass("Find Card: " + message.getText() + " Successfully!");
			}else {
				SimpleUtils.fail("Failed to find the card that is new and contain: " + expectedMessage + "! Actual card is: " + message.getText(), false);
			}
		}else {
			SimpleUtils.fail("Shift Swap Activity failed to Load!", false);
		}
	}

	@Override
	public void approveOrRejectOpenShiftGroupRequestOnActivity(int requestNum, String action) throws Exception {
		WebElement openShiftGroupCard = activityCards.get(requestNum);
		if (openShiftGroupCard != null) {
			List<WebElement> actionButtons = openShiftGroupCard.findElements(By.className("notification-buttons-button"));
			if (actionButtons != null && actionButtons.size() == 2) {
				for (WebElement button : actionButtons) {
					if (action.equalsIgnoreCase(button.getText())) {
						click(button);
						break;
					}
				}
				// Wait for the card to change the status message, such as approved or rejected
				waitForSeconds(5);
				clickTheElement(activityFilters.get(1));
				waitForSeconds(1);
				clickTheElement(activityFilters.get(1));
				if (areListElementVisible(activityCards, 15)) {
					WebElement approveOrRejectMessage = activityCards.get(0).findElement(By.cssSelector(".notification-approved"));
					if (approveOrRejectMessage != null && approveOrRejectMessage.getText().toLowerCase().contains(action.toLowerCase())) {
						SimpleUtils.pass(action + " the open shift group request is " + action + "successfully!");
					} else {
						SimpleUtils.fail(action + " message failed to load!", false);
					}
				}
			}else {
				SimpleUtils.fail("Action buttons: Approve and Reject failed to load!", false);
			}
		}else {
			SimpleUtils.fail("Failed to find a new Shift Swap activity!", false);
		}
	}

	@FindBy (css = "div[class*=\"notification-details__shift-group-title\"]")
	private WebElement shiftTitle;
	@FindBy (css = "div[class*=\"notification-details__shift-group-expiry\"]")
	private WebElement shiftExpiryDate;
	@FindBy (css = "div[class=\"notification-details__shift-group-date\"]")
	private List<WebElement> dates;
	@FindBy (css = "div[class=\"notification-details__shift-group-time\"]")
	private List<WebElement> shiftTimes;
	@FindBy (css = "div[class=\"notification-details__shift-group-wrapper ng-scope\"]")
	private List<WebElement> shiftSections;
	@Override
	public void verifyContentOfOpenShiftGroupRequestOnActivity(String shiftGroupTitle, String expiryDate, ArrayList<String> specificDate, String shiftStartNEndTime, String workRole, String location) throws Exception {
		WebElement openShiftGroupCard = activityCards.get(0);
		if (openShiftGroupCard != null) {
			if (areListElementVisible(detailLinksInActivities, 5)) {
				if (shiftTitle.getText().equalsIgnoreCase(shiftGroupTitle) & shiftExpiryDate.getText().replaceAll("\\n", "").trim().contains(expiryDate)) {
					if (detailLinksInActivities.get(0).getText().trim().equalsIgnoreCase("Details"))
						clickTheElement(detailLinksInActivities.get(0));
					if (!areListElementVisible(shiftSections, 10))
						SimpleUtils.fail("Shift details in activity are not loaded correctly!", false);
					String shiftDate = null;
					String shiftTime = null;
					String shiftWorkRole = null;
					String shiftLocation = null;
					List<WebElement> actionButtons = openShiftGroupCard.findElements(By.className("notification-buttons-button"));
					for (int i = 0; i < shiftSections.size(); i++) {
						shiftDate = shiftSections.get(i).findElement(By.cssSelector(".notification-details__shift-group-date"))
								.getText().replaceAll("\\n", "");

						shiftTime = shiftSections.get(i).findElement(By.cssSelector(".notification-details__shift-group-time span:nth-child(1)"))
								.getText().replaceAll(" ", "") + "-" + shiftSections.get(i).findElement(By.cssSelector(".notification-details__shift-group-time span:nth-child(2)"))
								.getText().replaceAll(" ", "");
						shiftWorkRole = shiftSections.get(i).findElement(By.cssSelector(".notification-details__shift-group-role span:nth-child(1)")).getText();
						shiftLocation = shiftSections.get(i).findElement(By.cssSelector(".notification-details__shift-group-role span:nth-child(2)")).getText();
						if (shiftDate.equalsIgnoreCase(specificDate.get(i)) & shiftTime.equalsIgnoreCase(shiftStartNEndTime) &
								shiftWorkRole.equalsIgnoreCase(workRole) & shiftLocation.equalsIgnoreCase(location) & areListElementVisible(actionButtons,5))
							continue;
						else
							SimpleUtils.fail("Shift information in activity is not correct! The actual result is: " +
									"" + shiftDate + " " + shiftTime + " " + shiftWorkRole + " " + shiftLocation
									+ "And expected result is: " + specificDate.get(i) + " " + shiftStartNEndTime + " "
									+ workRole + " " + location, false);
						}
				}else
					SimpleUtils.fail("Shift title or expiry date in activity are not correct! The actual group title is: " +
							"" + shiftTitle.getText().replaceAll("\\n", "").trim() + " Expiry date is: "
							+ shiftExpiryDate.getText().replaceAll("\\n", "").trim() +
							"And expected group title is: " + shiftGroupTitle + " " + " Expiry date is: " + expiryDate, false);
			} else
				SimpleUtils.fail("Request detail link is not loaded!", false);
		}else
			SimpleUtils.fail("Failed to find open shift group request in activity!", false);
	}

	@Override
	public boolean isOpenShiftGroupRequestFolded() throws Exception {
		boolean isRequestFolded = true;
		WebElement openShiftGroupCard = activityCards.get(0);
		if (openShiftGroupCard != null) {
			WebElement detailLink = openShiftGroupCard.findElement(By.cssSelector("div[ng-if=\"canShowDetails()\"]"));
			if (isElementLoaded(detailLink,5)) {
				if (detailLink.getText().trim().equalsIgnoreCase("Details") & !isElementLoaded(shiftTitle)
						& !isElementLoaded(shiftExpiryDate) & !areListElementVisible(dates) & !areListElementVisible(shiftTimes)){
					SimpleUtils.report("Open Shift Group Request is folded!");
				}else if(detailLink.getText().trim().equalsIgnoreCase("Less") & isElementLoaded(shiftTitle)
						& isElementLoaded(shiftExpiryDate) & areListElementVisible(dates) & areListElementVisible(shiftTimes)) {
					isRequestFolded = false;
					SimpleUtils.report("Open Shift Group Request is unfolded!");
				}else
					SimpleUtils.fail("Open Shift Group Request is not displayed correctly!",false);
			} else
				SimpleUtils.fail("Request detail link is not loaded!", false);
		}else
			SimpleUtils.fail("Failed to find open shift group request in activity!", false);
		return isRequestFolded;
	}
}
