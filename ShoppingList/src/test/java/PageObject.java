import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

public class PageObject {
    AndroidDriver<AndroidElement> driver;

    public PageObject(AndroidDriver<AndroidElement> driver){
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, 30, TimeUnit.SECONDS), this);
    }

    public boolean doesItemExist(String id){
        try{
            driver.findElement(By.id(id));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isInitialized(AndroidElement element){
        return element.isDisplayed();
    }

    public void androidBackspase(){
        ((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
    }
}
