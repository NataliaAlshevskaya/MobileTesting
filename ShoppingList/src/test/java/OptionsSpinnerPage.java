import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class OptionsSpinnerPage extends PageObject{
    @FindBy(id="text1")
    private List<WebElement> option;

    public OptionsSpinnerPage(AppiumDriver driver) {
        super(driver);
//        Assert.assertTrue(this.option.get(0).isDisplayed());
    }

    public void setOption(String option){
        for(WebElement optionElement: this.option){
            if(optionElement.getText().equalsIgnoreCase(option)){
                optionElement.click();
                break;
            }
        }
    }
}
