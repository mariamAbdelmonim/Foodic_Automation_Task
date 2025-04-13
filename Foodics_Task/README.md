# Amazon Egypt Automation Framework

## 📌 Overview
This framework automates test scenarios for Amazon Egypt using Selenium WebDriver with Java. It follows Page Object Model (POM) design pattern and includes all necessary components for robust test automation.

## 🛠️ Framework Structure

```
src/
├── main/
│   ├── java/
│   │   ├── base/              # Base classes
│   │   ├── pages/             # Page classes
│   │   └── utilities/         # Helper classes
│   └── resources/
└── test/
    ├── java/
    │   └── tests/             # Test classes
    └── resources/
        ├── config/            # Configuration files
        ├── drivers/           # WebDriver executables
        └── testdata/          # Test data files
```

## 🚀 Key Features
- **Page Object Model (POM) Design**
- **Multi-browser support** (Chrome, Firefox, Edge)
- **Advanced Element Handling**
- **Data-driven testing** (JSON test data)
- **Comprehensive Test Coverage**

## 📽️ Task Execution Demo
You can watch a full demo of the task execution in the video below:

🔗 [Click here to watch the demo](https://drive.google.com/file/d/1zzHSfnInsP2gzewP_XyIyKPGyARbkJak/view)


## ⚙️ Configuration
Edit `config.properties` to set:
```properties
base.url=https://www.amazon.eg/
browser=chrome
headless=false
implicit.wait=10
```

## 📂 Test Data
Edit `testdata.json` for test-specific data:
```json
{
  "user": {
    "valid": {
      "email": "testuser@example.com",
      "password": "Test@1234"
    }
  },
  "products": {
    "maxPrice": 15000
  }
}
```

## 🧪 Running Tests
Run tests using TestNG:
```bash
mvn clean test
```

Or through IDE using `testng.xml`
```

## 🔧 Dependencies
- Java 11+
- Maven
- Selenium WebDriver
- TestNG
- WebDriverManager

## 🛠️ Setup
1. Clone the repository
2. Install dependencies: `mvn clean install`
3. Add your test credentials in `testdata.json`
4. Run tests: `mvn test`

## 📋 Test Scenario Covered
1. Login to Amazon Egypt
2. Navigate to Video Games section
3. Apply filters (Free Shipping + New condition)
4. Sort by price (High to Low)
5. Add products under 15,000 EGP
6. Verify cart items
7. Proceed to checkout
8. Add shipping address
9. Select cash payment
10. Verify order total

