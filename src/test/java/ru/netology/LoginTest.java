package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.ApiHelper;
import ru.netology.UserData;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {
    private UserData activeUser;
    private UserData blockedUser;

    @BeforeAll
    static void setUpAll() {
        Configuration.browserSize = "1280x800";
    }

    @BeforeEach
    void setUp() {
        activeUser = ApiHelper.generateUser("active");
        blockedUser = ApiHelper.generateUser("blocked");

        ApiHelper.registerUser(activeUser);
        ApiHelper.registerUser(blockedUser);

        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithActiveUser() {
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.text("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        $("[data-test-id=login] input").setValue(ApiHelper.generateUser("active").getLogin());
        $("[data-test-id=password] input").setValue(activeUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        $("[data-test-id=login] input").setValue(activeUser.getLogin());
        $("[data-test-id=password] input").setValue(ApiHelper.generateUser("active").getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}