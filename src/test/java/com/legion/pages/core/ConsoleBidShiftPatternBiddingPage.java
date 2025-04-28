package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.BidShiftPatternBiddingPage;
import com.legion.utils.SimpleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleBidShiftPatternBiddingPage extends BasePage implements BidShiftPatternBiddingPage {
    public ConsoleBidShiftPatternBiddingPage() {
        PageFactory.initElements(getDriver(), this);
    }

    @FindBy(xpath = "//div[contains(@class,'MuiBox-root css-0')][2]")
    private WebElement shiftBiddingWidgetOnDashboard;
    @FindBy(xpath = "//div[contains(@class,'MuiBox-root css-0')][2]/div[3]/button")
    private WebElement submitBidsButton;

    @Override
    public boolean checkIfTheShiftBiddingWidgetLoaded() throws Exception {
        boolean isLoaded= false;
        if (isElementLoaded(shiftBiddingWidgetOnDashboard, 5)){
            isLoaded = true;
            SimpleUtils.pass("The shift bidding widget is loaded! ");
        } else
            SimpleUtils.report("The shift bidding widget is not loaded! ");
        return isLoaded;
    }

    @Override
    public void clickSubmitBidButton() throws Exception {
        if (isElementLoaded(submitBidsButton, 5)){
            SimpleUtils.pass("The submit bids button is loaded! ");
            clickTheElement(submitBidsButton);
            SimpleUtils.pass("Click submit bid button successfully! ");
        } else
            SimpleUtils.fail("The submit bids button is not loaded! ", false);
    }

    @FindBy(xpath = "//button[contains(text(),'Next')]")
    private WebElement nextButton;

    @Override
    public void clickNextButton() throws Exception {
        if (isElementLoaded(nextButton, 5)){
            SimpleUtils.pass("The next button is loaded! ");
            clickTheElement(nextButton);
            SimpleUtils.pass("Click next button successfully! ");
        } else
            SimpleUtils.fail("The next button is not loaded! ", false);
    }


    @FindBy(xpath = "//span[contains(text(),'Add')]")
    private List<WebElement> addButtons;
    @Override
    public void addAllShiftPatterns() throws Exception {
        if (areListElementVisible(addButtons, 5)){
            while (addButtons.size()>0){
                clickTheElement(addButtons.get(0));
                SimpleUtils.pass("Add one shift pattern successfully! ");
            }
        } else
            SimpleUtils.fail("There is no shift patterns loaded! ", false);
    }

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    private WebElement submitButton;

    @Override
    public void clickSubmitButton() throws Exception {
        if (isElementLoaded(submitButton, 5)){
            SimpleUtils.pass("The submit button is loaded! ");
            clickTheElement(submitButton);
            SimpleUtils.pass("Click submit button successfully! ");
        } else
            SimpleUtils.fail("The submit button is not loaded! ", false);
    }

    @FindBy(xpath = "//div[contains(@id,'react-shift-bidding-patterns')]/div/div/div/div[2]/div[2]/div/div")
    private List<WebElement> shiftBiddingPatternsOnAddPatternPage;
    @Override
    public void addSpecificShiftPattern(String workRole, String shiftPatternName) throws Exception {
        if (areListElementVisible(shiftBiddingPatternsOnAddPatternPage, 5)
                && shiftBiddingPatternsOnAddPatternPage.size()>0){
            boolean addShiftPatternSuccess = false;
            for (WebElement shiftPattern: shiftBiddingPatternsOnAddPatternPage){
                List<WebElement> workRoleAndNameOfShiftPattern = shiftPattern.findElements(By.xpath(".//span[contains(@class,'MuiTypography-root MuiTypography-caption')]"));
                String shiftPatternNameOnAddPatternPage =
                        shiftPattern.findElement(By.xpath(".//div/div[2]/div")).getText().split("/")[0].trim();
                if (workRoleAndNameOfShiftPattern.size() == 2){
                    String workRoleOfShiftPattern = workRoleAndNameOfShiftPattern.get(0).getText();
//                    String shiftPatternNameOnAddPatternPage = workRoleAndNameOfShiftPattern.get(1).getText();
                    if(workRoleOfShiftPattern.equalsIgnoreCase(workRole)
                            && shiftPatternNameOnAddPatternPage.equalsIgnoreCase(shiftPatternName)){
                        int shiftPatternCountBeforeAdd = shiftBiddingPatternsOnAddPatternPage.size();
                        WebElement addButton = shiftPattern.findElement(By.xpath(".//span[contains(@class,'MuiTypography-button')]"));
                        clickTheElement(addButton);
                        waitForSeconds(2);
                        int shiftPatternCountAfterAdd = shiftBiddingPatternsOnAddPatternPage.size();
                        if(shiftPatternCountBeforeAdd==shiftPatternCountAfterAdd+1){
                            SimpleUtils.pass("Add one shift pattern successfully! ");
                        }else
                            SimpleUtils.fail("Fail to add shift pattern! The shift count before add is: " +shiftPatternCountBeforeAdd+
                                    "the shift count after add is: "+shiftPatternCountAfterAdd, false);
                        addShiftPatternSuccess = true;
                        break;
                    }
                } else
                    SimpleUtils.fail("The work role and name of shift pattern fail to display! ", false);
            }
            SimpleUtils.assertOnFail("The shift pattern not been added! ",
                    addShiftPatternSuccess, false);
        } else
            SimpleUtils.fail("There is no shift patterns loaded! ", false);
    }


    @FindBy(xpath = "//div[@data-rbd-droppable-id='list']/div/div/div/div")
    private List<WebElement> selectedShiftPatterns;
    public void rankSelectedShiftPattern(String workRole, String shiftPatternName, int rank){
        if (areListElementVisible(selectedShiftPatterns, 5) && selectedShiftPatterns.size()>0){
            if(rank<=selectedShiftPatterns.size()){
                for (WebElement selectedShiftPattern:selectedShiftPatterns){
                    String workRoleOfShiftPattern = selectedShiftPattern
                            .findElements(By.xpath(".//span[contains(@class,'MuiTypography-root MuiTypography-caption')]")).get(0).getText();
                    String shiftPatternNameOnAddPatternPage =
                            selectedShiftPattern.findElement(By.xpath(".//div[2]/div[1]/div/div[2]")).getText().split("/")[0].trim();
                    if(workRoleOfShiftPattern.equalsIgnoreCase(workRole)
                            && shiftPatternNameOnAddPatternPage.equalsIgnoreCase(shiftPatternName)){
                        //click Rank dropdown
                        click(selectedShiftPattern.findElement(By.xpath(".//div[contains(@class,'react-select__indicators')]/div")));
                        waitForSeconds(2);
                        click(getDriver(). findElement(By.xpath("//*[contains(@id,'option-"+(rank-1)+"')]")));
                        break;
                    }
                }
            } else
                SimpleUtils.fail("The rank more than selected shift patterns, the selected shift pattern size is: "+selectedShiftPatterns.size()
                        +", the rank is:"+rank, false);

        } else
            SimpleUtils.fail("There is no shift pattern been selected! ", false);
    }
}
