package com.chokchok.chokchokapi.member.exception;

import com.chokchok.chokchokapi.common.exception.base.InvalidEnumValueException;

public class InvalidStatusValueException extends InvalidEnumValueException {
    public InvalidStatusValueException(String message) {
        super(message);
    }
}
