package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.RegistrationByCardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class CardFormTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }


    @Test
    void shouldRegisterCard() {
        RegistrationByCardInfo firstAttempt = DataGenerator.Registration.generateByCard("ru");
        String firstDate = DataGenerator.generateDate(5);
        String secondDate = DataGenerator.generateDate(7);

        $("[data-test-id='city'] input").setValue(firstAttempt.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstDate);
        $("[data-test-id='name'] input").setValue(firstAttempt.getName());
        $("[data-test-id='phone'] input").setValue(firstAttempt.getPhone());
        $("[data-test-id='agreement'] .checkbox__text").click();
        $$(".button__text").find(Condition.text("Запланировать")).click();
        $(".notification__content").shouldBe(Condition.visible).shouldHave(exactText("Встреча успешно запланирована на " + firstDate), Duration.ofSeconds(15));
        $(".notification__content").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $$(".button__text").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(Condition.visible).shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$(".button__text").find(exactText("Перепланировать")).click();
        $(".notification__content").shouldBe(Condition.visible).shouldHave(exactText("Встреча успешно запланирована на " + secondDate), Duration.ofSeconds(15));
    }
}