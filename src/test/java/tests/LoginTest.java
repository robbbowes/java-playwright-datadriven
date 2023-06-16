package tests;

import core.GlobalTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends GlobalTestBase {

    @Test
    public void managerLoginTest() {
        getBrowser("chrome");
        navigate("https://www.way2automation.com/angularjs-protractor/banking/#/login");
        click("bmlBtn_CSS");
        click("addCustomer_CSS");
        type("customerFirstName_CSS", "Tom");
        type("customerLastName_CSS", "Jones");
        type("customerPostCode_CSS", "123456");
        click("createCustomerButton_CSS");
    }

    @Test
    public void failingTest() {
        getBrowser("chrome");
        navigate("https://www.way2automation.com/angularjs-protractor/banking/#/login");
        Assert.assertTrue(isElementPresent("bmlBtn_CSS"));
        Assert.assertTrue(isElementPresent("bmlBtn2_CSS"));
    }
}
