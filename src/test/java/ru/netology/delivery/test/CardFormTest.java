package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardFormTest {
    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }


    @Test
    public void shouldRegByAcc() {
        $("[data-test-id='city'] input").val(DataGenerator.getCity());
        String date1 = DataGenerator.getDate();
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(date1);
        $("[data-test-id='name'] input").val(DataGenerator.getName());
        $("[data-test-id='phone'] input").val(DataGenerator.getPhone());
        $("span.checkbox__box").click();
        $(withText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id='success-notification']").shouldBe(visible).shouldHave(Condition.text("Встреча успешно запланирована на " + date1));
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date2 = DataGenerator.getDate();
        $("[data-test-id='date'] input").setValue(date2);
        $(withText("Запланировать")).click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id=replan-notification] button.button").click();
        $(withText("Успешно")).shouldBe(visible);

    }
}
