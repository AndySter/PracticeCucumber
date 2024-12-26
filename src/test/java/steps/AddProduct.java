package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.bg.И;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AddProduct {

    static WebDriver driver;

    @Before
    public void openBrowser() throws MalformedURLException {
        driver = ConnectionJenkins.createDriver();
        //driver = new EdgeDriver();
        //System.setProperty("webdriver.msedgedriver.driver", "\\src\\test\\resources\\msedgedriver.exe");
        driver.manage().window().maximize();
    }

    @И("запущен файл qualit-sandbox.jar и открыта страница по адресу {string}")
    public void openWindow(String string) {
        driver.get(string);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @И("выполнено нажатие на {string}")
    public void clickSandbox(String string) {
        WebElement buttonSandbox = driver.findElement(By.xpath("//a[@id='navbarDropdown']"));
        assertEquals(string, buttonSandbox.getText());
        buttonSandbox.click();
    }

    @И("выполнено нажатие на кнопку {string} в выпадающем списке")
    public void clickProduct(String string) {
        WebElement buttonFood = driver.findElement(By.xpath("//a[@href='/food']"));
        assertEquals(string, buttonFood.getText());
        buttonFood.click();
    }

    @И("кнопка {string} появилась в окне")
    public void waitAddButton(String string) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement buttonAdd = driver.findElement(By.xpath("//button[@data-toggle='modal']"));
        assertEquals(string, buttonAdd.getText(), "Должна быть кнопка" +
                "с надписью" + string + "");
    }

    @И("выполнено нажатие на кнопку {string} для добавления нового товара")
    public void pressAddButton(String string) {
        WebElement buttonAdd = driver.findElement(By.xpath("//button[@data-toggle='modal']"));
        assertEquals(string, buttonAdd.getText());
        buttonAdd.click();
    }

    @И("окно {string} появилось на экране")
    public void waitAddedWindow(String string) throws InterruptedException {
        Thread.sleep(1000);
        WebElement headerWindow = driver.findElement(By.xpath("//h5[@id='editModalLabel']"));
        assertEquals(string, headerWindow.getText(), "Должна быть окно " +
                "с надписью" + string + "");
    }

    @И("ввести слово {string} в поле {string}")
    public void writeText(String string, String string_name) {
        WebElement textName = driver.findElement(By.xpath("//input[@id='name']"));
        textName.sendKeys(string);
        assertEquals(string, textName.getAttribute("value"), "Должно быть слово " + string + "");
    }

    @И("в выпадающем списке {string} выбрано {string}")
    public void chooseValue(String string, String string_type) {
        WebElement typeProduct;
        if (string_type.equals("Фрукт")) {
            typeProduct = driver.findElement(By.xpath("//option[@value='FRUIT']"));
        } else {
            typeProduct = driver.findElement(By.xpath("//option[@value='VEGETABLE']"));
        }
        typeProduct.click();
        assertEquals(string_type, typeProduct.getText(), "Должно быть слово " + string_type + "");
    }

    @И("^чекбокс \".*\" должен быть (включен|выключен)$")
    public void pressChechbox(String check) {
        WebElement checkboxExotic = driver.findElement(By.xpath("//input[@id='exotic']"));
        if (!check.equals("включен")) {
            if (checkboxExotic.isSelected()) {
                checkboxExotic.click();
            }
            assertEquals(false, checkboxExotic.isSelected(), "Чекбокс должен быть false");
        } else {
            if (!checkboxExotic.isSelected()) {
                checkboxExotic.click();
            }
            assertEquals(true, checkboxExotic.isSelected(), "Чекбокс должен быть true");
        }
    }

    @И("выполнено нажатие на кнопку {string} для добавления товара")
    public void pressSave(String string) {
        WebElement buttonSave = driver.findElement(By.xpath("//button[@id='save']"));
        assertEquals(string, buttonSave.getText());
        buttonSave.click();
    }

    @И("товар с наименованием {string} появился в списке товаров")
    public void addedElement(String string) {
        WebElement addedElementFirst = driver.findElement(By.xpath("//td[text()='" + string + "']"));
        assertTrue(addedElementFirst.isDisplayed());
        assertEquals(string, addedElementFirst.getText(), "Добавленный элемент должен называться " + string + "");
    }

    @И("выполнено нажатие на {string} для очистки списка")
    public void clearButton(String string) {
        WebElement buttonClear = driver.findElement(By.xpath("//a[@id='reset']"));
        assertEquals(string, buttonClear.getText());
        buttonClear.click();
    }

    @И("выполнена проверка на удаление {string},{string} из списка")
    public void findProduct(String string, String string1) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='" + string + "']")));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='" + string1 + "']")));
        } catch (Exception e) {
            fail("Элементы найдены, хотя должны быть удалены");
        }
    }

    @After
    public void exit() {
        driver.quit();
    }
}