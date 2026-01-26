package main.java.pompages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AgentPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//a[contains(text(),'Agents')] | //span[contains(text(),'Agents')]")
    private WebElement agentsMenu;

    @FindBy(xpath = "//button[contains(text(),'Add Agent')] | //a[contains(text(),'Add Agent')]")
    private WebElement addAgentButton;

    @FindBy(id = "txtAgentName")
    private WebElement agentNameField;

    @FindBy(id = "txtAgentEmail")
    private WebElement agentEmailField;

    @FindBy(id = "txtAgentPhone")
    private WebElement agentPhoneField;

    @FindBy(id = "txtAgentExtension")
    private WebElement agentExtensionField;

    @FindBy(xpath = "//button[contains(text(),'Save')] | //input[@value='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//div[contains(@class,'success')] | //span[contains(text(),'successfully')]")
    private WebElement successMessage;

    // Constructor
    public AgentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Actions
    public void navigateToAgentsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(agentsMenu));
        agentsMenu.click();
    }

    public void clickAddAgentButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addAgentButton));
        addAgentButton.click();
    }

    public void enterAgentName(String name) {
        wait.until(ExpectedConditions.visibilityOf(agentNameField));
        agentNameField.clear();
        agentNameField.sendKeys(name);
    }

    public void enterAgentEmail(String email) {
        agentEmailField.clear();
        agentEmailField.sendKeys(email);
    }

    public void enterAgentPhone(String phone) {
        agentPhoneField.clear();
        agentPhoneField.sendKeys(phone);
    }

    public void enterAgentExtension(String extension) {
        agentExtensionField.clear();
        agentExtensionField.sendKeys(extension);
    }

    public void clickSaveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButton.click();
    }

    public boolean isAgentAddedSuccessfully() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void addAgent(String name, String email, String phone, String extension) {
        clickAddAgentButton();
        enterAgentName(name);
        enterAgentEmail(email);
        enterAgentPhone(phone);
        enterAgentExtension(extension);
        clickSaveButton();
    }
}