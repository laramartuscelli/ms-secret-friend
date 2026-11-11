package org.secretfriend.exception;

import lombok.extern.slf4j.Slf4j;
import org.secretfriend.exception.base.NotFoundException;
import org.secretfriend.exception.base.UnprocessableContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_LOG_MESSAGE = "{}: {}";

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {

        log.warn(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Not Found");
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

        List<String> errorMessages = new ArrayList<>();
        ex.getValueResults().forEach(result -> {
            result.getResolvableErrors().forEach(error -> {
                errorMessages.add(error.getDefaultMessage());
            });
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "There are invalid parameters"
        );

        problemDetail.setTitle("Invalid Parameters");
        problemDetail.setProperty("errors", errorMessages);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {

        log.error(DEFAULT_LOG_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please, contact the support"
        );

        problemDetail.setTitle("Unexpected Error");
        return problemDetail;
    }
}
