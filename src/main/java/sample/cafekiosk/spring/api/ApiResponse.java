package sample.cafekiosk.spring.api;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    @Builder
    private ApiResponse(int code, HttpStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private int code;

    private HttpStatus status;

    private String message;

    private T data;

    public static <T> ApiResponse<T> of(HttpStatus httpStatus,String message, T data) {
        return ApiResponse.<T>builder()
                .code(httpStatus.value())
                .status(httpStatus)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus,T data) {
        return ApiResponse.<T>builder()
                .code(httpStatus.value())
                .status(httpStatus)
                .message(httpStatus.name())
                .data(data)
                .build();
    }
}
