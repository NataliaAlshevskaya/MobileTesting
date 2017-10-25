import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

import java.util.List;

public class OptionsSpinnerPage extends PageObject{
    @AndroidFindBy(id="text1")
    private List<AndroidElement> option;

    public OptionsSpinnerPage(AndroidDriver<AndroidElement> driver) {
        super(driver);
    }

    public boolean isInitialized(){
        return super.isInitialized(option.get(0));
    }

    public void setOption(String option){
        for(AndroidElement optionElement: this.option){
            if(optionElement.getText().equalsIgnoreCase(option)){
                optionElement.click();
                break;
            }
        }
    }
}
