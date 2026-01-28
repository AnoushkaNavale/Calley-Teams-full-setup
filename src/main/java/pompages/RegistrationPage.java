package main.java.pompages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegistrationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    private final WebDriverWait longWait;

    private final By[] firstNameLocators = new By[]{
            By.cssSelector("input[id$='txtFirstName']"),
            By.cssSelector("input[name$='txtFirstName']"),
            By.cssSelector("input[placeholder*='Your Name']"),
            By.cssSelector("input[placeholder*='First']"),
            By.cssSelector("input[placeholder*='Name']"),
            By.xpath("//label[contains(normalize-space(.), 'First')]/following::input[1]")
    };

    private final By[] lastNameLocators = new By[]{
            By.cssSelector("input[id$='txtLastName']"),
            By.cssSelector("input[name$='txtLastName']"),
            By.cssSelector("input[placeholder*='Last']"),
            By.xpath("//label[contains(normalize-space(.), 'Last')]/following::input[1]")
    };

    private final By[] emailLocators = new By[]{
            By.cssSelector("input[id$='txtEmail']"),
            By.cssSelector("input[name$='txtEmail']"),
            By.cssSelector("input[type='email']"),
            By.cssSelector("input[placeholder*='Email']"),
            By.cssSelector("input[placeholder*='Email Id']"),
            By.xpath("//label[contains(normalize-space(.), 'Email')]/following::input[1]")
    };

    private final By[] phoneLocators = new By[]{
            By.cssSelector("input[id$='txtPhone']"),
            By.cssSelector("input[name$='txtPhone']"),
            By.cssSelector("input[type='tel']"),
            By.cssSelector("input[placeholder*='Phone']"),
            By.cssSelector("input[placeholder*='Whatsapp']"),
            By.cssSelector("input[placeholder*='WhatsApp']"),
            By.xpath("//label[contains(normalize-space(.), 'Phone')]/following::input[1]")
    };

    private final By[] companyLocators = new By[]{
            By.cssSelector("input[id$='txtCompany']"),
            By.cssSelector("input[name$='txtCompany']"),
            By.cssSelector("input[placeholder*='Company']"),
            By.xpath("//label[contains(normalize-space(.), 'Company')]/following::input[1]")
    };

    private final By[] passwordLocators = new By[]{
            By.cssSelector("input[id$='txtPassword']"),
            By.cssSelector("input[name$='txtPassword']"),
            By.cssSelector("input[type='password']"),
            By.cssSelector("input[placeholder*='Password']:not([placeholder*='Confirm'])"),
            By.xpath("//label[contains(normalize-space(.), 'Password')]/following::input[1]")
    };

    private final By[] confirmPasswordLocators = new By[]{
            By.cssSelector("input[id$='txtConfirmPassword']"),
            By.cssSelector("input[name$='txtConfirmPassword']"),
            By.cssSelector("input[placeholder*='Confirm']"),
            By.xpath("(//input[@type='password'])[2]")
    };

    private final By[] calleyTeamsPlanLocators = new By[]{
            By.xpath("//input[contains(@value,'Calley Teams')]"),
            By.xpath("//label[contains(normalize-space(.), 'Calley Teams')]/preceding::input[1]"),
            By.xpath("//label[contains(normalize-space(.), 'Calley Teams')]/following::input[1]"),
            By.cssSelector("input[type='radio'][value*='Calley']"),
            By.cssSelector("input[type='checkbox'][value*='Calley']")
    };

    private final By[] registerButtonLocators = new By[]{
            By.cssSelector("input[id$='btnRegister']"),
            By.cssSelector("button[id$='btnRegister']"),
            By.cssSelector("input[type='submit'][value*='Register']"),
            By.cssSelector("input[type='button'][value*='Register']"),
            By.xpath("//button[contains(normalize-space(.), 'Register') or contains(normalize-space(.), 'Sign Up')]")
    };

    private final By successMessageLocator = By.xpath(
            "//div[contains(@class,'success-message')] | //div[contains(@class,'success')] | //span[contains(@class,'success')]"
    );
    private final By recaptchaResponseLocator = By.cssSelector("textarea#g-recaptcha-response");
    private final By recaptchaFrameLocator = By.cssSelector("iframe[src*='recaptcha'], iframe[title*='reCAPTCHA']");

    // Constructor
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(300));
    }

    // Actions
    public void navigateToRegistrationPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.urlContains("registration"));
    }

    public void enterFirstName(String firstName) {
        WebElement field = waitForVisible(firstNameLocators);
        field.clear();
        field.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        WebElement field = findOptional(lastNameLocators);
        if (field != null) {
            field.clear();
            field.sendKeys(lastName);
            return;
        }
        appendLastNameToNameField(lastName);
    }

    public void enterEmail(String email) {
        WebElement field = waitForVisible(emailLocators);
        field.clear();
        field.sendKeys(email);
    }

    public void enterPhone(String phone) {
        WebElement field = findOptional(phoneLocators);
        if (field != null) {
            field.clear();
            field.sendKeys(phone);
        }
    }

    public void enterCompany(String company) {
        WebElement field = findOptional(companyLocators);
        if (field != null) {
            field.clear();
            field.sendKeys(company);
        }
    }

    public void enterPassword(String password) {
        WebElement field = waitForVisible(passwordLocators);
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement field = findOptional(confirmPasswordLocators);
        if (field != null) {
            field.clear();
            field.sendKeys(confirmPassword);
        }
    }

    public void selectCalleyTeamsPlan() {
        WebElement plan = findOptionalClickable(calleyTeamsPlanLocators);
        if (plan == null) {
            System.out.println("[Registration] Calley Teams plan not found, skipping selection.");
            return;
        }
        if (!isPlanElement(plan)) {
            System.out.println("[Registration] Calley Teams plan not selectable, skipping.");
            return;
        }
        try {
            scrollIntoView(plan);
            if ("input".equalsIgnoreCase(plan.getTagName())) {
                if (!plan.isSelected()) {
                    plan.click();
                }
            } else {
                plan.click();
            }
        } catch (ElementClickInterceptedException e) {
            System.out.println("[Registration] Plan selection intercepted. Skipping.");
        }
    }

    public boolean isCalleyTeamsPlanAvailable() {
        return findOptionalClickable(calleyTeamsPlanLocators) != null;
    }

    public void clickRegisterButton() {
        waitForRecaptchaIfPresent();
        try {
            WebElement button = waitForClickable(longWait, registerButtonLocators);
            scrollIntoView(button);
            button.click();
            return;
        } catch (TimeoutException ignored) {
        }

        WebElement fallback = findInAnyFrame(registerButtonLocators);
        if (fallback != null) {
            scrollIntoView(fallback);
            if (fallback.isEnabled()) {
                fallback.click();
                return;
            }
        }

        System.out.println("[Registration] Register button not clickable. Click it manually.");
        waitForManualSubmit();
    }

    public boolean isRegistrationSuccessful() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successMessageLocator),
                    ExpectedConditions.urlContains("dashboard"),
                    ExpectedConditions.urlContains("login")));
            return true;
        } catch (Exception e) {
            return false;
        }
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

    private WebElement findOptional(By... locators) {
        try {
            return shortWait.until(driver -> {
                for (By locator : locators) {
                    WebElement element = findVisibleInAnyFrame(locator);
                    if (element != null) {
                        return element;
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

    private WebElement findOptionalClickable(By... locators) {
        try {
            return shortWait.until(driver -> {
                for (By locator : locators) {
                    WebElement element = findVisibleInAnyFrame(locator);
                    if (element != null && element.isEnabled()) {
                        return element;
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
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

    private WebElement findInAnyFrame(By... locators) {
        driver.switchTo().defaultContent();
        for (By locator : locators) {
            WebElement element = findInCurrentContext(locator);
            if (element != null) {
                return element;
            }
        }
        List<WebElement> frames = driver.findElements(By.cssSelector("iframe,frame"));
        for (WebElement frame : frames) {
            try {
                driver.switchTo().defaultContent();
                driver.switchTo().frame(frame);
                for (By locator : locators) {
                    WebElement element = findInCurrentContext(locator);
                    if (element != null) {
                        return element;
                    }
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

    private WebElement findInCurrentContext(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) {
                return elements.get(0);
            }
        } catch (StaleElementReferenceException ignored) {
            return null;
        }
        return null;
    }

    private void waitForRecaptchaIfPresent() {
        boolean captchaPresent = !driver.findElements(recaptchaFrameLocator).isEmpty()
                || !driver.findElements(recaptchaResponseLocator).isEmpty();
        if (!captchaPresent) {
            return;
        }
        System.out.println("[Registration] reCAPTCHA detected. Please solve it in the browser.");
        longWait.until(driver -> {
            try {
                WebElement response = driver.findElement(recaptchaResponseLocator);
                String value = response.getAttribute("value");
                return value != null && !value.trim().isEmpty();
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void waitForManualSubmit() {
        longWait.until(driver -> {
            try {
                return ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(successMessageLocator),
                        ExpectedConditions.urlContains("dashboard"),
                        ExpectedConditions.urlContains("login")
                ).apply(driver);
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception ignored) {
        }
    }

    private boolean isPlanElement(WebElement element) {
        String tag = element.getTagName();
        if ("input".equalsIgnoreCase(tag)) {
            return true;
        }
        if ("label".equalsIgnoreCase(tag)) {
            return true;
        }
        return false;
    }


    public void registerUser(String firstName, String lastName, String email,
            String phone, String company, String password) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone(phone);
        enterCompany(company);
        enterPassword(password);
        enterConfirmPassword(password);
        selectCalleyTeamsPlan();
        clickRegisterButton();
    }

    private void appendLastNameToNameField(String lastName) {
        WebElement nameField = findOptional(
                By.cssSelector("input[placeholder*='Your Name']"),
                By.cssSelector("input[placeholder*='Name']"),
                By.xpath("//label[contains(normalize-space(.), 'Name')]/following::input[1]")
        );
        if (nameField == null) {
            return;
        }
        String existing = nameField.getAttribute("value");
        if (existing == null) {
            existing = "";
        }
        if (!existing.toLowerCase().contains(lastName.toLowerCase())) {
            nameField.sendKeys(existing.isEmpty() ? lastName : " " + lastName);
        }
    }
}