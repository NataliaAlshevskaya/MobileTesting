import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class BuyListsPage extends PageObject{
    @FindBy(id="button2")
    private WebElement plusButton;

    @FindBy(id="button1")
    private WebElement confirm;

    @FindBy(id="editText1")
    private WebElement listName;

    @FindBy(id="title")
    private List<WebElement> listTitle;

    @FindBy(id="str1")
    private List<WebElement> listInfoText;

    @FindBy(id="imageView2")
    private List<WebElement> editInList;

    @FindBy(id="imageView1")
    private List<WebElement> deleteInList;

    @FindBy(xpath="//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]")
//    @FindBy(className="EditText")
    private WebElement newListName;

    public BuyListsPage(AppiumDriver driver) {
        super(driver);
        Assert.assertTrue(listName.isDisplayed());
    }


    public WebElement getListTitle(String titleText) {
        for(WebElement title: this.listTitle){
            if(title.getText().equalsIgnoreCase(titleText)) {
                return title;
            }
        }
        return null;
    }

    public WebElement getListInfoText(String titleInfoTextSubstring) {
        for(WebElement infoText: this.listInfoText){
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
