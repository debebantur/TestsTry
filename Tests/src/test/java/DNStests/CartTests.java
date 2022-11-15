package DNStests;

import jdk.jfr.Description;
import jdk.jfr.Name;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
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
    }

    @After
    @Description("Закрыть сайт")
    public void ClearTestData(){
        driver.close();
    }

    @Test
    @Name("Проверка успешного добавления товара в корзину")
    public void AddGoodsToTheCart(){
        //Открыть сайт ДНС
        driver.get("https://www.dns-shop.ru/");

        //Ввести поисковой запрос на сайте
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='q'])[2]")));
        driver.findElement(By.xpath("(//input[@name='q'])[2]")).sendKeys("клавиатура");

        //Нажать на поиск
        driver.findElement(By.cssSelector(".ui-input-search__buttons:nth-child(3) > .ui-input-search__icon_search")).click();

        //Подождать, пока загрузятся товары и добавить один из них в корзину
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")));
        driver.findElement(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")).click();

        //Подождать, пока добавление обработается и проверить, что товар добавлен в корзину
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge")));
        var elemeent = driver.findElement(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge"));
        Assert.assertEquals("Wrong count of goods", "1", elemeent.getText());
    }

    @Test
    @Name("Проверка успешного удаления товара из корзины")
    public void RemoveGoodsFromTheCart(){
        //Открыть сайт ДНС
        driver.get("https://www.dns-shop.ru/");

        //Предусловие: добавить в корзину товар
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@name='q'])[2]")));
        driver.findElement(By.xpath("(//input[@name='q'])[2]")).sendKeys("клавиатура");

        driver.findElement(By.cssSelector(".ui-input-search__buttons:nth-child(3) > .ui-input-search__icon_search")).click();

        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")));
        driver.findElement(By.cssSelector(".catalog-product:nth-child(2) .buy-btn")).click();

        //Запомнить кол-во товаров в корзине
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge")));
        var badgeElementText = driver.findElement(By.cssSelector(".ui-link:nth-child(3) > .cart-link__badge")).getText();

        //Шаги выполнения
        //Перейти в корзину
        driver.get("https://www.dns-shop.ru/cart/");

        // В зависимости от кол-ва товаров в корзине нажать на удаление одного/всех товаров
        if(badgeElementText.equals("1")){
            waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".count-buttons__icon-minus")));
            driver.findElement(By.cssSelector(".count-buttons__icon-minus")).click();
        }
        else{
            waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mass-selection__delete-btn")));
            driver.findElement(By.cssSelector(".mass-selection__delete-btn")).click();
        }

        //Проверить, что корзина пуста
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".empty-message__title-empty-cart")));
        var elemeent = driver.findElement(By.cssSelector(".empty-message__title-empty-cart"));
        Assert.assertTrue("Wrong count of goods", elemeent.getText().contains("пуста"));

    }
}
