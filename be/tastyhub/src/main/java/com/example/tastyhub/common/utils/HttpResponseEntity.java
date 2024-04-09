package com.example.tastyhub.common.utils;


import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.enums.ResponseMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class HttpResponseEntity {

    private final SetHttpHeaders setHttpHeaders;
    //1xx
//    public static final ResponseEntity<StatusResponse> CONTINUE =
//        ResponseEntity.status(ResponseMessages.CONTINUE.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.CONTINUE));
//    public static final ResponseEntity<StatusResponse> SWITCHING_PROTOCOLS =
//        ResponseEntity.status(ResponseMessages.SWITCHING_PROTOCOLS.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.SWITCHING_PROTOCOLS));

    // 2xx
    public static final ResponseEntity<StatusResponse> RESPONSE_OK =
        ResponseEntity.status(ResponseMessages.SUCCESS.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.SUCCESS));
    public static final ResponseEntity<StatusResponse> RESPONSE_CREATED =
        ResponseEntity.status(ResponseMessages.CREATED_SUCCESS.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.CREATED_SUCCESS));
    public static final ResponseEntity<StatusResponse> DELETE_SUCCESS =
        ResponseEntity.status(ResponseMessages.DELETE_SUCCESS.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.DELETE_SUCCESS));
//
//    //3xx
//    public static final ResponseEntity<StatusResponse> MOVED_PERMANENTLY =
//        ResponseEntity.status(ResponseMessages.MOVED_PERMANENTLY.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.MOVED_PERMANENTLY));
//    public static final ResponseEntity<StatusResponse> FOUND =
//        ResponseEntity.status(ResponseMessages.FOUND.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.FOUND));
//    public static final ResponseEntity<StatusResponse> SEE_OTHER =
//        ResponseEntity.status(ResponseMessages.SEE_OTHER.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.SEE_OTHER));
//    public static final ResponseEntity<StatusResponse> NOT_MODIFIED =
//        ResponseEntity.status(ResponseMessages.NOT_MODIFIED.getStatusCode())
//            .body(StatusResponse.valueOf(ResponseMessages.NOT_MODIFIED));

    //4xx
    public static final ResponseEntity<StatusResponse> BAD_REQUEST =
        ResponseEntity.status(ResponseMessages.BAD_REQUEST.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.BAD_REQUEST));
    public static final ResponseEntity<StatusResponse> UNAUTHORIZED =
        ResponseEntity.status(ResponseMessages.UNAUTHORIZED.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.UNAUTHORIZED));
    public static final ResponseEntity<StatusResponse> FORBIDDEN =
        ResponseEntity.status(ResponseMessages.FORBIDDEN.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.FORBIDDEN));
    public static final ResponseEntity<StatusResponse> NOT_FOUND =
        ResponseEntity.status(ResponseMessages.NOT_FOUND.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.NOT_FOUND));
    public static final ResponseEntity<StatusResponse> METHOD_NOT_ALLOWED =
        ResponseEntity.status(ResponseMessages.METHOD_NOT_ALLOWED.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.METHOD_NOT_ALLOWED));
    public static final ResponseEntity<StatusResponse> CONFLICT =
        ResponseEntity.status(ResponseMessages.CONFLICT.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.CONFLICT));

    //5xx
    public static final ResponseEntity<StatusResponse> INTERNAL_SERVER_ERROR =
        ResponseEntity.status(ResponseMessages.INTERNAL_SERVER_ERROR.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.INTERNAL_SERVER_ERROR));
    public static final ResponseEntity<StatusResponse> NOT_IMPLEMENTED =
        ResponseEntity.status(ResponseMessages.NOT_IMPLEMENTED.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.NOT_IMPLEMENTED));
    public static final ResponseEntity<StatusResponse> SERVICE_UNAVAILABLE =
        ResponseEntity.status(ResponseMessages.SERVICE_UNAVAILABLE.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.SERVICE_UNAVAILABLE));
    public static final ResponseEntity<StatusResponse> GATEWAY_TIMEOUT =
        ResponseEntity.status(ResponseMessages.GATEWAY_TIMEOUT.getStatusCode())
            .body(StatusResponse.valueOf(ResponseMessages.GATEWAY_TIMEOUT));

}
