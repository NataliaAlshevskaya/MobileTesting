import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
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

    public void setProductName(String productNameToBeSet){
        productName.sendKeys(productNameToBeSet);
    }

    public void setItemPrice(String itemPriceToBeSet){
        itemPrice.sendKeys(itemPriceToBeSet);
    }

    public void setAmount(String amountToBeSet){
        amount.sendKeys(amountToBeSet);
    }

    public void setComment(String commentToBeSet){
        comment.sendKeys(commentToBeSet);
    }

    public void addProduct(){
        plusButton.click();
    }

    public void openDimentionPage (){
        amountDimension.click();
    }

    public void openCategoryPage (){
        category.click();
    }

    public AndroidElement getItemPrice(){
        return itemPrice;
    }

    public AndroidElement getPriceCurrency(){
        return priceCurrency;
    }

    public AndroidElement getAmount(){
        return amount;
    }

    public AndroidElement getAmountDimention(){
        return amountDimension;
    }

    public AndroidElement getComment(){
        return comment;
    }

    public AndroidElement getCategory(){
        return category;
    }

    public AndroidElement getTotalText(){
        return totalText;
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
        return getElement(comment, commentInList);
    }

    public AndroidElement getProductNameInList(String title){
        return getElement(title, productNameInList);
    }

    public AndroidElement getAmountInList(String amount){
        return getElement(amount, amountInList);
    }

    public AndroidElement getPriceInList(String price){
        return getElement(price, priceInList);
    }

    public List<AndroidElement> getProductNames(){
        return productNameInList;
    }


    public boolean doesItemPriceExist(){
        return super.doesItemExist("com.slava.buylist:id/editText2");
    }

    public void clickThreePointButton() {
        threePointButton.click();
    }

    public void removeKeyboard(){
        super.androidBackspase();
    }

    public void returnToBuyListsPage(){
        super.androidBackspase();
    }
}
