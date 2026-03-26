package com.bravos.recruitment.libs.starter.configuration.exadvice;

import com.bravos.recruitment.libs.utils.exceptions.*;
import com.bravos.recruitment.libs.utils.models.FieldError;
import com.bravos.recruitment.libs.utils.models.RApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * Global exception handler that provides centralized exception handling for
 * REST controllers.
 * <p>
 * This advice catches various exception types and transforms them into
 * standardized
 * {@link com.bravos.recruitment.libs.utils.models.RApiResponse} responses with appropriate HTTP status codes.
 * </p>
 *
 * <p>
 * <b>Handled Exceptions:</b>
 * </p>
 * <ul>
 * <li>{@link NotFoundException} - HTTP 404 Not Found</li>
 * <li>{@link BadRequestException} - HTTP 400 Bad Request</li>
 * <li>{@link ConflictDataException} - HTTP 409 Conflict</li>
 * <li>{@link ForbiddenException} - HTTP 403 Forbidden</li>
 * <li>{@link UnauthorizeException} - HTTP 401 Unauthorized</li>
 * <li>{@link TooManyRequestException} - HTTP 429 Too Many Requests</li>
 * <li>{@link InternalErrorException} - HTTP 500 Internal Server Error</li>
 * <li>{@link MethodArgumentTypeMismatchException} - HTTP 400 Bad Request</li>
 * <li>{@link MethodArgumentNotValidException} - HTTP 400 Bad Request</li>
 * <li>{@link Exception} - HTTP 500 Internal Server Error (catch-all)</li>
 * </ul>
 */
@Slf4j
@ControllerAdvice
public class DefaultGlobalExceptionAdvice {

  /**
   * Handles generic exceptions not caught by other handlers.
   *
   * @param exception the exception that was thrown
   * @return a ResponseEntity containing the error response with HTTP 500 status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<RApiResponse<?>> handleGenericException(Exception exception) {
    log.error("Unhandled exception occurred", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new RApiResponse<>(
            ExceptionConstant.INTERNAL_ERROR_CODE,
            null,
            ExceptionConstant.INTERNAL_ERROR_MESSAGE
        )
    );
  }

  /**
   * Handles NotFoundException and returns HTTP 404 Not Found.
   *
   * @param exception the NotFoundException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 404 status
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<RApiResponse<?>> handleNotFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles BadRequestException and returns HTTP 400 Bad Request.
   *
   * @param exception the BadRequestException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 400 status
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<RApiResponse<?>> handleBadRequestException(BadRequestException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles ConflictDataException and returns HTTP 409 Conflict.
   *
   * @param exception the ConflictDataException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 409 status
   */
  @ExceptionHandler(ConflictDataException.class)
  public ResponseEntity<RApiResponse<?>> handleConflictDataException(ConflictDataException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles ForbiddenException and returns HTTP 403 Forbidden.
   *
   * @param exception the ForbiddenException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 403 status
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<RApiResponse<?>> handleForbiddenException(ForbiddenException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles UnauthorizeException and returns HTTP 401 Unauthorized.
   *
   * @param exception the UnauthorizeException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 401 status
   */
  @ExceptionHandler(UnauthorizeException.class)
  public ResponseEntity<RApiResponse<?>> handleUnauthorizeException(UnauthorizeException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles TooManyRequestException and returns HTTP 429 Too Many Requests.
   *
   * @param exception the TooManyRequestException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 429 status
   */
  @ExceptionHandler(TooManyRequestException.class)
  public ResponseEntity<RApiResponse<?>> handleTooManyRequestException(TooManyRequestException exception) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles InternalErrorException and returns HTTP 500 Internal Server Error.
   *
   * @param exception the InternalErrorException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 500 status
   */
  @ExceptionHandler(InternalErrorException.class)
  public ResponseEntity<RApiResponse<?>> handleInternalErrorException(InternalErrorException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new RApiResponse<>(
            exception.getCode(),
            null,
            exception.getMessage()
        )
    );
  }

  /**
   * Handles NoHandlerFoundException when no handler is found for a request.
   *
   * @return a ResponseEntity containing the error response with HTTP 404 status
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<RApiResponse<?>> handleNoHandlerFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new RApiResponse<>(
            ExceptionConstant.NOT_FOUND_CODE,
            null,
            ExceptionConstant.NOT_FOUND_MESSAGE
        )
    );
  }

  private static final String INVALID_PARAMETER_MESSAGE_TEMPLATE = "Invalid value '%s' for parameter '%s'. Expected type: %s";

  /**
   * Handles MethodArgumentTypeMismatchException when a request parameter cannot
   * be converted to the expected type.
   *
   * @param exception the MethodArgumentTypeMismatchException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 400 status
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<RApiResponse<?>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    String requiredType = exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown type";
    String errorMessage = String.format(INVALID_PARAMETER_MESSAGE_TEMPLATE, exception.getValue(), exception.getName(), requiredType);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new RApiResponse<>(
            ExceptionConstant.BAD_REQUEST_CODE,
            null,
            errorMessage
        )
    );
  }

  /**
   * Handles MethodArgumentNotValidException for validation errors on request body
   * fields.
   * <p>
   * Returns a list of {@link FieldError} objects containing details about each
   * invalid field.
   * </p>
   *
   * @param exception the MethodArgumentNotValidException that was thrown
   * @return a ResponseEntity containing the error response with field errors and
   * HTTP 400 status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RApiResponse<List<FieldError>>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> new FieldError(error.getField(),
            error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"))
        .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new RApiResponse<>(
            ExceptionConstant.BAD_REQUEST_CODE,
            fieldErrors,
            ExceptionConstant.BAD_REQUEST_MESSAGE
        )
    );
  }

  /**
   * Handles HttpRequestMethodNotSupportedException when an HTTP method is not
   * supported for a request.
   *
   * @param exception the HttpRequestMethodNotSupportedException that was thrown
   * @return a ResponseEntity containing the error response with HTTP 405 status
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<RApiResponse<?>> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new RApiResponse<>(
            ExceptionConstant.NOT_FOUND_CODE,
            null,
            String.format("HTTP method '%s' is not supported for this endpoint", exception.getMethod())
        ));
  }

}
