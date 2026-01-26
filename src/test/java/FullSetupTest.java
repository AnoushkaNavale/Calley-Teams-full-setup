package test.java;

import main.java.BaseClass;
import main.java.pompages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class FullSetupTest extends BaseClass {

    @Test(priority = 1, description = "Complete Calley Teams Setup - Login, Add Agent, Upload CSV")
    public void testFullSetup() {
        try {
            // Step 1: Login
            System.out.println("========== Step 1: User Login ==========");
            LoginPage loginPage = new LoginPage(driver);
            String loginUrl = getProperty("login.url");
            String email = getProperty("user.email");
            String password = getProperty("user.password");

            loginPage.navigateToLoginPage(loginUrl);
            loginPage.login(email, password);
            Thread.sleep(3000);

            boolean loginSuccess = loginPage.isLoginSuccessful();
            Assert.assertTrue(loginSuccess, "Login failed");
            System.out.println("✓ Login successful");

            // Step 2: Verify Dashboard
            System.out.println("\n========== Step 2: Verify Dashboard ==========");
            DashboardPage dashboardPage = new DashboardPage(driver);
            boolean dashboardDisplayed = dashboardPage.isDashboardDisplayed();
            Assert.assertTrue(dashboardDisplayed, "Dashboard not displayed");
            System.out.println("✓ Dashboard verified");

            // Step 3: Add Agent
            System.out.println("\n========== Step 3: Add Agent ==========");
            AgentPage agentPage = new AgentPage(driver);
            String agentName = getProperty("agent.name");
            String agentEmail = getProperty("agent.email");
            String agentPhone = getProperty("agent.phone");
            String agentExtension = getProperty("agent.extension");

            agentPage.navigateToAgentsPage();
            Thread.sleep(2000);
            agentPage.addAgent(agentName, agentEmail, agentPhone, agentExtension);
            Thread.sleep(2000);

            boolean agentAdded = agentPage.isAgentAddedSuccessfully();
            Assert.assertTrue(agentAdded, "Agent addition failed");
            System.out.println("✓ Agent added successfully");

            // Step 4: Upload CSV
            System.out.println("\n========== Step 4: Upload CSV ==========");
            CSVUploadPage csvUploadPage = new CSVUploadPage(driver);
            String listName = getProperty("csv.listname");
            String csvFilePath = getProperty("csv.filepath");

            // Verify file exists
            File csvFile = new File(csvFilePath);
            Assert.assertTrue(csvFile.exists(), "CSV file not found at: " + csvFilePath);

            csvUploadPage.uploadCSV(listName, agentName, csvFile.getAbsolutePath());
            Thread.sleep(3000);

            // Map fields
            csvUploadPage.mapFields("FirstName", "LastName", "Phone", "Email");
            Thread.sleep(2000);

            csvUploadPage.clickImportButton();
            Thread.sleep(5000);

            boolean importSuccess = csvUploadPage.isImportSuccessful();
            Assert.assertTrue(importSuccess, "CSV import failed");
            System.out.println("✓ CSV uploaded and imported successfully");

            System.out.println("\n========== Full Setup Completed Successfully ==========");

        } catch (Exception e) {
            System.err.println("Full setup test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Full setup test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Test Login functionality separately")
    public void testLogin() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            String loginUrl = getProperty("login.url");
            String email = getProperty("user.email");
            String password = getProperty("user.password");

            loginPage.navigateToLoginPage(loginUrl);
            loginPage.login(email, password);
            Thread.sleep(3000);

            boolean loginSuccess = loginPage.isLoginSuccessful();
            Assert.assertTrue(loginSuccess, "Login verification failed");
            System.out.println("Login test passed");

        } catch (Exception e) {
            System.err.println("Login test failed: " + e.getMessage());
            Assert.fail("Login test failed");
        }
    }
}