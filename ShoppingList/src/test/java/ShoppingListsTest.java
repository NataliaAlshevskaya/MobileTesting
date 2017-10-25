import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.WebElement;
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
    }

    @BeforeMethod
    public void setUp(){
        callShellCommand("adb shell pm clear com.slava.buylist");
        callShellCommand("adb shell am start -n com.slava.buylist/com.slava.buylist.MainActivity");
        buyListPage.setListName("firstList");
        buyListPage.addList();

    }

    /* We disable the driver after  the test has been executed. */
    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test (description = "Create two new lists and inspect main page list.")
    public void checkListOfBuyListsTest(){
        productListPage.setProductName("try");
        productListPage.setItemPrice("5");
        productListPage.addProduct();

        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        buyListPage.setListName("secondList");
        buyListPage.addList();

        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        WebElement firstListText = buyListPage.getListTitle("firstList");
        Assert.assertTrue(firstListText.isDisplayed());

        WebElement firstListUnderText = buyListPage.getListInfoText("All items: 1 Sum: 5 £ Date:");
        Assert.assertTrue(firstListUnderText.isDisplayed());


        WebElement secondListText = buyListPage.getListTitle("secondList");
        Assert.assertTrue(secondListText.isDisplayed());
        WebElement secondListUnderText = buyListPage.getListInfoText("All items: 0 Sum: 0 £ Date:");
        Assert.assertTrue(secondListUnderText.isDisplayed());
    }

    @Test (description = "Create a product and check appeared detailed fields.")
    public void checkAppearedProductDetailFieldsTest(){
        //Add product name with just 2 letters.
        productListPage.setProductName("tr");
        Assert.assertTrue(!productListPage.doesItemPriceExist());

        productListPage.setProductName("try");

        Assert.assertTrue(productListPage.doesItemPriceExist());
        Assert.assertTrue(productListPage.getItemPrice().isDisplayed());
        Assert.assertTrue(productListPage.getPriceCurrency().isDisplayed());
        Assert.assertEquals(productListPage.getPriceCurrency().getText(),"£  ");
        Assert.assertTrue(productListPage.getAmount().isDisplayed());
        Assert.assertTrue(productListPage.getAmountDimention().isDisplayed());
        Assert.assertTrue(productListPage.getCategory().isDisplayed());

    }

    @Test (description = "Create 3 products in one list and inspect list page.")
    public void checkListOfProductsTest(){
        productListPage.setProductName("try");
        productListPage.setItemPrice("5");
        productListPage.setAmount("3");


        productListPage.openDimentionPage();
        optionPage.setOption("pack");
        productListPage.setComment("Some comment");
        productListPage.openCategoryPage();
        optionPage.setOption("Medications");
        productListPage.addProduct();

        // Product with filled only name and price.
        productListPage.setProductName("try1");
        productListPage.setItemPrice("50");
        productListPage.addProduct();

        // Product with filled only name.
        productListPage.setProductName("try2");
        productListPage.addProduct();


        // Check final list page.
        productListPage.removeKeyboard();

        Assert.assertEquals(productListPage.getTotalText().getText(), "Total: 65 £");

        Assert.assertTrue(productListPage.getProductNameInList("try").isDisplayed());
        Assert.assertTrue(productListPage.getCommentInList("Some comment").isDisplayed());
        Assert.assertTrue(productListPage.getAmountInList("3 pack").isDisplayed());
        Assert.assertTrue(productListPage.getPriceInList("5 £").isDisplayed());

        Assert.assertTrue(productListPage.getProductNameInList("try1").isDisplayed());
        Assert.assertTrue(productListPage.getPriceInList("50 £").isDisplayed());

        Assert.assertTrue(productListPage.getProductNameInList("try2").isDisplayed());
    }

    @Test (description = "Check edit list name button on main page.")
    public void checkEditListButtonTest(){
        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        buyListPage.setNewListName("SecondNameForFirstList");

        Assert.assertTrue(buyListPage.getListTitle("firstListSecondNameForFirstList").isDisplayed());
    }

    @Test (description = "Check delete list button on main page.")
    public void checkDeleteListButtonTest(){

        productListPage.removeKeyboard();
        productListPage.returnToBuyListsPage();

        buyListPage.deleteList();
         Assert.assertNull(buyListPage.getListTitle("firstList"));
    }

    @Test (description = "Settings -> change currency option.")
    public void checkChangeCurrencyFunctionTest(){
        // Product with filled all possible fields.
        productListPage.setProductName("try");
        productListPage.setItemPrice("5");
        productListPage.setAmount("3");
        productListPage.addProduct();

        Assert.assertEquals(productListPage.getTotalText().getText(),"Total: 15 £");

        productListPage.clickThreePointButton();
        settingPage.setSetting("Settings");
        settingPage.setSetting("Currency");
        optionPage.setOption("$");

        settingPage.returnBackFromSettings();
        Assert.assertTrue(productListPage.getPriceInList("5 $").isDisplayed());

        productListPage.setProductName("try");
        Assert.assertEquals(productListPage.getPriceCurrency().getText(),"$  ");
        }

    @Test (description = "Check change order settings button.")
    public void checkChangeOrderSettingsTest(){
        productListPage.setProductName("aaa");
        productListPage.setItemPrice("5");
        productListPage.addProduct();

        productListPage.setProductName("bbb");
        productListPage.setItemPrice("5");
        productListPage.addProduct();

        productListPage.setProductName("ccc");
        productListPage.setItemPrice("15");
        productListPage.addProduct();

        //three point button
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
    
    
}
