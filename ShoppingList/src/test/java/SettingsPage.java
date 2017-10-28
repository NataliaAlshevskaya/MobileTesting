import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.android.AndroidElement;

import java.util.List;

public class SettingsPage extends PageObject{

    @AndroidFindBy(id="android:id/title")
    private List<AndroidElement> setting;

    public SettingsPage(AndroidDriver<AndroidElement> driver) {
        super(driver);
    }

    public boolean isInitialized(){
        return super.isInitialized(setting.get(0));
    }

    public void setSetting(String settingToBeSet){
        for(AndroidElement settingElement: setting){
            if(settingElement.getText().equalsIgnoreCase(settingToBeSet)){
                settingElement.click();
                break;
            }
        }
    }

    public void returnBackFromSettings(){
        super.androidBackspase();
    }
}
