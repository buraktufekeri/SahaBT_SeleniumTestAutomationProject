package page;

import org.openqa.selenium.WebDriver;
import utilities.BasePage;

public class GeneralTestPage extends BasePage{

    public GeneralTestPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateTo() {
        if (!driver.getCurrentUrl().equals("https://www.n11.com/"))
            driver.navigate().to("https://www.n11.com/");
    }

    public String expectedURL(){
        return driver.getCurrentUrl();
    }

    public void loginButton(){
        clickElement(".btnSignIn"); // Giriş yap butonuna tıkla
    }

    public void setLoginData(String username, String password){
        fillInputField("#email", username); // Kullanıcı adı gir
        fillInputField("#password", password); // Şifre gir
    }

    public void memberLoginButton(){
        clickElement("#loginButton"); // Üye girişi butonuna tıkla
    }

    public boolean isLogin(){
        try {
            return isElementDisplayed(".user");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void searchData(String searchDataValue){
        fillInputField("#searchData", searchDataValue); // Kelimeyi ara
        clickElement(".searchBtn"); // Ara butonu
    }

    public String searchDataControl(){
        try {
            return findElement("#breadCrumb span", 1).getText();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getSecondPage(){
        scrollToElement(".pagination a", 1);
        clickElement(".pagination a", 1); // İkinci sayfaya git
    }

    public String paginationControl(){
        try {
            scrollToElement(".pagination a", 2);
            return findElement(".pagination a", 2).getText();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void addFavorite(){
        scrolltoElement(".followBtn", 1);
        clickElement(".followBtn", 2); // Listelemedeki 3.ürüne favorilere ekle
    }

    public void goMyFavorites(){
        scrollToElement(".myAccount", 0);
        HoverElement(".myAccount", 0);
        clickElement(".hOpenMenuContent a", 1); // Hesabım > İstek Listeme git
        clickElement(".listItemTitle"); // Favorilerime git
    }

    public String expectedMyFavoritesPageURL(){
        return driver.getCurrentUrl();
    }

    public void deleteProduct(){
        clickElement(".deleteProFromFavorites"); // Favorilerimdeki ürünü sil
        clickElement(".confirm"); // Tamam popup ı
    }

    public boolean isDeleted(){
        try {
            return isElementDisplayed(".productName");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void exitButton(){
        HoverElement(".myAccount", 0);
        clickElement(".hOpenMenuContent a", 8); // Çıkış yap
    }
}
