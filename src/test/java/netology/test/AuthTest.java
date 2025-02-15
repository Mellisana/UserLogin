package netology.test;

import com.codeborne.selenide.Condition;
import netology.data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

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
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("h2").should(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }


    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! " + "Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] .input__control").setValue(wrongLogin);
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(wrongPassword);
        $("[data-test-id=action-login] .button__content").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
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