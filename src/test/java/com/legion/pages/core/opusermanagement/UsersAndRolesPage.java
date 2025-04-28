package com.legion.pages.core.opusermanagement;

import com.legion.pages.BasePage;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static com.legion.utils.MyThreadLocal.getDriver;

public class UsersAndRolesPage extends BasePage {
    public UsersAndRolesPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // Added by Sophia
    @FindBy(css = "div.lg-page-heading__breadcrumbs>a")
    private WebElement backButton;
    @FindBy(css = "div.lg-tabs>nav>div:nth-child(5)")
    private WebElement badgesTab;
    @FindBy(css = "lg-badge-management tr>td:nth-child(2)>lg-button")//need to get label
    private List<WebElement> badgesList;
    @FindBy(css = "lg-badge-management lg-tab-toolbar div.lg-pagination__pages")
    private WebElement paging;
    @FindBy(css = "lg-badge-management lg-tab-toolbar div.lg-pagination>div.lg-pagination__arrow--left")
    private WebElement previousPageArrow;
    @FindBy(css = "lg-badge-management lg-tab-toolbar div.lg-pagination>div.lg-pagination__arrow--right")
    private WebElement nextPageArrow;

    public void back() {
        backButton.click();
    }

    public void goToBadges() {
        badgesTab.click();
    }

    public List<String> getBadgeList() {
        ArrayList<String> badges = new ArrayList<String>();
        String str = paging.getText();
        String st = StringUtils.substringAfterLast(str, " ");
        int pageNum = Integer.parseInt(st);
        for (int i = 1; i <= pageNum; i++) {
            for (WebElement bad : badgesList) {
                badges.add(bad.getAttribute("label"));
            }
            if (i < pageNum) {
                nextPageArrow.click();
            }
        }
        return badges;
    }


}
