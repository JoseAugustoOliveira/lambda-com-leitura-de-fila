package com.invest.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class OutputObject {

    private String result;
    private String requestId;

    public OutputObject setResult(String result) {
        this.result = result;
        return this;
    }

    public OutputObject setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
