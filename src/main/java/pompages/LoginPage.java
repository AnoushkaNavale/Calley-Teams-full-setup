package main.java.pompages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

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
            By.cssSelector("input[name$='btnLogin']"),
            By.cssSelector("button[type='submit']"),
            By.cssSelector("input[type='submit']"),
            By.cssSelector("input[type='button'][value*='Login']"),
            By.cssSelector("input[type='submit'][value*='Login']"),
            By.xpath("//button[contains(normalize-space(.), 'Login') or contains(normalize-space(.), 'Log In') or contains(normalize-space(.), 'Sign In')]"),
            By.xpath("//a[contains(normalize-space(.), 'Login') or contains(normalize-space(.), 'Log In') or contains(normalize-space(.), 'Sign In')]")
    };

    private final By errorMessageLocator = By.xpath("//div[contains(@class,'error-message')]");
    private final By dashboardHeaderLocator = By.xpath("//h1[contains(text(),'Dashboard')] | //span[contains(text(),'Dashboard')]");
    private final By goToDashboardLocator = By.xpath("//a[contains(normalize-space(.), 'Go to Dashboard') or contains(normalize-space(.), 'Go To Dashboard')]");
    private final By autodialComputerLocator = By.xpath("//a[contains(normalize-space(.), 'Autodial using your computer')]");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // Actions
    public void navigateToLoginPage(String url) {
        driver.get(url);
        try {
            waitForVisible(emailLocators);
            return;
        } catch (TimeoutException ignored) {
            // Fall through for redirected landing page.
        }

        // Some flows redirect to a landing page before login.
        if (clickIfPresent(goToDashboardLocator) || clickIfPresent(autodialComputerLocator)) {
            waitForVisible(longWait, emailLocators);
            return;
        }

        // If still not found, surface diagnostics to guide locator updates.
        logLoginDiagnostics();
        throw new TimeoutException("Login form not found after navigating to " + url);
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
        try {
            WebElement button = waitForClickable(longWait, loginButtonLocators);
            button.click();
        } catch (TimeoutException e) {
            WebElement fallback = findLoginButtonNearEmail();
            if (fallback != null) {
                fallback.click();
                return;
            }
            logLoginDiagnostics();
            throw e;
        }
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

    private WebElement waitForVisible(WebDriverWait waiter, By... locators) {
        return waiter.until(driver -> {
            for (By locator : locators) {
                WebElement element = findVisibleInAnyFrame(locator);
                if (element != null) {
                    return element;
                }
            }
            return null;
        });
    }

    private WebElement waitForVisible(By... locators) {
        return waitForVisible(wait, locators);
    }

    private WebElement waitForClickable(WebDriverWait waiter, By... locators) {
        return waiter.until(driver -> {
            for (By locator : locators) {
                WebElement element = findVisibleInAnyFrame(locator);
                if (element != null && element.isEnabled()) {
                    return element;
                }
            }
            return null;
        });
    }

    private WebElement waitForClickable(By... locators) {
        return waitForClickable(wait, locators);
    }

    private WebElement findLoginButtonNearEmail() {
        WebElement emailField = findVisibleInAnyFrame(emailLocators[0]);
        if (emailField == null) {
            emailField = findVisibleInAnyFrame(By.cssSelector("input[type='email'], input[placeholder*='Email']"));
        }
        if (emailField == null) {
            return null;
        }

        WebElement form = null;
        try {
            form = emailField.findElement(By.xpath("ancestor::form[1]"));
        } catch (Exception ignored) {
        }

        List<WebElement> candidates = form != null
                ? form.findElements(By.cssSelector("button, input[type='submit'], input[type='button']"))
                : driver.findElements(By.cssSelector("button, input[type='submit'], input[type='button']"));

        WebElement fallback = null;
        for (WebElement candidate : candidates) {
            if (!candidate.isDisplayed()) {
                continue;
            }
            String text = getElementText(candidate);
            if (containsIgnoreCase(text, "login") || containsIgnoreCase(text, "log in")
                    || containsIgnoreCase(text, "sign in")) {
                return candidate;
            }
            if (candidate.isEnabled() && fallback == null) {
                fallback = candidate;
            }
        }
        return fallback;
    }

    private boolean clickIfPresent(By locator) {
        WebElement element = findVisibleInAnyFrame(locator);
        if (element != null) {
            element.click();
            return true;
        }
        return false;
    }

    private void logLoginDiagnostics() {
        System.out.println("[Login] URL: " + driver.getCurrentUrl());
        System.out.println("[Login] Title: " + driver.getTitle());
        boolean captchaPresent = !driver.findElements(By.cssSelector("iframe[src*='recaptcha'], div.g-recaptcha"))
                .isEmpty();
        if (captchaPresent) {
            System.out.println("[Login] reCAPTCHA detected. Complete it manually during the run.");
        }
        List<WebElement> elements = driver.findElements(By.cssSelector("input, button, a"));
        int shown = 0;
        for (WebElement element : elements) {
            if (!element.isDisplayed()) {
                continue;
            }
            System.out.println("[Login] element: tag=" + element.getTagName()
                    + " type=" + safeAttr(element, "type")
                    + " id=" + safeAttr(element, "id")
                    + " name=" + safeAttr(element, "name")
                    + " value=" + safeAttr(element, "value")
                    + " text=" + truncate(getElementText(element))
                    + " placeholder=" + safeAttr(element, "placeholder")
                    + " enabled=" + element.isEnabled());
            shown++;
            if (shown >= 12) {
                break;
            }
        }
    }

    private String safeAttr(WebElement element, String name) {
        String value = element.getAttribute(name);
        return value == null ? "" : value;
    }

    private String getElementText(WebElement element) {
        if ("input".equalsIgnoreCase(element.getTagName())) {
            String value = element.getAttribute("value");
            return value == null ? "" : value;
        }
        return element.getText();
    }

    private boolean containsIgnoreCase(String value, String needle) {
        if (value == null) {
            return false;
        }
        return value.toLowerCase().contains(needle.toLowerCase());
    }

    private String truncate(String value) {
        if (value == null) {
            return "";
        }
        return value.length() > 60 ? value.substring(0, 60) + "..." : value;
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