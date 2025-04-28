package com.legion.pages.opConfiguration;

import java.util.List;

public interface MealAndRestPage {
    public void verifyTheContentOnMealBreaksSection() throws Exception;
    public void selectYesOrNoOnMealOrRest(String mealOrRest, String yesOrNo) throws Exception;
    public void clickOnAddButtonOnMealOrRestSection(String mealOrRest) throws Exception;
    public void verifyTheFunctionalityOfInputsInMealOrRest(String mealOrRest) throws Exception;
    public void verifyXbuttonOnMealOrRest(String mealOrRest) throws Exception;
    public void verifyTheContentOnRestBreaksSection() throws Exception;
    public void verifyCanSetTheValueForInputs(String mealOrRest, List<String> settings) throws Exception;
    public Boolean verifyMealAndRestValueAreSaved(String mealOrRest, List<String> settings) throws Exception;
    public void setRestDuration(String restDuration) throws Exception;
}
