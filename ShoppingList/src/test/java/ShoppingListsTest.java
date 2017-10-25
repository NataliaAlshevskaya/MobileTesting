import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import org.testng.Assert;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class ShoppingListsTest {

    AndroidDriver driver;
    BuyListsPage buyListPage;
    ProductListPage productListPage;
    SettingsPage settingPage;
    OptionsSpinnerPage optionPage;


    private void callShellCommand(String command){
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void suiteSetUp()throws Exception {
        /* Here we specify the capabilities required by the Appium server.
        * We have already specified most of these manually through the
        * Appium server interface (apk path and etc.)
        */
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("app", "/Users/grid/MobileTesting/Shopping_list_1.6.apk");

        /* We initialize Appium driver that will connect us to the Android device with
        * the capabilities we have just set. The URL we are providing is telling Appium we
        * are going to run the test locally.
        */

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        buyListPage = new BuyListsPage(driver);
        productListPage = new ProductListPage(driver);
        optionPage = new OptionsSpinnerPage(driver);
        settingPage = new SettingsPage(driver);
        buyListPage.setListName("firstList");
        buyListPage.addList();
    }

    /* We disable the driver after  the test has been executed. */
    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test (description = "Create a product and check appeared detailed fields.")
    public void checkAppearedProductDetailFieldsTest(){
        productListPage.setProductName("aa");
        Assert.assertTrue(!productListPage.doesItemPriceExist());

        productListPage.setProductName("aaa");

        Assert.assertTrue(productListPage.doesItemPriceExist());
        Assert.assertTrue(productListPage.getItemPrice().isDisplayed());
        Assert.assertTrue(productListPage.getPriceCurrency().isDisplayed());
        Assert.assertEquals(productListPage.getPriceCurrency().getText(),"£  ");
        Assert.assertTrue(productListPage.getAmount().isDisplayed());
        Assert.assertTrue(productListPage.getAmountDimention().isDisplayed());
        Assert.assertTrue(productListPage.getCategory().isDisplayed());

    }

    @Test (dependsOnMethods = { "checkAppearedProductDetailFieldsTest" },
            description = "Create 3 products in one list and inspect list page.")
    public void checkListOfProductsTest(){
        productListPage.setItemPrice("5");
        productListPage.setAmount("3");

        productListPage.openDimentionPage();
        optionPage.setOption("pack");
        productListPage.setComment("Some comment");
        productListPage.openCategoryPage();
        optionPage.setOption("Medications");
        productListPage.addProduct();

        // Product with filled only name and price.
        productListPage.setProductName("bbb");
        productListPage.setItemPrice("50");
        productListPage.addProduct();

        // Product with filled only name.
        productListPage.setProductName("ccc");
        productListPage.addProduct();


        // Check final list page.
        productListPage.removeKeyboard();

        Assert.assertEquals(productListPage.getTotalText().getText(), "Total: 65 £");

        Assert.assertTrue(productListPage.getProductNameInList("aaa").isDisplayed());
        Assert.assertTrue(productListPage.getCommentInList("Some comment").isDisplayed());
        Assert.assertTrue(productListPage.getAmountInList("3 pack").isDisplayed());
        Assert.assertTrue(productListPage.getPriceInList("5 £").isDisplayed());

        Assert.assertTrue(productListPage.getProductNameInList("bbb").isDisplayed());
        Assert.assertTrue(productListPage.getPriceInList("50 £").isDisplayed());

        Assert.assertTrue(productListPage.getProductNameInList("ccc").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkListOfProductsTest" },
            description = "Settings -> change currency option.")
    public void checkChangeCurrencyFunctionTest(){

        productListPage.clickThreePointButton();
        Assert.assertTrue(settingPage.isInitialized());
        settingPage.setSetting("Settings");
        settingPage.setSetting("Currency");
        optionPage.setOption("$");

        settingPage.returnBackFromSettings();
        Assert.assertTrue(productListPage.getPriceInList("5 $").isDisplayed());

        productListPage.setProductName("try");
        Assert.assertEquals(productListPage.getPriceCurrency().getText(),"$  ");
        productListPage.setProductName("");
    }

    @Test (dependsOnMethods = { "checkChangeCurrencyFunctionTest" },
            description = "Check change order settings button.")
    public void checkChangeOrderSettingsTest(){
        productListPage.clickThreePointButton();
        Assert.assertTrue(settingPage.isInitialized());
        settingPage = new SettingsPage(driver);
        settingPage.setSetting("Settings");
        settingPage.setSetting("Sort list");
        optionPage.setOption("In a pre-order");

        settingPage.returnBackFromSettings();

        Assert.assertEquals(productListPage.getProductNames().get(0).getText(),"ccc");
        Assert.assertEquals(productListPage.getProductNames().get(1).getText(),"bbb");
        Assert.assertEquals(productListPage.getProductNames().get(2).getText(),"aaa");
    }


    @Test (dependsOnMethods = { "checkChangeOrderSettingsTest" },
            description = "Create two new lists and inspect main page list.")
    public void checkListOfBuyListsTest(){
        productListPage.returnToBuyListsPage();

        buyListPage.setListName("secondList");
        buyListPage.addList();

        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        Assert.assertTrue(buyListPage.getListTitle("firstList").isDisplayed());
        Assert.assertTrue(buyListPage.getListInfoText("All items: 3 Sum: 65 $ Date:").isDisplayed());
        Assert.assertTrue(buyListPage.getListTitle("secondList").isDisplayed());
        Assert.assertTrue(buyListPage.getListInfoText("All items: 0 Sum: 0 $ Date:").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkListOfBuyListsTest" },
            description = "Check edit list name button on main page.")
    public void checkEditListButtonTest(){
        buyListPage.setNewListName("SecondNameForFirstList");

        Assert.assertTrue(buyListPage.getListTitle("firstListSecondNameForFirstList").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkEditListButtonTest" },
            description = "Check delete list button on main page.")
    public void checkDeleteListButtonTest(){
        buyListPage.deleteList();
         Assert.assertNull(buyListPage.getListTitle("firstListSecondNameForFirstList"));
    }
}
