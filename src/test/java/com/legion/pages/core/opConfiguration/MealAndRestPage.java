package com.legion.pages.core.opConfiguration;

import com.legion.pages.BasePage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class MealAndRestPage extends BasePage implements com.legion.pages.opConfiguration.MealAndRestPage {

    public MealAndRestPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(css = "[form-title=\"Meal Breaks\"]")
    private WebElement mealBreakSection;

    @FindBy(css = "[form-title=\"Rest Breaks\"]")
    private WebElement restBreakSection;

    @FindBy(css = "[form-title=\"Meal Breaks\"] .lg-question-input__text")
    private List<WebElement> mealBreakTexts;

    @FindBy(css = "[form-title=\"Rest Breaks\"] .lg-question-input__text")
    private List<WebElement> restBreakTexts;

    @FindBy(css = "[form-title=\"Meal Breaks\"] .lg-button-group div")
    private List<WebElement> mealBreakPaidBtns;

    @FindBy(css = "[form-title=\"Rest Breaks\"] .lg-button-group div")
    private List<WebElement> restBreakPaidBtns;

    @FindBy(css = "[form-title=\"Meal Breaks\"] th.ng-binding")
    private List<WebElement> mealBreakTableTitles;

    @FindBy(css = "[form-title=\"Rest Breaks\"] th.ng-binding")
    private List<WebElement> restBreakTableTitles;

    @FindBy(css = "[form-title=\"Meal Breaks\"] [label*=\"+ Add\"]")
    private WebElement mealAddBtn;

    @FindBy(css = "[form-title=\"Rest Breaks\"] [label=\"+ Add\"]")
    private WebElement restAddBtn;

    @FindBy(css = "[modal-title=\"New Meal Break Rule\"] input[type=\"number\"]")
    private List<WebElement> mealInputs;

    @FindBy(css = "[form-title=\"Rest Breaks\"] table input[type=\"number\"]")
    private List<WebElement> restInputs;

    @FindBy(css = "[form-title=\"Meal Breaks\"] .delete-action")
    private List<WebElement> mealDeleteBtns;

    @FindBy(css = "[form-title=\"Rest Breaks\"] .delete-action")
    private List<WebElement> restDeleteBtns;

    @FindBy(css = "[question-title=\"Rest break duration\"] input")
    private WebElement restDurationInput;

    @Override
    public void verifyTheContentOnMealBreaksSection() throws Exception {
        if (isElementLoaded(mealBreakSection, 10)) {
            if (isElementLoaded(mealBreakSection.findElement(By.className("info"))) &&
                    mealBreakSection.findElement(By.className("info")).getText().equalsIgnoreCase("Meal Breaks")
                    && areListElementVisible(mealBreakTexts, 5) && mealBreakTexts.size() == 2 &&
                    mealBreakTexts.get(0).getText().equalsIgnoreCase("Are meal breaks paid?") && mealBreakTexts.get(1).getText().equalsIgnoreCase("Meal Breaks")
                    && areListElementVisible(mealBreakPaidBtns, 5) && mealBreakPaidBtns.size() == 2) {
                SimpleUtils.pass("Meal breaks content shows correctly");
                if (areListElementVisible(mealBreakTableTitles, 5) && mealBreakTableTitles.size() == 7 &&
                        mealBreakTableTitles.get(0).getText().equalsIgnoreCase("Meal Break #") &&
                        mealBreakTableTitles.get(1).getText().equalsIgnoreCase("Min Shift Length (inclusive)") &&
                        mealBreakTableTitles.get(2).getText().equalsIgnoreCase("Max Shift Length (exclusive)") &&
                        mealBreakTableTitles.get(3).getText().equalsIgnoreCase("Duration") &&
                        mealBreakTableTitles.get(4).getText().equalsIgnoreCase("Left Offset") &&
                        mealBreakTableTitles.get(5).getText().equalsIgnoreCase("Right Offset") &&
                        mealBreakTableTitles.get(6).getText().equalsIgnoreCase("Latest Start") && isElementLoaded(mealAddBtn, 5)) {
                    SimpleUtils.pass("Meal Break setting table and + Add buttons loaded successfully!");
                } else {
                    SimpleUtils.fail("Meal Break setting table and + Add buttons failed to load!", false);
                }
            } else {
                SimpleUtils.fail("Meal breaks content not loaded!", false);
            }
        } else {
            SimpleUtils.fail("Meal Breaks section not loaded on Meal And Rest template!", false);
        }
    }

    @Override
    public void verifyTheContentOnRestBreaksSection() throws Exception {
        if (isElementLoaded(restBreakSection, 5) && isElementLoaded(restBreakSection.findElement(By.className("info"))) &&
                restBreakSection.findElement(By.className("info")).getText().equalsIgnoreCase("Rest Breaks") &&
                areListElementVisible(restBreakTexts, 5) && restBreakTexts.size() == 4 && restBreakTexts.get(0).getText().equals("Are rest breaks paid?")
                && restBreakTexts.get(1).getText().equals("Maximum allowed minutes between 2 consecutive breaks") && restBreakTexts.get(2).getText().equals("Rest break duration")
                && restBreakTexts.get(3).getText().equals("Rest Breaks") && areListElementVisible(restBreakPaidBtns, 5) && restBreakPaidBtns.size() == 2
                && areListElementVisible(restBreakTableTitles, 5) && restBreakTableTitles.size() == 3 && restBreakTableTitles.get(0).getText().equals(
                "Min Shift Length (inclusive)") && restBreakTableTitles.get(1).getText().equals("Max Shift Length (exclusive)") &&
                restBreakTableTitles.get(2).getText().equals("Number of Rest Breaks") && isElementLoaded(restAddBtn, 5)) {
            SimpleUtils.pass("The content on Rest Breaks section is correct!");
        } else {
            SimpleUtils.fail("Rest Breaks section not loaded on Meal And Rest template!", false);
        }
    }

    @Override
    public void selectYesOrNoOnMealOrRest(String mealOrRest, String yesOrNo) throws Exception {
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (areListElementVisible(mealBreakPaidBtns, 5) && mealBreakPaidBtns.size() == 2) {
                for (WebElement button : mealBreakPaidBtns) {
                    if (button.getText().equalsIgnoreCase(yesOrNo)) {
                        clickTheElement(button);
                        waitForSeconds(1);
                        if (button.getAttribute("class").contains("lg-button-group-selected")) {
                            SimpleUtils.report("Click the: " + yesOrNo + " button in Meal Breaks Section successfully");
                        } else {
                            SimpleUtils.fail("Failed to click the: " + yesOrNo + " button in Meal Breaks Section!", false);
                        }
                        break;
                    }
                }
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (areListElementVisible(restBreakPaidBtns, 5) && restBreakPaidBtns.size() == 2) {
                for (WebElement button : restBreakPaidBtns) {
                    if (button.getText().equalsIgnoreCase(yesOrNo)) {
                        clickTheElement(button);
                        waitForSeconds(1);
                        if (button.getAttribute("class").contains("lg-button-group-selected")) {
                            SimpleUtils.report("Click the: " + yesOrNo + " button in Rest Breaks Section successfully");
                        } else {
                            SimpleUtils.fail("Failed to click the: " + yesOrNo + " button in Rest Breaks Section!", false);
                        }
                        break;
                    }
                }
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
    }

    @Override
    public void clickOnAddButtonOnMealOrRestSection(String mealOrRest) throws Exception {
        String zero = "0";
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (isElementLoaded(mealAddBtn, 5)) {
                clickTheElement(mealAddBtn);
                if (areListElementVisible(mealInputs, 10) && mealInputs.size() == 13) {
                    for (int i = 0; i < mealInputs.size(); i++) {
                        if ((i == 1 || i == 2 || i == 7 || i == 11 || i == 12) && !mealInputs.get(i).getAttribute("value").equalsIgnoreCase(zero)) {
                            SimpleUtils.fail("The Default value is not zero, N/A or #!", false);
                            break;
                        }
                    }
                } else {
                    SimpleUtils.fail("Seven inputs failed to show when clicking + Add button!", false);
                }
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (isElementLoaded(restAddBtn, 5)) {
                clickTheElement(restAddBtn);
                if (areListElementVisible(restInputs, 10) && restInputs.size() == 3) {
                    for (WebElement input : restInputs) {
                        if (!input.getAttribute("value").equalsIgnoreCase(zero)) {
                            SimpleUtils.fail("The Default value is not zero!", false);
                            break;
                        }
                    }
                } else {
                    SimpleUtils.fail("Three inputs failed to show when clicking + Add button!", false);
                }
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
    }

    @Override
    public void verifyTheFunctionalityOfInputsInMealOrRest(String mealOrRest) throws Exception {
        int number = 10;
        String text = "a";
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (areListElementVisible(mealInputs, 5) && mealInputs.size() == 7) {
                clickTheElement(mealInputs.get(0));
                mealInputs.get(0).clear();
                mealInputs.get(0).sendKeys(String.valueOf(number));
                if (mealInputs.get(0).getAttribute("value").equalsIgnoreCase(String.valueOf(number))) {
                    SimpleUtils.pass("Meal inputs: number is supported!");
                } else {
                    SimpleUtils.fail("Number is not updated successfully!", false);
                }
                mealInputs.get(0).clear();
                mealInputs.get(0).sendKeys(text);
                if (mealInputs.get(0).getAttribute("value").equalsIgnoreCase(String.valueOf(number))) {
                    SimpleUtils.fail("Meal inputs: only number is supported!", false);
                } else if (mealInputs.get(0).getAttribute("value").equalsIgnoreCase("#")) {
                    SimpleUtils.pass("Meal inputs: only number is supported! If inputing other format, it should show #");
                }
            } else {
                SimpleUtils.fail("Seven inputs failed to show when clicking + Add button!", false);
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (areListElementVisible(restInputs, 5) && restInputs.size() == 3) {
                clickTheElement(restInputs.get(0));
                restInputs.get(0).clear();
                restInputs.get(0).sendKeys(String.valueOf(number));
                if (restInputs.get(0).getAttribute("value").equalsIgnoreCase(String.valueOf(number))) {
                    SimpleUtils.pass("Rest inputs: number is supported!");
                } else {
                    SimpleUtils.fail("Number is not updated successfully!", false);
                }
                restInputs.get(0).clear();
                restInputs.get(0).sendKeys(text);
                if (restInputs.get(0).getAttribute("value").equalsIgnoreCase(String.valueOf(number))) {
                    SimpleUtils.fail("Rest inputs: only number is supported!", false);
                } else if (restInputs.get(0).getAttribute("value").equalsIgnoreCase("#")) {
                    SimpleUtils.pass("Rest inputs: only number is supported! If inputing other format, it should show #");
                }
            } else {
                SimpleUtils.fail("Three inputs failed to show when clicking + Add button!", false);
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
    }

    @Override
    public void verifyXbuttonOnMealOrRest(String mealOrRest) throws Exception {
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (areListElementVisible(mealDeleteBtns, 5) && mealDeleteBtns.size() > 0) {
                clickTheElement(mealDeleteBtns.get(mealDeleteBtns.size() - 1));
            } else {
                SimpleUtils.fail("X buttons not loaded on Meal Breaks template!", false);
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (areListElementVisible(restDeleteBtns, 5) && restDeleteBtns.size() > 0) {
                clickTheElement(restDeleteBtns.get(restDeleteBtns.size() - 1));
            } else {
                SimpleUtils.fail("X buttons not loaded on Rest Breaks template!", false);
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
    }

    @Override
    public void verifyCanSetTheValueForInputs(String mealOrRest, List<String> settings) throws Exception {
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (areListElementVisible(mealInputs, 5) && mealInputs.size() == 13 && settings.size() == 13) {
                for (int i = 0; i < mealInputs.size(); i++) {
                    clickTheElement(mealInputs.get(i));
                    mealInputs.get(i).clear();
                    mealInputs.get(i).sendKeys(String.valueOf(settings.get(i)));
                    if (mealInputs.get(i).getAttribute("value").equalsIgnoreCase(String.valueOf(settings.get(i)))) {
                        SimpleUtils.pass("Meal inputs: number is updated successfully!");
                    } else {
                        SimpleUtils.fail("Number is not updated successfully!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Seven inputs failed to show when clicking + Add button!", false);
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (areListElementVisible(restInputs, 5) && restInputs.size() == 3 && settings.size() == 3) {
                for (int i = 0; i < restInputs.size(); i++) {
                    clickTheElement(restInputs.get(i));
                    restInputs.get(i).clear();
                    restInputs.get(i).sendKeys(String.valueOf(settings.get(i)));
                    if (restInputs.get(i).getAttribute("value").equalsIgnoreCase(String.valueOf(settings.get(i)))) {
                        SimpleUtils.pass("Rest inputs: number is updated successfully!");
                    } else {
                        SimpleUtils.fail("Number is not updated successfully!", false);
                    }
                }
            } else {
                SimpleUtils.fail("Three inputs failed to show when clicking + Add button!", false);
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
    }

    @Override
    public Boolean verifyMealAndRestValueAreSaved(String mealOrRest, List<String> settings) throws Exception {
        boolean isSaved = true;
        if (mealOrRest.equalsIgnoreCase("Meal")) {
            if (areListElementVisible(mealInputs, 5) && mealInputs.size() == 13 && settings.size() == 13) {
                for (int i = 0; i < mealInputs.size(); i++) {
                    if (!mealInputs.get(i).getAttribute("value").equals(String.valueOf(settings.get(i)))) {
                        isSaved = false;
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Seven inputs failed to show when clicking + Add button!", false);
            }
        } else if (mealOrRest.equalsIgnoreCase("Rest")) {
            if (areListElementVisible(restInputs, 5) && restInputs.size() == 3 && settings.size() == 3) {
                for (int i = 0; i < restInputs.size(); i++) {
                    if (!restInputs.get(i).getAttribute("value").equals(String.valueOf(settings.get(i)))) {
                        isSaved = false;
                        break;
                    }
                }
            } else {
                SimpleUtils.fail("Three inputs failed to show when clicking + Add button!", false);
            }
        } else {
            SimpleUtils.fail("Please send the correct Param: Meal Or Rest!", false);
        }
        return isSaved;
    }

    @Override
    public void setRestDuration(String restDuration) throws Exception {
        if (isElementLoaded(restDurationInput, 5)) {
            restDurationInput.clear();
            restDurationInput.sendKeys(restDuration);
            if (restDurationInput.getAttribute("value").equals(restDuration)) {
                SimpleUtils.pass("Rest Break Duration: " + restDuration + " is set successfully!");
            } else {
                SimpleUtils.fail("Expected Rest Break Duration: " + restDuration + ", but actual is: " +
                        restDurationInput.getAttribute("value"), false);
            }
        } else {
            SimpleUtils.fail("Rest Break Duration failed to load!", false);
        }
    }

}
