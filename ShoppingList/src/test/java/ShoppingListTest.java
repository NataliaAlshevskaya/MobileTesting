import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.Assert;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class ShoppingListTest {

    AppiumDriver driver;

    private static final String productListXpath = "//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                "android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.RelativeLayout[%d]/" +
                "android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[%d]";

    private void callShellCommand(String command){
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String listsXpath = "//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                "android.widget.RelativeLayout[1]/android.widget.ListView[1]/" +
                "android.widget.LinearLayout[%d]/" +
                "android.widget.RelativeLayout[1]/android.widget.TextView[%d]";

        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();
        WebElement addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("try");
        WebElement priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
        priceText.sendKeys("5");
        plusButton.click();


        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("secondList");
        plusButton.click();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        WebElement firstListText = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(listsXpath, 1, 1))));
        WebElement firstListUnderText = driver.findElement(By.xpath(String.format(listsXpath, 1, 2)));
        Assert.assertEquals(firstListText.getText(),"firstList");
        Assert.assertEquals(firstListUnderText.getText().substring(0, 27),"All items: 1 Sum: 5 £ Date:");
        WebElement secondListText = driver.findElement(By.xpath(String.format(listsXpath, 2, 1)));
        WebElement secondListUnderText = driver.findElement(By.xpath(String.format(listsXpath, 2, 2)));
        Assert.assertEquals(secondListText.getText(),"secondList");
        Assert.assertEquals(secondListUnderText.getText().substring(0, 27),"All items: 0 Sum: 0 £ Date:");
    }

    @Test (description = "Create a product and check appeared detailed fields.")
    public void checkAppearedProductDetailFieldsTest(){

        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();

        //Add product name with just 2 letters.
        WebElement addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("tr");
        try{
            WebElement priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
            throw new Exception("A shopping list wasn't deleted by 'delete' button!");
        } catch (Exception e){
            Assert.assertEquals(e.getMessage().substring(0, 78),
                    "An element could not be located on the page using the given search parameters.");
        }
        addProduct.sendKeys("try");
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/editText2")).isDisplayed(),true);
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/value")).getText(), "£  ");
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/editText3")).isDisplayed(),true);
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/spinner1")).isDisplayed(),true);
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/editText4")).isDisplayed(),true);
        Assert.assertEquals(driver.findElement(By.id("com.slava.buylist:id/editText4")).isDisplayed(),true);
    }

    @Test (description = "Create 3 products in one list and inspect list page.")
    public void checkListOfProductsTest(){


        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();
        // Product with filled all possible fields.
        WebElement addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("try");
        WebElement priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
        priceText.sendKeys("5");
        WebElement amountText = driver.findElement(By.id("com.slava.buylist:id/editText3"));
        amountText.sendKeys("3");
        WebElement amountSpinner = driver.findElement(By.id("com.slava.buylist:id/spinner1"));
        amountSpinner.click();
        String optionsXpath = "//android.widget.FrameLayout[1]/" +
                "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                "android.widget.ListView[1]/android.widget.CheckedTextView[%d]";
        //  click on "pack" option
        WebElement	optionPack = driver.findElement(By.xpath(String.format(optionsXpath,3)));
        optionPack.click();
        WebElement commentText = driver.findElement(By.id("com.slava.buylist:id/editText4"));
        commentText.sendKeys("Some comment");
        WebElement categorySpinner = driver.findElement(By.id("com.slava.buylist:id/spinner2"));
        categorySpinner.click();
        WebElement	optionCategory=driver.findElement(By.xpath(String.format(optionsXpath,6)));
        optionCategory.click();
        plusButton.click();

        // Product with filled only name and price.
        addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("try1");
        priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
        priceText.sendKeys("50");
        plusButton.click();

        // Product with filled only name.
        addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("try2");
        plusButton.click();


        // Check finel list page.
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        WebElement voiceButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        Assert.assertEquals(true, voiceButton.isDisplayed());
        WebElement totalText = driver.findElement(By.id("com.slava.buylist:id/textView2"));
        Assert.assertEquals(totalText.getText(), "Total: 65 £");


        WebElement resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 1, 1)));
        Assert.assertEquals(resultListTitle.getText(), "try1");
        WebElement franks = driver.findElement(By.xpath(String.format(productListXpath, 1, 3)));
        Assert.assertEquals(franks.getText(), "50 £");

        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 2, 1)));
        Assert.assertEquals(resultListTitle.getText(), "try2");


        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 3, 1)));
        Assert.assertEquals(resultListTitle.getText(), "try");
        WebElement comment = driver.findElement(By.xpath(String.format(productListXpath, 3, 2)));
        Assert.assertEquals(comment.getText(), "Some comment");
        WebElement pack = driver.findElement(By.xpath(String.format(productListXpath, 3, 3)));
        Assert.assertEquals(pack.getText(), "3 pack");
        franks = driver.findElement(By.xpath(String.format(productListXpath, 3, 4)));
        Assert.assertEquals(franks.getText(), "5 £");

    }

    @Test (description = "Settings -> change currency option.")
    public void checkChangeCurrencyFunctionTest(){

        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();
        // Product with filled all possible fields.
        WebElement addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("try");
        WebElement priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
        priceText.sendKeys("5");
        WebElement amountText = driver.findElement(By.id("com.slava.buylist:id/editText3"));
        amountText.sendKeys("3");

        plusButton.click();

        WebElement totalText = driver.findElement(By.id("com.slava.buylist:id/textView2"));
        Assert.assertEquals(totalText.getText(), "Total: 15 £");

        // three point button
        driver.findElement(By.id("com.slava.buylist:id/button1")).click();
        //settings button
        driver.findElement(By.xpath("//android.widget.ListView[1]/android.widget.LinearLayout[1]")).click();
        //currency button
        (new WebDriverWait(driver, 10))
                 .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/" +
                         "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/" +
                         "android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/" +
                         "android.widget.LinearLayout[2]"))).click();
        //dollar button
        (new WebDriverWait(driver, 10))
                 .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[1]/" +
                         "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]" +
                         "/android.widget.ListView[1]/android.widget.CheckedTextView[2]"))).click();
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        WebElement dollars5 = driver.findElement(By.xpath(String.format(productListXpath, 1, 4)));
        Assert.assertEquals(dollars5.getText(), "5 $");

        addProduct.sendKeys("try1");
        WebElement priceValueLabel = driver.findElement(By.id("com.slava.buylist:id/value"));
        Assert.assertEquals(priceValueLabel.getText(), "$  ");
        }

    @Test (description = "Check edit list name button on main page.")
    public void checkEditListButtonTest(){
        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        WebElement editListButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/" +
                        "android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/" +
                        "android.widget.ListView[1]/android.widget.LinearLayout[1]/" +
                        "android.widget.RelativeLayout[1]/android.widget.ImageView[1]")));
        editListButton.click();

        WebElement newListNameFieldText = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[1]/" +
                        "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                        "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]")));
        newListNameFieldText.sendKeys("SecondNameForFirstList");

        driver.findElement(By.id("android:id/button1")).click();

        WebElement newNameOfList = driver.findElement(By.xpath("//android.widget.LinearLayout[1]/" +
                "android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ListView[1]/" +
                "android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.TextView[1]"));
        Assert.assertEquals(newNameOfList.getText(), "firstListSecondNameForFirstList");

    }

    @Test (description = "Check delete list button on main page.")
    public void checkDeleteListButtonTest(){
        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        WebElement deleteListButton = driver.findElement(By.xpath("//android.widget.LinearLayout[1]/" +
                "android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ListView[1]/" +
                "android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.ImageView[2]"));
        deleteListButton.click();

        WebElement confirmButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/button1")));
        confirmButton.click();

        try{
            driver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                    "android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/" +
                    "android.widget.RelativeLayout[1]/android.widget.TextView[1]"));
            throw new Exception("A shopping list wasn't deleted by 'delete' button!");
        } catch (Exception e){
            Assert.assertEquals(e.getMessage().substring(0, 78),
                    "An element could not be located on the page using the given search parameters.");
        }

    }

    @Test (description = "Check change order settings button.")
    public void checkChangeOrderSettingsTest(){
        WebElement plusButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
        WebElement listName = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        listName.sendKeys("firstList");
        plusButton.click();

        WebElement addProduct = driver.findElement(By.id("com.slava.buylist:id/editText1"));
        addProduct.sendKeys("aaa");
        WebElement priceText = driver.findElement(By.id("com.slava.buylist:id/editText2"));
        priceText.sendKeys("5");
        plusButton.click();

        addProduct.sendKeys("bbb");
        priceText.sendKeys("5");
        plusButton.click();


        addProduct.sendKeys("ccc");
        priceText.sendKeys("15");
        plusButton.click();

        //three point button
        driver.findElement(By.id("com.slava.buylist:id/button1")).click();
        //settings button
        driver.findElement(By.xpath("//android.widget.ListView[1]/android.widget.LinearLayout[1]")).click();
        //sort list button
        WebElement sortListButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/" +
                        "android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/" +
                        "android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/" +
                        "android.widget.LinearLayout[1]")));
        sortListButton.click();
        //in a pre-order
        driver.findElement(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/" +
                "android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.ListView[1]/" +
                "android.widget.CheckedTextView[3]")).click();

        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

        //inpect from here

        WebElement resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 1, 1)));
        Assert.assertEquals(resultListTitle.getText(), "ccc");
        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 2, 1)));
        Assert.assertEquals(resultListTitle.getText(), "bbb");
        resultListTitle = driver.findElement(By.xpath(String.format(productListXpath, 3, 1)));
        Assert.assertEquals(resultListTitle.getText(), "aaa");

    }

//    @Test (description = "Check add category settings.")
//    public void checkAddNewCategoryButtonTest() throws Exception {
//        //three point button
//        driver.findElement(By.id("com.slava.buylist:id/button1")).click();
//        //settings button
//        driver.findElement(By.xpath("//android.widget.ListView[1]/android.widget.LinearLayout[1]")).click();
//
//        //category button
//        By categoryButton = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[9]");
//        scrollToElement(categoryButton);
//        driver.findElement(categoryButton).click();
//
//        //  add category button
//        WebElement addCategoryButton = driver.findElement(By.id("com.slava.buylist:id/button2"));
//        addCategoryButton.click();
//
//        WebElement addCategoryName = (new WebDriverWait(driver, 10))
//                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]")));
//        addCategoryName.sendKeys("NewCategoryName");
//        driver.findElement(By.id("android:id/button1")).click();
//
////        WebElement addColorToCategory = (new WebDriverWait(driver, 10))
////                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[13]/android.widget.RelativeLayout[1]/android.widget.ImageView[2]")));
////        addColorToCategory.click();
//
//        By newCategoryElementBy = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[12]/android.widget.RelativeLayout[1]/android.widget.TextView[1]");
//
//        Boolean isFoundElement;
//        isFoundElement = driver.findElements(newCategoryElementBy).size() > 0;
//        while (isFoundElement == false) {
//            swipeVertical((AppiumDriver) driver, 0.9, 0.1, 0.5, 2000);
//            isFoundElement = driver.findElements(newCategoryElementBy).size() > 0;
//        }
//        //when this line doesn't scroll in previous part
//        driver.findElement(newCategoryElementBy).click();
//
//
////        scrollToElement(newCategoryElementBy);
////        WebElement newCategoryElement = driver.findElement(newCategoryElementBy);
////        Assert.assertEquals(newCategoryElement.getText(),"NewCategoryName");
//
//    }
//
//    public void scrollToElement(By my_element) throws Exception {
//        Boolean isFoundElement;
//        isFoundElement = driver.findElements(my_element).size() > 0;
//        while (isFoundElement == false){
//            swipeVertical((AppiumDriver) driver,0.9,0.1,0.5,2000);
//            isFoundElement = driver.findElements(my_element).size() > 0;
//        }
//    }
//
//    public static void swipeVertical(AppiumDriver driver, double startPercentage, double finalPercentage, double anchorPercentage, int duration) throws Exception {
//        Dimension size = driver.manage().window().getSize();
//        int anchor = (int) (size.width * anchorPercentage);
//        int startPoint = (int) (size.height * startPercentage);
//        int endPoint = (int) (size.height * finalPercentage);
//        new TouchAction(driver).press(anchor, startPoint).waitAction(duration).moveTo(anchor, endPoint).release().perform();
//    }
}
