package tests;

import com.microsoft.playwright.Dialog;
import core.GlobalTestBase;
import org.testng.annotations.Test;
import utils.DataProviders;

import java.util.Map;

public class AddCustomerTest extends GlobalTestBase {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "HashTableDataProvider")
    public void addCustomer(Map<String, String> map) {

        getBrowser(map.get("browser"));
        navigate("https://www.way2automation.com/angularjs-protractor/banking/#/login");

        getPage().onDialog(Dialog::accept);

        click("bmlBtn_CSS");
        click("addCustomer_CSS");
        type("customerFirstName_CSS", map.get("firstname"));
        type("customerLastName_CSS", map.get("lastname"));
        type("customerPostCode_CSS", map.get("postcode"));
        click("createCustomerButton_CSS");


        System.out.println(map.get("runmode"));
        System.out.println(map.get("firstname"));
        System.out.println(map.get("lastname"));
        System.out.println(map.get("postcode"));
        System.out.println(map.get("browser"));
    }

}
