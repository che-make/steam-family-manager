package com.metamedicsvr.springtemplate.error;

import com.metamedicsvr.springtemplate.error.dto.ErrorMessage;
import com.metamedicsvr.springtemplate.error.exception.CsvFormatException;
import com.metamedicsvr.springtemplate.error.exception.DuplicateEntryException;
import com.metamedicsvr.springtemplate.error.exception.EmailLimitExceededException;
import com.metamedicsvr.springtemplate.error.exception.ExpirePasswordTokenException;
import com.metamedicsvr.springtemplate.error.exception.FileProcessingException;
import com.metamedicsvr.springtemplate.error.exception.JsonFormatException;
import com.metamedicsvr.springtemplate.error.exception.LanguageNotFoundException;
import com.metamedicsvr.springtemplate.error.exception.MissingPropertyException;
import com.metamedicsvr.springtemplate.error.exception.NotFoundException;
import com.metamedicsvr.springtemplate.error.exception.UnauthorizedEmailException;
import com.metamedicsvr.springtemplate.error.exception.UnsupportedFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorMessage>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorMessage> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorMessage(HttpStatus.BAD_REQUEST, fieldName + ": " + errorMessage));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getReason()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleEmailLimitExceededException(EmailLimitExceededException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleMissingPropertyException(MissingPropertyException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleSqlIntegrityException(DuplicateEntryException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.CONFLICT, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpirePasswordTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleExpirePasswordTokenException(ExpirePasswordTokenException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedEmailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleUnauthorizedEmailException(UnauthorizedEmailException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CsvFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorMessage> handleCsvFormatException(CsvFormatException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(JsonFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorMessage> handleJsonFormatException(JsonFormatException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FileProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleCsvProcessingException(FileProcessingException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedFileException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleUnsupportedFileException(UnsupportedFileException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ErrorMessage> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage()), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(LanguageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleLanguageNotFoundException(LanguageNotFoundException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
