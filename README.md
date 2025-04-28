## console-ui-selenium
This repository gathers Legion selenium/testng test framework and console-ui Selenium tests.
Tests will be added progressively.

### Environment Setup

1. Global Dependencies
    * [Install Maven](https://maven.apache.org/install.html)
    * Or Install Maven with [Homebrew](http://brew.sh/)
    ```
    $ brew install maven
    ```
2. In the folder "src/test/resources", adapt the files envCfg.json and browsersCfg.json using your editor of choice (an IDE or any simple editor)
    * Adapt the file envCfg.json: 
    ```
    {
      "BASE_URL":"your desktop homepage url"
    }
    ```
    * Add the browsers you want to test under browsersCfg.json: 
     ```
     [
        ["browser","version","environment","platform"] ,
        ["browser","version","environment","platform"] ,
        ["browser","version","environment","platform"]    
     ]
     ```
     
     Examples
     ```
     [
       ["firefox","49.0","Windows 10","DesktopWeb"],
     ]
      ```
3. Project Dependencies
	* Check that Packages are available
	```
	$ cd console-ui-selenium
	$ mvn test-compile
	```
	* check and validate dependency
	```
	$ mvn versions:display-dependency-updates
	```
### Running Tests

Tests in Parallel:
	```
	$ mvn test
	```

##### Stack Overflow:
* [Related Stack Overflow Threads](http://stackoverflow.com/questions/27355003/advise-on-hierarchy-for-element-locators-in-selenium-webdriver)
