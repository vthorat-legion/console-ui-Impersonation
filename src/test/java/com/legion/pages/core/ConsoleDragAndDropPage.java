package com.legion.pages.core;

import com.legion.pages.BasePage;
import com.legion.pages.DragAndDropPage;
import org.openqa.selenium.support.PageFactory;

import static com.legion.utils.MyThreadLocal.getDriver;

public class ConsoleDragAndDropPage extends BasePage implements DragAndDropPage {
    public ConsoleDragAndDropPage() {
        PageFactory.initElements(getDriver(), this);
    }
}
