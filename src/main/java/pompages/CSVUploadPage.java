package main.java.pompages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CSVUploadPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//a[contains(text(),'Call List')] | //span[contains(text(),'Call List')]")
    private WebElement callListMenu;

    @FindBy(xpath = "//a[contains(text(),'Power Import')] | //span[contains(text(),'Power Import')]")
    private WebElement powerImportMenu;

    @FindBy(id = "txtListName")
    private WebElement listNameField;

    @FindBy(id = "ddlAgent")
    private WebElement agentDropdown;

    @FindBy(id = "fileUpload")
    private WebElement fileUploadInput;

    @FindBy(xpath = "//button[contains(text(),'Upload')] | //input[@value='Upload']")
    private WebElement uploadButton;

    @FindBy(id = "ddlFirstName")
    private WebElement firstNameMapping;

    @FindBy(id = "ddlLastName")
    private WebElement lastNameMapping;

    @FindBy(id = "ddlPhone")
    private WebElement phoneMapping;

    @FindBy(id = "ddlEmail")
    private WebElement emailMapping;

    @FindBy(xpath = "//button[contains(text(),'Import')] | //input[@value='Import']")
    private WebElement importButton;

    @FindBy(xpath = "//div[contains(@class,'success')] | //span[contains(text(),'imported successfully')]")
    private WebElement successMessage;

    // Constructor
    public CSVUploadPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // Actions
    public void navigateToCallList() {
        wait.until(ExpectedConditions.elementToBeClickable(callListMenu));
        callListMenu.click();
    }

    public void navigateToPowerImport() {
        wait.until(ExpectedConditions.elementToBeClickable(powerImportMenu));
        powerImportMenu.click();
    }

    public void enterListName(String listName) {
        wait.until(ExpectedConditions.visibilityOf(listNameField));
        listNameField.clear();
        listNameField.sendKeys(listName);
    }

    public void selectAgent(String agentName) {
        wait.until(ExpectedConditions.visibilityOf(agentDropdown));
        Select select = new Select(agentDropdown);
        select.selectByVisibleText(agentName);
    }

    public void uploadFile(String filePath) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                org.openqa.selenium.By.id("fileUpload")));
        fileUploadInput.sendKeys(filePath);
    }

    public void clickUploadButton() {
        wait.until(ExpectedConditions.elementToBeClickable(uploadButton));
        uploadButton.click();
    }

    public void mapFields(String firstName, String lastName, String phone, String email) {
        wait.until(ExpectedConditions.visibilityOf(firstNameMapping));

        Select fnSelect = new Select(firstNameMapping);
        fnSelect.selectByVisibleText(firstName);

        Select lnSelect = new Select(lastNameMapping);
        lnSelect.selectByVisibleText(lastName);

        Select phoneSelect = new Select(phoneMapping);
        phoneSelect.selectByVisibleText(phone);

        Select emailSelect = new Select(emailMapping);
        emailSelect.selectByVisibleText(email);
    }

    public void clickImportButton() {
        wait.until(ExpectedConditions.elementToBeClickable(importButton));
        importButton.click();
    }

    public boolean isImportSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void uploadCSV(String listName, String agentName, String filePath) {
        navigateToCallList();
        navigateToPowerImport();
        enterListName(listName);
        selectAgent(agentName);
        uploadFile(filePath);
        clickUploadButton();
    }
}