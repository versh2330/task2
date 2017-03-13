

import java.util.*;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LoginTest {
    private WebDriver driver;
    private String url;
    private String name;
    private String pass;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Parameters({"name", "pass", "url"})
    @BeforeClass(alwaysRun = true)
    public void setUp(String name, String pass, String url) throws Exception {
        this.name = name;
        this.pass = pass;
        this.url = url;

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.notifications", 0);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);



        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.manage().window().maximize();
    }

    @Test(groups = "login")
    public void testLogin() throws Exception {
        driver.get(url);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(name);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("SubmitCreds")).click();
        if(isAlertPresent()){ closeAlertAndGetItsText();}


        assertTrue(isElementPresent(By.xpath("//*[@id=\"primaryContainer\"]/div[5]/div/div[1]/div/div[5]/div[2]/div[1]/div/div/div")));
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"primaryContainer\"]/div[5]/div/div[1]/div/div[5]/div[2]/div[1]/div/div/div")).click();
        assertTrue(isElementPresent(By.xpath("//*[@id=\"O365_TopMenu\"]/div/div/div[1]/div[14]/button/div/span/div")));
        Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(10, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
        WebElement icon = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver input) {
                return driver.findElement(By.className("_pe_h"));
            }
        });

        if(isAlertPresent()){ closeAlertAndGetItsText();}
        icon.click();
        WebElement logout = wait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver input) {
                return driver.findElement(By.xpath("//div[12]/div/div[2]/div[3]/div/div[3]/button"));
            }
        });
        assertNotNull(logout);
        logout.click();

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
