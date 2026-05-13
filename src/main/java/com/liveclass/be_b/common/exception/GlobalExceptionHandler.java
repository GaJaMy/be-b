package com.liveclass.be_b.common.exception;

import com.liveclass.be_b.common.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 도메인 비즈니스 예외 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn(
                "Business exception occurred: code={}, message={}",
                e.getErrorCode().getCode(),
                e.getMessage(),
                e
        );
        return ApiResponse.fail(e.getErrorCode());
    }

    /** @Valid 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        var fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.INVALID_INPUT.getMessage());
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, message);
    }

    /** 바인딩 실패 */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException e) {
        var fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.INVALID_INPUT.getMessage());
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, message);
    }

    /** 컨트롤러에서 @RequestParam/@PathVariable 검증 실패 (Spring Boot 3.2+) */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidation(HandlerMethodValidationException e) {
        var validationResults = e.getParameterValidationResults();
        String message = validationResults.stream()
                .flatMap(result -> {
                    var resolvableErrors = result.getResolvableErrors();
                    return resolvableErrors.stream();
                })
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.INVALID_INPUT.getMessage());
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, message);
    }

    /** 서비스 레이어 검증 실패 */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException e) {
        var violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(v -> {
                    String path = v.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return field + ": " + v.getMessage();
                })
                .findFirst()
                .orElse(ErrorCode.INVALID_INPUT.getMessage());
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, message);
    }

    /** JSON 파싱 실패 (잘못된 타입, 필드명 오류 등) */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException && !invalidFormatException.getPath().isEmpty()) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();

            if ("paidAt".equals(fieldName)) {
                return ApiResponse.fail(ErrorCode.INVALID_INPUT, "결제 일시 형식은 yyyy-MM-dd'T'HH:mm:ssXXX 이어야 합니다.");
            }

            if ("canceledAt".equals(fieldName)) {
                return ApiResponse.fail(ErrorCode.INVALID_INPUT, "취소 일시 형식은 yyyy-MM-dd'T'HH:mm:ssXXX 이어야 합니다.");
            }
        }

        return ApiResponse.fail(ErrorCode.INVALID_INPUT, "요청 본문을 읽을 수 없습니다.");
    }

    /** 필수 @RequestParam 누락 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, e.getParameterName() + " 파라미터가 필요합니다.");
    }

    /** @RequestParam / @PathVariable 타입 변환 실패 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String parameterName = e.getName();

        if ("from".equals(parameterName) || "to".equals(parameterName)) {
            return ApiResponse.fail(ErrorCode.INVALID_INPUT, parameterName + " 형식은 yyyy-MM-dd 이어야 합니다.");
        }

        if ("yearMonth".equals(parameterName)) {
            return ApiResponse.fail(ErrorCode.INVALID_INPUT, "조회 연월 형식은 yyyy-MM 이어야 합니다.");
        }

        return ApiResponse.fail(ErrorCode.INVALID_INPUT);
    }

    /** 존재하지 않는 엔드포인트 */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(NoHandlerFoundException e) {
        return ApiResponse.fail(ErrorCode.NOT_FOUND_ENDPOINT);
    }

    /** 허용되지 않은 HTTP 메서드 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ApiResponse.fail(ErrorCode.METHOD_NOT_ALLOWED);
    }

    /** 그 외 모든 예외 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled exception occurred", e);
        return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
