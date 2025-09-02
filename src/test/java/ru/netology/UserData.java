package ru.netology;

import lombok.Value;

@Value
public class UserData {
    String login;
    String password;
    String status;
}