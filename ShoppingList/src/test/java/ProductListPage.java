import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class ProductListPage extends PageObject{
    @FindBy(id="editText1")
    private WebElement productName;

    @FindBy(id="editText2")
    private WebElement itemPrice;

    @FindBy(id="editText3")
    private WebElement amount;

    @FindBy(id="editText4")
    private WebElement comment;

    @FindBy(id="button1")
    private WebElement threePointButton;

    @FindBy(id="button2")
    private WebElement plusButton;

    @FindBy(id="value")
    private WebElement priceCurrency;

    @FindBy(id="spinner1")
    private WebElement amountDimension;

    @FindBy(id="spinner2")
    private WebElement category;

    @FindBy(id="textView2")
    private WebElement totalText;

    @FindBy(id="title")
    private List<WebElement> productNameInList;

    @FindBy(id="str1")
    private List<WebElement> commentInList;

    @FindBy(id="TextView01")
    private List<WebElement> amountInList;

    @FindBy(id="textView1")
    private List<WebElement> priceInList;

    public ProductListPage(AppiumDriver driver) {
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

    public void setAmountDimention(String amountDimention){

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

    public WebElement getItemPrice(){
        return this.itemPrice;
    }

    public WebElement getPriceCurrency(){
        return this.priceCurrency;
    }

    public WebElement getAmount(){
        return this.amount;
    }

    public WebElement getAmountDimention(){
        return this.amountDimension;
    }

    public WebElement getComment(){
        return this.comment;
    }

    public WebElement getCategory(){
        return this.category;
    }

    public WebElement getTotalText(){
        return this.totalText;
    }

    private WebElement getElement(String text, List<WebElement> list){
        for(WebElement element: list){
            if(element.getText().equalsIgnoreCase(text)){
                return element;
            }
        }
        return null;
    }
    public WebElement getCommentInList(String comment){
        return getElement(comment, this.commentInList);
    }

    public WebElement getProductNameInList(String title){
        return getElement(title, this.productNameInList);
    }

    public WebElement getAmountInList(String amount){
        return getElement(amount, this.amountInList);
    }

    public WebElement getPriceInList(String price){
        return getElement(price, this.priceInList);
    }


    public boolean doesItemPriceExist(){
        try{
            driver.findElement(By.id("com.slava.buylist:id/editText2"));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void clickThreePointButton() {
        this.threePointButton.click();
    }
}
