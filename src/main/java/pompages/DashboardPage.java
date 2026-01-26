package main.java.pompages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//h1[contains(text(),'Dashboard')] | //span[contains(text(),'Dashboard')]")
    private WebElement dashboardHeader;

    @FindBy(xpath = "//a[contains(@class,'profile')] | //span[contains(@class,'user-name')]")
    private WebElement userProfile;

    @FindBy(xpath = "//a[contains(text(),'Logout')] | //button[contains(text(),'Logout')]")
    private WebElement logoutButton;

    // Constructor
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Actions
    public boolean isDashboardDisplayed() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOf(dashboardHeader),
                    ExpectedConditions.urlContains("dashboard")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDashboardTitle() {
        wait.until(ExpectedConditions.visibilityOf(dashboardHeader));
        return dashboardHeader.getText();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userProfile));
        userProfile.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
    }
}