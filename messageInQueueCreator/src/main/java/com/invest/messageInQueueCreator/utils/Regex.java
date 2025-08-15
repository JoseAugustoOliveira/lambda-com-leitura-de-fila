package com.invest.messageInQueueCreator.utils;

public interface Regex {

    String POSTAL_CODE = "(\\d{8})";
    String ONLY_NUMBER = "\\d+";
    String STATE = "^[A-Z]{2}$";
    String CPF = "(\\d{11})?";
}
