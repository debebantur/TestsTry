package DNStests;

import jdk.jfr.Description;
import jdk.jfr.Name;
import net.bytebuddy.build.Plugin;
import org.asynchttpclient.util.Assertions;
import org.checkerframework.framework.qual.PreconditionAnnotation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartTests {

    private ChromeDriver driver;
    private WebDriverWait waiter;

    @Before
    @Description("Открыть сайт")
    public void InitTestData(){
        driver = new ChromeDriver();
        waiter = new WebDriverWait(driver, Duration.ofSeconds(3));

        Dimension size = new Dimension(1936,1080);
        driver.manage().window().setSize(size);

        driver.get("https://www.dns-shop.ru/");
    }

    @After
    @Description("Закрыть сайт")
    public void ClearTestData(){
        driver.close();
    }

    @Test
    @Name("Проверка успешного добавления товара в корзину")
    public void AddGoodsToTheCart(){
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='q'])[2]")));
        //
        driver.findElement(By.xpath("(//input[@name='q'])[2]")).sendKeys("клавиатура");
        //
        driver.findElement(By.cssSelector(".ui-input-search__buttons:nth-child(3) > .ui-input-search__icon_search")).click();
        //
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")));
        driver.findElement(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")).click();

        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge")));
        var elemeent = driver.findElement(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge"));
        Assert.assertEquals("Wrong count of goods", "1", elemeent.getText());

    }
}
