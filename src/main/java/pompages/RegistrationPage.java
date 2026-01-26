package main.java.pompages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators using @FindBy
    @FindBy(id = "txtFirstName")
    private WebElement firstNameField;

    @FindBy(id = "txtLastName")
    private WebElement lastNameField;

    @FindBy(id = "txtEmail")
    private WebElement emailField;

    @FindBy(id = "txtPhone")
    private WebElement phoneField;

    @FindBy(id = "txtCompany")
    private WebElement companyField;

    @FindBy(id = "txtPassword")
    private WebElement passwordField;

    @FindBy(id = "txtConfirmPassword")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//input[@value='Calley Teams']")
    private WebElement calleyTeamsPlan;

    @FindBy(id = "btnRegister")
    private WebElement registerButton;

    @FindBy(xpath = "//div[contains(@class,'success-message')]")
    private WebElement successMessage;

    // Constructor
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Actions
    public void navigateToRegistrationPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.urlContains("registration"));
    }

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPhone(String phone) {
        phoneField.clear();
        phoneField.sendKeys(phone);
    }

    public void enterCompany(String company) {
        companyField.clear();
        companyField.sendKeys(company);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void selectCalleyTeamsPlan() {
        wait.until(ExpectedConditions.elementToBeClickable(calleyTeamsPlan));
        if (!calleyTeamsPlan.isSelected()) {
            calleyTeamsPlan.click();
        }
    }

    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }

    public boolean isRegistrationSuccessful() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(successMessage),
                    ExpectedConditions.urlContains("dashboard"),
                    ExpectedConditions.urlContains("login")));
            return true;
        } catch (Exception e) {
            return false;
        }
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