import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
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

    @AndroidFindBy(className="android.widget.EditText")
    private AndroidElement newListName;

    public BuyListsPage(AndroidDriver<AndroidElement> driver) {
        super(driver);
        Assert.assertTrue(listName.isDisplayed());
    }


    public AndroidElement getListTitle(String titleText) {
        for(AndroidElement title: listTitle){
            if(title.getText().equalsIgnoreCase(titleText)) {
                return title;
            }
        }
        return null;
    }

    public AndroidElement getListInfoText(String titleInfoTextSubstring) {
        for(AndroidElement infoText: listInfoText){
            if(infoText.getText().contains(titleInfoTextSubstring)){
                return infoText;
            }
        }
        return null;
    }

    public void setListName(String listNameToBeSet){
        listName.sendKeys(listNameToBeSet);
    }

    public void addList(){
        plusButton.click();
    }

    public void setNewListName(String newName){
        //TODO(nalshevskaia): find other better solution
        editInList.get(0).click();
        newListName.sendKeys(newName);
        confirm.click();
    }

    public void deleteList(){
        deleteInList.get(0).click();
        confirm.click();
    }

}
