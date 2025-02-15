package netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("#login").setValue(registeredUser.getLogin());
        $("#password").setValue(registeredUser.getPassword());
        $(".button").click();
        $(byText("Личный кабинет")).shouldHave(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("#login").setValue(notRegisteredUser.getLogin());
        $("#password").setValue(notRegisteredUser.getPassword());
        $(".button").click();
        $("#error-notification .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("#login").setValue(blockedUser.getLogin());
        $("#password").setValue(blockedUser.getPassword());
        $(".button").click();
        $("#error-notification .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("#login").setValue(wrongLogin);
        $("#password").setValue(registeredUser.getPassword());
        $(".button").click();
        $("#error-notification .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("#login").setValue(registeredUser.getLogin());
        $("#password").setValue(wrongPassword);
        $(".button").click();
        $("#error-notification .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    private static DataGenerator.RegistrationDto getRegisteredUser(String status) {
        return DataGenerator.Registration.getRegisteredUser(status);
    }

    private static String getRandomLogin() {
        return DataGenerator.getRandomLogin();
    }

    private static String getRandomPassword() {
        return DataGenerator.getRandomPassword();
    }

    private static DataGenerator.RegistrationDto getUser(String status) {
        return DataGenerator.Registration.getUser(status);
    }
}