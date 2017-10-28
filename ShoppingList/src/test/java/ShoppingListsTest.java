import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import java.net.URL;


public class ShoppingListsTest {

    AndroidDriver driver;
    BuyListsPage buyListPage;
    ProductListPage productListPage;
    SettingsPage settingPage;
    OptionsSpinnerPage optionPage;

    @BeforeSuite
    public void suiteSetUp()throws Exception {
        setUpDriver();
        setUpPagesToBeTested();
    }

    /* We disable the driver after  the test has been executed. */
    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test (description = "Create a product and check appeared detailed fields.")
    public void checkAppearedProductDetailFieldsTest(){
        buyListPage.setListName("firstList");
        buyListPage.addList();

        productListPage.setProductName("aa");
        assertFalse(productListPage.doesItemPriceExist());

        productListPage.setProductName("aaa");

        assertTrue(productListPage.doesItemPriceExist());
        assertTrue(productListPage.getItemPrice().isDisplayed());
        assertTrue(productListPage.getPriceCurrency().isDisplayed());
        assertEquals(productListPage.getPriceCurrency().getText(),"£  ");
        assertTrue(productListPage.getAmount().isDisplayed());
        assertTrue(productListPage.getAmountDimention().isDisplayed());
        assertTrue(productListPage.getCategory().isDisplayed());

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

        assertEquals(productListPage.getTotalText().getText(), "Total: 65 £");

        assertTrue(productListPage.getProductNameInList("aaa").isDisplayed());
        assertTrue(productListPage.getCommentInList("Some comment").isDisplayed());
        assertTrue(productListPage.getAmountInList("3 pack").isDisplayed());
        assertTrue(productListPage.getPriceInList("5 £").isDisplayed());

        assertTrue(productListPage.getProductNameInList("bbb").isDisplayed());
        assertTrue(productListPage.getPriceInList("50 £").isDisplayed());

        assertTrue(productListPage.getProductNameInList("ccc").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkListOfProductsTest" },
            description = "Settings -> change currency option.")
    public void checkChangeCurrencyFunctionTest(){

        productListPage.clickThreePointButton();
        assertTrue(settingPage.isInitialized());
        settingPage.setSetting("Settings");
        settingPage.setSetting("Currency");
        optionPage.setOption("$");

        settingPage.returnBackFromSettings();
        assertTrue(productListPage.getPriceInList("5 $").isDisplayed());

        productListPage.setProductName("try");
        assertEquals(productListPage.getPriceCurrency().getText(),"$  ");
        productListPage.setProductName("");
    }

    @Test (dependsOnMethods = { "checkChangeCurrencyFunctionTest" },
            description = "Check change order settings button.")
    public void checkChangeOrderSettingsTest(){
        productListPage.clickThreePointButton();
        assertTrue(settingPage.isInitialized());
        settingPage = new SettingsPage(driver);
        settingPage.setSetting("Settings");
        settingPage.setSetting("Sort list");
        optionPage.setOption("In a pre-order");

        settingPage.returnBackFromSettings();

        assertEquals(productListPage.getProductNames().get(0).getText(),"ccc");
        assertEquals(productListPage.getProductNames().get(1).getText(),"bbb");
        assertEquals(productListPage.getProductNames().get(2).getText(),"aaa");
    }


    @Test (dependsOnMethods = { "checkChangeOrderSettingsTest" },
            description = "Create two new lists and inspect main page list.")
    public void checkListOfBuyListsTest(){
        productListPage.returnToBuyListsPage();

        buyListPage.setListName("secondList");
        buyListPage.addList();

        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        assertTrue(buyListPage.getListTitle("firstList").isDisplayed());
        assertTrue(buyListPage.getListInfoText("All items: 3 Sum: 65 $ Date:").isDisplayed());
        assertTrue(buyListPage.getListTitle("secondList").isDisplayed());
        assertTrue(buyListPage.getListInfoText("All items: 0 Sum: 0 $ Date:").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkListOfBuyListsTest" },
            description = "Check edit list name button on main page.")
    public void checkEditListButtonTest(){
        buyListPage.setNewListName("SecondNameForFirstList");

        assertTrue(buyListPage.getListTitle("firstListSecondNameForFirstList").isDisplayed());
    }

    @Test (dependsOnMethods = { "checkEditListButtonTest" },
            description = "Check delete list button on main page.")
    public void checkDeleteListButtonTest(){
        buyListPage.deleteList();
         assertNull(buyListPage.getListTitle("firstListSecondNameForFirstList"));
    }


    private void setUpDriver()throws Exception{
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
    }

    private void setUpPagesToBeTested(){
        buyListPage = new BuyListsPage(driver);
        productListPage = new ProductListPage(driver);
        optionPage = new OptionsSpinnerPage(driver);
        settingPage = new SettingsPage(driver);
    }
}
