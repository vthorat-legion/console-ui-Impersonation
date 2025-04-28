package com.legion.tests.bdd;

import com.google.inject.Inject;
import com.legion.tests.testframework.bdd.DriverManager;
import com.legion.tests.testframework.bdd.StepsBase;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hook extends StepsBase {

    @Inject
    DriverManager manager;

    @Before
    public void before(){
      manager.initDriver("Chrome","60.02","Linux");
    }

    @After
    public void after(){
        manager.finishDriver();
    }


}
