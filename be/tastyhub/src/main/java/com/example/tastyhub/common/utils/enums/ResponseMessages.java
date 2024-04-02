package com.example.tastyhub.common.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseMessages {

    SUCCESS(200, "요청 성공"),
    CREATED_SUCCESS(201, "생성 성공"),
    DELETE_SUCCESS(204, "삭제 성공"),

    // 클라이언트 에러
    BAD_REQUEST(400, "잘못된 요청"),
    UNAUTHORIZED(401, "인증 실패"),
    FORBIDDEN(403, "권한 없음"),
    NOT_FOUND(404, "요청된 리소스를 찾을 수 없음"),
    METHOD_NOT_ALLOWED(405, "허용되지 않는 메서드"),
    CONFLICT(409, "충돌 발생"),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류"),
    NOT_IMPLEMENTED(501, "구현되지 않은 기능"),
    SERVICE_UNAVAILABLE(503, "서비스 이용 불가"),
    GATEWAY_TIMEOUT(504, "게이트웨이 타임아웃");

//    // 정보 제공
//    CONTINUE(100, "요청 계속"),
//    SWITCHING_PROTOCOLS(101, "프로토콜 변경"),
//
//    // 리다이렉션
//    MOVED_PERMANENTLY(301, "영구 리다이렉션"),
//    FOUND(302, "임시 리다이렉션"),
//    SEE_OTHER(303, "다른 리소스 참조"),
//    NOT_MODIFIED(304, "리소스 변경 없음");

    private final int statusCode;
    private final String message;
}
