import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObject {
    protected AppiumDriver driver;

    public PageObject(AppiumDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
