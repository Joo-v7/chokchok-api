package com.chokchok.chokchokapi.member.exception;

import com.chokchok.chokchokapi.common.exception.base.InvalidEnumValueException;

public class InvalidGenderValueException extends InvalidEnumValueException {
    public InvalidGenderValueException(String message) {
        super(message);
    }
}
