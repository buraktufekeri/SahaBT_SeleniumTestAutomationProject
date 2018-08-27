package test;

import org.junit.Assert;
import org.junit.Test;
import page.GeneralTestPage;
import utilities.BaseTest;

public class GeneralTest extends BaseTest{

    private static GeneralTestPage generalTestPage;

    @Test
    public void generalTest(){
        generalTestPage = new GeneralTestPage(driver);

        generalTestPage.navigateTo();
        Assert.assertEquals("Expected web page URL is wrong.",
                generalTestPage.expectedURL(), "https://www.n11.com/");

        generalTestPage.loginButton();
        generalTestPage.setLoginData("seleniumwebdriver25@gmail.com", "s14789652");
        generalTestPage.memberLoginButton();
        Assert.assertTrue("Member Login failed.", generalTestPage.isLogin());

        generalTestPage.searchData("Samsung");
        Assert.assertEquals("Samsung word not found.", generalTestPage.searchDataControl(), "Samsung");

        generalTestPage.getSecondPage();
        Assert.assertEquals("The pagination is not working.", generalTestPage.paginationControl(), "2");

        generalTestPage.addFavorite();
        generalTestPage.goMyFavorites();
        Assert.assertEquals("Expected favorites web page url is wrong.",
                generalTestPage.expectedMyFavoritesPageURL(), "https://www.n11.com/hesabim/favorilerim");

        generalTestPage.deleteProduct();
        Assert.assertFalse("The product in my favorites could not be deleted.", generalTestPage.isDeleted());

        generalTestPage.exitButton();
    }
}
