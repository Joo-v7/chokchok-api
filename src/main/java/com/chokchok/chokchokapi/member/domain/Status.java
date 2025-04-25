package com.chokchok.chokchokapi.member.domain;

import com.chokchok.chokchokapi.common.exception.base.InvalidException;
import com.chokchok.chokchokapi.common.exception.code.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    DELETED("삭제");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Status fromDisplayName(String displayName) {
        for (Status status : Status.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        throw new InvalidException(ErrorCode.INVALID_MEMBER_STATUS_VALUE, "status 값이 올바르지 않습니다");
    }

    public boolean isActive() {
        return Status.ACTIVE.equals(this);
    }

    public boolean isInactive() {
        return Status.INACTIVE.equals(this);
    }

    public boolean isDeleted() {
        return Status.DELETED.equals(this);
    }

}
