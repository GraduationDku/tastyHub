package com.example.tastyhub.common.dto;


import com.example.tastyhub.common.utils.enums.ResponseMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {

    private int statusCode;
    private String message;

    public static StatusResponse valueOf(ResponseMessages responseMessages) {
        return new StatusResponse(responseMessages.getStatusCode(), responseMessages.getMessage());
    }
}