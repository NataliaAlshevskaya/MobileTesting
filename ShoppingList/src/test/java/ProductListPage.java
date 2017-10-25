import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import java.util.List;

public class ProductListPage extends PageObject{
    @AndroidFindBy(id="editText1")
    private AndroidElement productName;

    @AndroidFindBy(id="editText2")
    private AndroidElement itemPrice;

    @AndroidFindBy(id="editText3")
    private AndroidElement amount;

    @AndroidFindBy(id="editText4")
    private AndroidElement comment;

    @AndroidFindBy(id="button1")
    private AndroidElement threePointButton;

    @AndroidFindBy(id="button2")
    private AndroidElement plusButton;

    @AndroidFindBy(id="value")
    private AndroidElement priceCurrency;

    @AndroidFindBy(id="spinner1")
    private AndroidElement amountDimension;

    @AndroidFindBy(id="spinner2")
    private AndroidElement category;

    @AndroidFindBy(id="textView2")
    private AndroidElement totalText;

    @AndroidFindBy(id="title")
    private List<AndroidElement> productNameInList;

    @AndroidFindBy(id="str1")
    private List<AndroidElement> commentInList;

    @AndroidFindBy(id="TextView01")
    private List<AndroidElement> amountInList;

    @AndroidFindBy(id="textView1")
    private List<AndroidElement> priceInList;

    public ProductListPage(AndroidDriver<AndroidElement> driver) {
        super(driver);
        Assert.assertTrue(plusButton.isDisplayed());
    }

    public void setProductName(String productName){
        this.productName.sendKeys(productName);
    }

    public void setItemPrice(String itemPrice){
        this.itemPrice.sendKeys(itemPrice);
    }

    public void setAmount(String amount){
        this.amount.sendKeys(amount);
    }

    public void setComment(String comment){
        this.comment.sendKeys(comment);
    }

    public void addProduct(){
        this.plusButton.click();
    }

    public void openDimentionPage (){
        this.amountDimension.click();
    }

    public void openCategoryPage (){
        this.category.click();
    }

    public AndroidElement getItemPrice(){
        return this.itemPrice;
    }

    public AndroidElement getPriceCurrency(){
        return this.priceCurrency;
    }

    public AndroidElement getAmount(){
        return this.amount;
    }

    public AndroidElement getAmountDimention(){
        return this.amountDimension;
    }

    public AndroidElement getComment(){
        return this.comment;
    }

    public AndroidElement getCategory(){
        return this.category;
    }

    public AndroidElement getTotalText(){
        return this.totalText;
    }

    private AndroidElement getElement(String text, List<AndroidElement> list){
        for(AndroidElement element: list){
            if(element.getText().equalsIgnoreCase(text)){
                return element;
            }
        }
        return null;
    }
    public AndroidElement getCommentInList(String comment){
        return getElement(comment, this.commentInList);
    }

    public AndroidElement getProductNameInList(String title){
        return getElement(title, this.productNameInList);
    }

    public AndroidElement getAmountInList(String amount){
        return getElement(amount, this.amountInList);
    }

    public AndroidElement getPriceInList(String price){
        return getElement(price, this.priceInList);
    }

    public List<AndroidElement> getProductNames(){
        return this.productNameInList;
    }


    public boolean doesItemPriceExist(){
        return super.doesItemExist("com.slava.buylist:id/editText2");
    }

    public void clickThreePointButton() {
        this.threePointButton.click();
    }

    public void removeKeyboard(){
        super.androidBackspase();
    }

    public void returnToBuyListsPage(){
        super.androidBackspase();
    }
}
