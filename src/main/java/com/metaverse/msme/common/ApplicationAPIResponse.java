package com.metaverse.msme.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationAPIResponse<T> {

    private T data;
    private Object code;
    private String message;
    private boolean success;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;

    public ApplicationAPIResponse(T data, boolean success) {
        this.data = data;
        this.success = success;
    }

    // Return the stored timestamp or current time if null
    public LocalDateTime getTimestamp() {
        return timestamp != null ? timestamp : LocalDateTime.now();
    }

    /**
     * Create a new builder for ApplicationAPIResponse with proper generic type handling
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder class for ApplicationAPIResponse with proper generic type support
     */
    public static class Builder<T> {
        private T data;
        private Object code;
        private String message;
        private boolean success;
        private LocalDateTime timestamp;

        /**
         * Set the data with proper generic type
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * Set the response code
         */
        public Builder<T> code(Object code) {
            this.code = code;
            return this;
        }

        /**
         * Set the response message
         */
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the success flag
         */
        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        /**
         * Set the timestamp
         */
        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Build the ApplicationAPIResponse object
         */
        public ApplicationAPIResponse<T> build() {
            ApplicationAPIResponse<T> response = new ApplicationAPIResponse<>();
            response.data = this.data;
            response.code = this.code;
            response.message = this.message;
            response.success = this.success;
            response.timestamp = this.timestamp;
            return response;
        }
    }
}

