package utilities;

import com.google.common.base.Function;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasePage {

    protected WebDriver driver;
    private WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 5);
    }

    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    protected void scrolltoElement(String webElement, int... index) {
        WebElement element = findElement(webElement, index);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void fillInputField(String cssSelector, String text, int... index) {
        fillInputField(cssSelector, text, false, index);
    }

    protected void fillInputField(String cssSelector, String text, boolean pressEnter, int... index) {
        WebElement element;

        try {
            element = findElement(cssSelector, index);
            if (element.isEnabled()) {
                HighlightElement(element);
                element.clear();
                element.sendKeys(text);

                if (pressEnter) {
                    element.sendKeys(Keys.ENTER);
                }
            }
        } catch (NullPointerException e) {
            Assert.assertTrue("Nullpointer Exception for " + cssSelector, false);
        }
    }

    protected void clickElement(String cssSelector, int... index) {
        clickElement(cssSelector, true, index);
    }

    protected void clickElement(String cssSelector, boolean waitForAjax, int... index) {
        WebElement element;

        try {
            element = findElement(cssSelector, index);
            if (element == null) {
                throw new RuntimeException("ELEMENT (" + cssSelector + "," + (index.length > 0 ? index[0] : "")
                        + ") NOT EXISTS; AUTOMATION DATAS MAY BE INVALID!");
            }

            if (!isElementInView(cssSelector, index)) {
                scrollTo(element.getLocation().getX(), element.getLocation().getY());
            }
            element.click();
            if (waitForAjax) {
                untilAjaxComplete();
            }

        } catch (NullPointerException e) {
            Assert.assertTrue("Nullpointer Exception for " + cssSelector, false);
        }
    }

    protected Object executeJS(String jsStmt, boolean wait) {
        return wait ? getJSExecutor().executeScript(jsStmt, "") : getJSExecutor().executeAsyncScript(jsStmt, "");
    }

    protected void scrollTo(int x, int y) {
        String jsStmt = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(jsStmt, true);
    }

    protected void scrollToElement(WebElement element) {
        if (element != null) {
            scrollTo(element.getLocation().getX(), element.getLocation().getY());
        }
    }

    protected void scrollToElement(String cssSelector, int index) {
        WebElement element = findElement(cssSelector, index);
        scrollToElement(element);
    }

    protected boolean isElementExists(String cssSelector, int... index) {
        cssSelector = escapeCssSelector(escapeCssSelector(cssSelector));

        String jsStmt = index.length == 0 || index[0] < 0 ? String.format("return $('%s').size()>0", cssSelector)
                : String.format("return $('%s').size()>0 && $('%s').eq(%d).size()>0", cssSelector, cssSelector,
                index[0]);
        Object result = executeJS(jsStmt, true);

        return result != null && "true".equalsIgnoreCase(result.toString());
    }

    protected boolean isElementInView(String cssSelector, int... index) {
        cssSelector = escapeCssSelector(escapeCssSelector(cssSelector));

        String jsStmt = index.length == 0 || index[0] < 0
                ? String.format("return $('%s').size()>0;", cssSelector, cssSelector)
                : String.format("return $('%s').size()>0;", cssSelector, cssSelector, index[0]);
        Object result = executeJS(jsStmt, true);

        return result != null && "true".equalsIgnoreCase(result.toString());
    }

    protected WebElement findElement(String cssSelector, int... index) {
        WebElement el = null;
        if (index.length == 0) {
            try {
                el = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            } catch (Exception e) {
                return null;
            }
        } else if (index[0] >= 0) {
            try {
                el = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector))).get(index[0]);
            } catch (Exception e) {
                return null;
            }
        }
        HighlightElement(el);

        return el;
    }

    private String escapeCssSelector(String selector) {
        return selector.replaceAll(Pattern.quote(":"), Matcher.quoteReplacement("\\:"));
    }

    // Focus olunan elementin etrafını istenilen renkte ve kalınlıkta çizme işlemini gerçekleştirir.
    private void HighlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                "color: red; border: 1px dashed red;");
    }

    // Mouse Hover işlemini gerçekleştirir.
    protected void HoverElement(String cssSelector, int index, boolean click) {
        Actions action = new Actions(driver);
        action.moveToElement(findElement(cssSelector, index)).build().perform();
        if (click) {
            clickElement(cssSelector, index);
        }
    }

    protected void HoverElement(String cssSelector, int index) {
        HoverElement(cssSelector, index, false);
    }

    protected boolean isElementDisplayed(String cssSelector, int... index) {
        boolean found = false;

        try {
            if (findElement(cssSelector, index) != null)
                found = true;
        } catch (NullPointerException e) {
            found = false;
        }
        return found;
    }

    protected void untilAjaxComplete() {
        new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
                .until(new Function<WebDriver, Boolean>() {
                    public Boolean apply(WebDriver input) {
                        return wait
                                .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".pace-active")));
                    }
                });
        isErrorMessageDisplayed();
        closeErrorMessageDialog();
    }

    protected boolean isErrorMessageDisplayed() {
        return isElementInView(".alert") || isElementInView(".phpErrorsForDeveloper");
    }

    protected void closeErrorMessageDialog() {
        if (isErrorMessageDisplayed()) {
            if (isElementExists(".phpErrorsForDeveloper")) {
                executeJS("$('.phpErrorsForDeveloper').hide()", true);
            }
        }
    }

    public abstract void navigateTo();
}