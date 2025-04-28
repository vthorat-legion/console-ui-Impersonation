package com.legion.tests.testframework;

import com.google.inject.AbstractModule;
import com.legion.pages.core.*;
import com.legion.tests.testframework.bdd.DriverManager;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DriverManager.class);

        bind(ConsoleAnalyticsPage.class);
        bind(ConsoleControlsNewUIPage.class);
        bind(ConsoleControlsPage.class);
        bind(ConsoleDashboardPage.class);
        bind(ConsoleForecastPage.class);
        bind(ConsoleLoginPage.class);
        bind(ConsoleTimeSheetPage.class);


    }


}
