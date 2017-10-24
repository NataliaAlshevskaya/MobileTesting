import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SettingsPage extends PageObject{
    @FindBy(id="title")
    private List<WebElement> setting;

    public SettingsPage(AppiumDriver driver) {
        super(driver);
    }

    public void setSetting(String setting){
        for(WebElement settingElement: this.setting){
            if(settingElement.getText().equalsIgnoreCase(setting)){
                settingElement.click();
                break;
            }
        }
    }
}
