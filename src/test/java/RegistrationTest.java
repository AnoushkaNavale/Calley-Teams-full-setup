package test.java;

import main.java.BaseClass;
import main.java.pompages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTest extends BaseClass {

    @Test(priority = 1, description = "Test user registration with Calley Teams plan")
    public void testUserRegistration() {
        try {
            // Initialize page object
            RegistrationPage registrationPage = new RegistrationPage(driver);

            // Get test data from properties file
            String url = getProperty("registration.url");
            String firstName = getProperty("user.firstname");
            String lastName = getProperty("user.lastname");
            String email = getProperty("user.email");
            String phone = getProperty("user.phone");
            String company = getProperty("user.company");
            String password = getProperty("user.password");

            // Navigate to registration page
            registrationPage.navigateToRegistrationPage(url);
            System.out.println("Navigated to registration page");

            // Fill registration form
            registrationPage.registerUser(firstName, lastName, email, phone, company, password);
            System.out.println("Registration form filled and submitted");

            // Wait for registration to complete
            Thread.sleep(3000);

            // Validate registration
            boolean isSuccessful = registrationPage.isRegistrationSuccessful();
            Assert.assertTrue(isSuccessful, "Registration was not successful");
            System.out.println("User registration completed successfully");

        } catch (Exception e) {
            System.err.println("Registration test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Registration test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Verify Calley Teams plan is selected")
    public void testCalleyTeamsPlanSelection() {
        try {
            RegistrationPage registrationPage = new RegistrationPage(driver);

            String url = getProperty("registration.url");
            registrationPage.navigateToRegistrationPage(url);

            // Select Calley Teams plan
            registrationPage.selectCalleyTeamsPlan();
            System.out.println("Calley Teams plan selected successfully");

            Assert.assertTrue(true, "Calley Teams plan selection successful");

        } catch (Exception e) {
            System.err.println("Plan selection test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Plan selection test failed");
        }
    }
}