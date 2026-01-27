package main.java.pompages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By[] emailLocators = new By[]{
            By.cssSelector("input[id$='txtEmail']"),
            By.cssSelector("input[name$='txtEmail']"),
            By.cssSelector("input[type='email']"),
            By.cssSelector("input[placeholder*='Email']"),
            By.xpath("//label[contains(normalize-space(.), 'Email')]/following::input[1]")
    };

    private final By[] passwordLocators = new By[]{
            By.cssSelector("input[id$='txtPassword']"),
            By.cssSelector("input[name$='txtPassword']"),
            By.cssSelector("input[type='password']"),
            By.cssSelector("input[placeholder*='Password']"),
            By.xpath("//label[contains(normalize-space(.), 'Password')]/following::input[1]")
    };

    private final By[] loginButtonLocators = new By[]{
            By.cssSelector("input[id$='btnLogin']"),
            By.cssSelector("button[id$='btnLogin']"),
            By.cssSelector("input[type='submit'][value*='Login']"),
            By.xpath("//button[contains(normalize-space(.), 'Login') or contains(normalize-space(.), 'Sign In')]")
    };

    private final By errorMessageLocator = By.xpath("//div[contains(@class,'error-message')]");
    private final By dashboardHeaderLocator = By.xpath("//h1[contains(text(),'Dashboard')] | //span[contains(text(),'Dashboard')]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Actions
    public void navigateToLoginPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.urlContains("Login"));
    }

    public void enterEmail(String email) {
        WebElement field = waitForVisible(emailLocators);
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = waitForVisible(passwordLocators);
        field.clear();
        field.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement button = waitForClickable(loginButtonLocators);
        button.click();
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("dashboard"),
                    ExpectedConditions.visibilityOfElementLocated(dashboardHeaderLocator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            return findVisibleInAnyFrame(errorMessageLocator) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    private WebElement waitForVisible(By... locators) {
        return wait.until(driver -> {
            for (By locator : locators) {
                WebElement element = findVisibleInAnyFrame(locator);
                if (element != null) {
                    return element;
                }
            }
            return null;
        });
    }

    private WebElement waitForClickable(By... locators) {
        return wait.until(driver -> {
            for (By locator : locators) {
                WebElement element = findVisibleInAnyFrame(locator);
                if (element != null && element.isEnabled()) {
                    return element;
                }
            }
            return null;
        });
    }

    private WebElement findVisibleInAnyFrame(By locator) {
        driver.switchTo().defaultContent();
        WebElement element = findVisibleInCurrentContext(locator);
        if (element != null) {
            return element;
        }
        List<WebElement> frames = driver.findElements(By.cssSelector("iframe,frame"));
        for (WebElement frame : frames) {
            try {
                driver.switchTo().defaultContent();
                driver.switchTo().frame(frame);
                element = findVisibleInCurrentContext(locator);
                if (element != null) {
                    return element;
                }
            } catch (NoSuchFrameException | StaleElementReferenceException ignored) {
            }
        }
        driver.switchTo().defaultContent();
        return null;
    }

    private WebElement findVisibleInCurrentContext(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
        } catch (StaleElementReferenceException ignored) {
            return null;
        }
        return null;
    }
}