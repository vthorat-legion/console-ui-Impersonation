package com.legion.tests.bdd.steps;

import com.google.inject.Inject;
import com.legion.pages.core.ConsoleLoginPage;
import com.legion.tests.testframework.bdd.StepsBase;
import cucumber.api.java.en.Given;

public class LoginSteps extends StepsBase {


    @Inject
    ConsoleLoginPage loginPage;

    @Given("I login as '(.*)' password '(.*)' in enterprise '(.*)' in TA")
    public void givenIloginAsManager(String username, String password, String enterprise)throws Exception{
        goEnterprisePage(enterprise);
        loginPage.loginToLegionWithCredential(username, password);
    }

}
