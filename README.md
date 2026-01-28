# Calley Teams Automation Testing Project

## Project Overview
This project automates the essential functionalities of the Calley Teams web application using Selenium WebDriver, TestNG, and Java with Page Object Model (POM) design pattern.

## Technologies Used
- **Java 11+**
- **Maven** - Build and dependency management
- **Selenium WebDriver 4.15.0** - Browser automation
- **TestNG 7.8.0** - Testing framework
- **WebDriverManager 5.6.2** - Automatic driver management
- **Apache Commons CSV** - CSV file handling
- **ExtentReports 5.1.1** - Test reporting

## Project Structure
```
CalleyTeamsFullSetup/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── BaseClass.java
│   │   │   └── pompages/
│   │   │       ├── RegistrationPage.java
│   │   │       ├── LoginPage.java
│   │   │       ├── AgentPage.java
│   │   │       ├── CSVUploadPage.java
│   │   │       └── DashboardPage.java
│   │   └── resources/
│   │       ├── data.properties
│   │       └── SampleFile.csv
│   └── test/
│       └── java/
│           ├── RegistrationTest.java
│           └── FullSetupTest.java
├── pom.xml
└── testng.xml
```

## Prerequisites
1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Apache Maven**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **IDE (Choose one)**
   - IntelliJ IDEA Community Edition (Recommended)
   - Eclipse IDE for Java Developers

4. **Google Chrome Browser** (latest version)

## Setup Instructions

### Step 1: Create Maven Project
1. Open your IDE (IntelliJ IDEA or Eclipse)
2. Create a new Maven project
3. Set Group ID: `com.calleyteams`
4. Set Artifact ID: `CalleyTeamsFullSetup`

### Step 2: Configure Project Files

#### 1. Copy the `pom.xml` content
- Replace your project's pom.xml with the provided pom.xml content

#### 2. Create package structure
```
src/main/java/main/java/
src/main/java/main/java/pompages/
src/main/resources/
src/test/java/test/java/
```

#### 3. Add all Java files to their respective packages
- BaseClass.java → src/main/java/main/java/
- RegistrationPage.java → src/main/java/main/java/pompages/
- LoginPage.java → src/main/java/main/java/pompages/
- AgentPage.java → src/main/java/main/java/pompages/
- CSVUploadPage.java → src/main/java/main/java/pompages/
- DashboardPage.java → src/main/java/main/java/pompages/
- RegistrationTest.java → src/test/java/test/java/
- FullSetupTest.java → src/test/java/test/java/

#### 4. Add resource files
- data.properties → src/main/resources/
- SampleFile.csv → src/main/resources/
- testng.xml → Project root directory

### Step 3: Configure Test Data

Edit `src/main/resources/data.properties`:

```properties
# Update with your test data
user.email=your.email@domain.com
user.password=YourPassword123
user.firstname=YourFirstName
user.lastname=YourLastName
user.phone=1234567890
user.company=Your Company Name

# Agent details
agent.name=Agent Name
agent.email=agent@domain.com
agent.phone=9876543210
agent.extension=101
```

### Step 4: Install Dependencies
```bash
mvn clean install
```

### Step 5: Run Tests

#### Option 1: Run via Maven
```bash
mvn test
```

#### Option 2: Run via TestNG XML
- Right-click on `testng.xml`
- Select "Run as TestNG Suite"

#### Option 3: Run Individual Test Classes
- Right-click on test class (e.g., RegistrationTest.java)
- Select "Run as TestNG Test"

## Test Scenarios Covered

### 1. User Registration Test
- Navigates to registration page
- Fills registration form with test data
- Selects "Calley Teams" plan
- Validates successful registration

### 2. User Login Test
- Navigates to login page
- Enters valid credentials
- Validates successful login
- Verifies dashboard access

### 3. Add Agent Test
- Navigates to Agents section
- Adds new agent with details
- Validates agent creation success

### 4. CSV Upload Test
- Navigates to Call List → Power Import
- Enters list name
- Selects agent
- Uploads CSV file
- Maps CSV fields
- Validates successful import

### 5. Full Setup Test
- Executes complete workflow: Login → Add Agent → Upload CSV
- Validates each step
- Reports overall success

## Troubleshooting

### Common Issues and Solutions

#### 1. WebDriver Issues
**Problem:** Browser doesn't launch
**Solution:** 
```java
// WebDriverManager handles this automatically
// If issues persist, update Chrome browser to latest version
```

#### 2. Element Not Found
**Problem:** NoSuchElementException
**Solution:**
- Check if website structure has changed
- Update locators in respective Page Object classes
- Increase wait times in BaseClass

#### 3. CSV File Not Found
**Problem:** File not found error
**Solution:**
```properties
# Use absolute path in data.properties
csv.filepath=C:/Users/YourName/Projects/CalleyTeamsFullSetup/src/main/resources/SampleFile.csv
```

#### 4. Login Credentials Invalid
**Solution:**
- Ensure you've registered first using RegistrationTest
- Update credentials in data.properties
- Check if account is active

## GitHub Repository Structure
```
CalleyTeamsAutomation/
├── README.md
├── pom.xml
├── testng.xml
├── .gitignore
└── src/
    ├── main/
    └── test/
```

### .gitignore Content
```
target/
.idea/
*.iml
.classpath
.project
.settings/
```

## Best Practices Implemented

1. **Page Object Model (POM)** - Separate page classes for better maintainability
2. **Data-Driven Testing** - Externalized test data in properties file
3. **Explicit Waits** - Better synchronization handling
4. **WebDriverManager** - Automatic driver management
5. **TestNG Annotations** - Proper test lifecycle management
6. **Error Handling** - Try-catch blocks with meaningful messages
7. **Logging** - Console output for test execution tracking

---
**Note:** This is a sample automation project. Ensure you have proper authorization before running automated tests on production systems.
