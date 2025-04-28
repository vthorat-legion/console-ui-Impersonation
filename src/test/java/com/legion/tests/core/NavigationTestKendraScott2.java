package com.legion.tests.core;

import java.util.Map;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.legion.tests.TestBase;
import com.legion.utils.SimpleUtils;

import java.lang.reflect.Method;

import com.legion.tests.annotations.Automated;
import com.legion.tests.annotations.Enterprise;
import com.legion.tests.annotations.Owner;
import com.legion.tests.annotations.TestName;
import com.legion.tests.data.CredentialDataProviderSource;


/**
 * Manideep
 */

public class NavigationTestKendraScott2 extends TestBase {
    private static Map<String, String> propertyMap = SimpleUtils.getParameterMap();

    @Override
	@BeforeMethod()
    public void firstTest(Method testMethod, Object[] params) throws Exception{
	  this.createDriver((String)params[0],"69","Window");
      visitPage(testMethod);
      loginToLegionAndVerifyIsLoginDone((String)params[1], (String)params[2],(String)params[3]);
    }
    
    @Automated(automated = "Manual")
    @Owner(owner = "Gunjan")
    @Enterprise(name = "Kendrascott2_Enterprise")
    @TestName(description = "LEG-5112:LocationGroup forecast, guidance and dashboard not loading on 10.09 master build for Carmel Club on LegionCoffee2")
    @Test(dataProvider = "legionTeamCredentialsByEnterprise", dataProviderClass=CredentialDataProviderSource.class)
    public void DataNotLoadingForCarmelClubLocation(String username, String password, String browser, String location)
           throws Exception
    {
        SimpleUtils.pass("Login to LegionCoffee2 Successfully");
        SimpleUtils.pass("Navigate to Carmel Club location");
        SimpleUtils.pass("assert navigation for carmel club location should load successfully ");

    }
	
    
}