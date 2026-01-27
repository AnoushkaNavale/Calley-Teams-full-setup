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

public class RegistrationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By[] firstNameLocators = new By[]{
            By.cssSelector("input[id$='txtFirstName']"),
            By.cssSelector("input[name$='txtFirstName']"),
            By.cssSelector("input[placeholder*='First']"),
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
            By.xpath("//label[contains(normalize-space(.), 'Email')]/following::input[1]")
    };

    private final By[] phoneLocators = new By[]{
            By.cssSelector("input[id$='txtPhone']"),
            By.cssSelector("input[name$='txtPhone']"),
            By.cssSelector("input[type='tel']"),
            By.cssSelector("input[placeholder*='Phone']"),
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
            By.xpath("//*[contains(normalize-space(.), 'Calley Teams') and (self::label or self::div or self::span)]")
    };

    private final By[] registerButtonLocators = new By[]{
            By.cssSelector("input[id$='btnRegister']"),
            By.cssSelector("button[id$='btnRegister']"),
            By.cssSelector("input[type='submit'][value*='Register']"),
            By.xpath("//button[contains(normalize-space(.), 'Register') or contains(normalize-space(.), 'Sign Up')]")
    };

    private final By successMessageLocator = By.xpath(
            "//div[contains(@class,'success-message')] | //div[contains(@class,'success')] | //span[contains(@class,'success')]"
    );

    // Constructor
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
        WebElement field = waitForVisible(lastNameLocators);
        field.clear();
        field.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        WebElement field = waitForVisible(emailLocators);
        field.clear();
        field.sendKeys(email);
    }

    public void enterPhone(String phone) {
        WebElement field = waitForVisible(phoneLocators);
        field.clear();
        field.sendKeys(phone);
    }

    public void enterCompany(String company) {
        WebElement field = waitForVisible(companyLocators);
        field.clear();
        field.sendKeys(company);
    }

    public void enterPassword(String password) {
        WebElement field = waitForVisible(passwordLocators);
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement field = waitForVisible(confirmPasswordLocators);
        field.clear();
        field.sendKeys(confirmPassword);
    }

    public void selectCalleyTeamsPlan() {
        WebElement plan = waitForClickable(calleyTeamsPlanLocators);
        if ("input".equalsIgnoreCase(plan.getTagName())) {
            if (!plan.isSelected()) {
                plan.click();
            }
        } else {
            plan.click();
        }
    }

    public void clickRegisterButton() {
        WebElement button = waitForClickable(registerButtonLocators);
        button.click();
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
}