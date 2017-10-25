import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class BuyListsPage extends PageObject{
    @AndroidFindBy(id="button2")
    private AndroidElement plusButton;

    @AndroidFindBy(id="button1")
    private AndroidElement confirm;

    @AndroidFindBy(id="editText1")
    private AndroidElement listName;

    @AndroidFindBy(id="title")
    private List<AndroidElement> listTitle;

    @AndroidFindBy(id="str1")
    private List<AndroidElement> listInfoText;

    @AndroidFindBy(id="imageView2")
    private List<AndroidElement> editInList;

    @AndroidFindBy(id="imageView1")
    private List<AndroidElement> deleteInList;

    @AndroidFindBy(xpath="//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]")
//    @AndroidFindBy(className="android.widget.EditText")
    private AndroidElement newListName;

    public BuyListsPage(AndroidDriver<AndroidElement> driver) {
        super(driver);
//        Assert.assertTrue(listName.isDisplayed());
    }


    public AndroidElement getListTitle(String titleText) {
        for(AndroidElement title: this.listTitle){
            if(title.getText().equalsIgnoreCase(titleText)) {
                return title;
            }
        }
        return null;
    }

    public AndroidElement getListInfoText(String titleInfoTextSubstring) {
        for(AndroidElement infoText: this.listInfoText){
            if(infoText.getText().contains(titleInfoTextSubstring)){
                return infoText;
            }
        }
        return null;
    }

    public void setListName(String listName){
        this.listName.sendKeys(listName);
    }

    public void addList(){
        this.plusButton.click();
    }

    public void setNewListName(String newName){
        //TODO(nalshevskaia): find other better solution
        this.editInList.get(0).click();
        this.newListName.sendKeys(newName);
        confirm.click();
    }

    public void deleteList(){
        this.deleteInList.get(0).click();
        confirm.click();
    }

}
