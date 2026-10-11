package com.example.my_super_app.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ApiError error;
    private final Pagination pagination;

    private ApiResponse(boolean success, T data, ApiError error, Pagination pagination) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.pagination = pagination;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> success(T data, Pagination pagination) {
        return new ApiResponse<>(true, data, null, pagination);
    }

    public static ApiResponse<Void> error(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message, null), null);
    }

    public static ApiResponse<Void> error(String code, String message, List<FieldError> details) {
        return new ApiResponse<>(false, null, new ApiError(code, message, details), null);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public static class ApiError {
        private final String code;
        private final String message;
        private final List<FieldError> details;

        public ApiError(String code, String message, List<FieldError> details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public List<FieldError> getDetails() {
            return details;
        }
    }

    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class Pagination {
        private final String nextCursor;
        private final boolean hasMore;
        private final int limit;

        public Pagination(String nextCursor, boolean hasMore, int limit) {
            this.nextCursor = nextCursor;
            this.hasMore = hasMore;
            this.limit = limit;
        }

        public String getNextCursor() {
            return nextCursor;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public int getLimit() {
            return limit;
        }
    }

}

