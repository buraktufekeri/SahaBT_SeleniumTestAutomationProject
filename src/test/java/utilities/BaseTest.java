package utilities;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        System.out.println("Before Run : " + "Chrome Driver");
        System.out.println("Installation finish");

        driver.manage().window().maximize();
    }

    @After
    public void tearDown(){
        driver.manage().deleteAllCookies();
        driver.quit();
        System.out.println("Driver quit");
    }
}
