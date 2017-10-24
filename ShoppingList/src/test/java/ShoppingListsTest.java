import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import org.testng.Assert;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class ShoppingListsTest {

    AppiumDriver driver;
    BuyListsPage buyListPage;
    ProductListPage productListPage;
    SettingsPage settingPage;
    OptionsSpinnerPage optionPage;

    private static final String productListXpath = "//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                "android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.RelativeLayout[%d]/" +
                "android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[%d]";


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

    private boolean doesExists(WebElement element){
        System.out.println(element);
        System.out.println(element.getTagName());

        if (element != null){
            if (element.isDisplayed()){
                return true;
            }
        }
        return false;
    }

    @BeforeClass
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
    }

    /* We disable the driver after  the test has been executed. */
    @AfterClass
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test (description = "Create two new lists and inspect main page list.")
    public void checkListOfBuyListsTest(){

        buyListPage = new BuyListsPage(driver);
        buyListPage.setListName("firstList");
        buyListPage.addList();

        productListPage.setProductName("try");
        productListPage.setItemPrice("5");
        productListPage.addProduct();


        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        buyListPage.setListName("secondList");
        buyListPage.addList();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

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

        buyListPage.setListName("firstList");
        buyListPage.addList();


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

        buyListPage = new BuyListsPage(driver);
        buyListPage.setListName("firstList");
        buyListPage.addList();

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
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

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
        buyListPage = new BuyListsPage(driver);
        buyListPage.setListName("firstList");
        buyListPage.addList();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        buyListPage.setNewListName("SecondNameForFirstList");

        Assert.assertTrue(buyListPage.getListTitle("firstListSecondNameForFirstList").isDisplayed());
    }

    @Test (description = "Check delete list button on main page.")
    public void checkDeleteListButtonTest(){
        buyListPage.setListName("firstList");
        buyListPage.addList();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        buyListPage.deleteList();
         Assert.assertNull(buyListPage.getListTitle("firstList"));
    }

    @Test (description = "Settings -> change currency option.")
    public void checkChangeCurrencyFunctionTest(){

        buyListPage.setListName("firstList");
        buyListPage.addList();

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

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        Assert.assertTrue(productListPage.getPriceInList("5 $").isDisplayed());

        productListPage.setProductName("try");
        Assert.assertEquals(productListPage.getPriceCurrency().getText(),"$  ");
        }

    @Test (description = "Check change order settings button.")
    public void checkChangeOrderSettingsTest(){
        buyListPage.setListName("firstList");
        buyListPage.addList();

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
        settingPage.setSetting("Settings");


        settingPage.setSetting("Sort list");
        optionPage.setOption("In a pre-order");

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        //inspect from here
        WebElement resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 1, 1)));
        Assert.assertEquals(resultListTitle.getText(), "ccc");
        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 2, 1)));
        Assert.assertEquals(resultListTitle.getText(), "bbb");
        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 3, 1)));
        Assert.assertEquals(resultListTitle.getText(), "aaa");

    }
}
