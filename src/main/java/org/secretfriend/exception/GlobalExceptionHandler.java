package org.secretfriend.exception;

import lombok.extern.slf4j.Slf4j;
import org.secretfriend.exception.base.InternalServerErrorException;
import org.secretfriend.exception.base.NotFoundException;
import org.secretfriend.exception.base.UnauthorizedException;
import org.secretfriend.exception.base.UnprocessableContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_LOG_MESSAGE = "{}: {}";
    private static final String GENERIC_EXCEPTION_LOG_MESSAGE = "Unexpected error on path: {} | " + DEFAULT_LOG_MESSAGE + " | {}";

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Access denied"
        );

        problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ProblemDetail handleInternalServerErrorException(InternalServerErrorException ex) {

        log.error(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred during the request. Please, try again later or contact the support"
        );

        problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );

        problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
        return problemDetail;
    }

    @ExceptionHandler(UnprocessableContentException.class)
    public ProblemDetail handleUnprocessableContentException(UnprocessableContentException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );

        problemDetail.setTitle("Business Violation");
        return problemDetail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleHandlerMethodValidationException(HandlerMethodValidationException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        List<String> errorMessages = extractInvalidParametersErrors(ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "There are invalid parameters"
        );

        problemDetail.setTitle("Invalid Parameters");
        problemDetail.setProperty("errors", errorMessages);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {

        log.error(GENERIC_EXCEPTION_LOG_MESSAGE,
                request.getDescription(false),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                extractErrorLocation(ex));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please, contact the support"
        );

        problemDetail.setTitle("Unexpected Error");
        return problemDetail;
    }

    private List<String> extractInvalidParametersErrors(HandlerMethodValidationException ex) {

        List<String> errorMessages = new ArrayList<>();
        ex.getParameterValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(error ->
                        errorMessages.add(error.getDefaultMessage())
                )
        );

        return errorMessages;
    }

    private String extractErrorLocation(Exception ex) {

        StackTraceElement stack = Arrays.stream(ex.getStackTrace())
                .filter(s -> s.getClassName().startsWith("org.secretfriend"))
                .toList().getFirst();

        return "Location: " + stack.getFileName() +
                " | Method: " + stack.getMethodName() +
                " | Line: " + stack.getLineNumber();
    }
}
