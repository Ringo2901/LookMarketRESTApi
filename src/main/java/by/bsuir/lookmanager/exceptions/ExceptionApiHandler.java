package by.bsuir.lookmanager.exceptions;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.services.impl.CatalogServiceImpl;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    private static final Logger LOGGER = LogManager.getLogger(ExceptionApiHandler.class);
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchValidationException(ConstraintViolationException exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn(exception.getMessage());
        responseDto.setMessage(exception.getMessage());
        responseDto.setStatus("ERROR");
        responseDto.setCode(400);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchNotFoundException(NotFoundException exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn(exception.getMessage());
        responseDto.setMessage(exception.getMessage());
        responseDto.setStatus("ERROR");
        responseDto.setCode(404);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchAlreadyExistsException(AlreadyExistsException exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn(exception.getMessage());
        responseDto.setMessage(exception.getMessage());
        responseDto.setStatus("ERROR");
        responseDto.setCode(406);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }

    @ExceptionHandler(BadParameterValueException.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchBadParameterValueException(BadParameterValueException exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn(exception.getMessage());
        responseDto.setMessage(exception.getMessage());
        responseDto.setStatus("ERROR");
        responseDto.setCode(400);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }

    @ExceptionHandler(UnauthorizedAccessError.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchUnauthorizedAccessError(UnauthorizedAccessError exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn(exception.getMessage());
        responseDto.setMessage(exception.getMessage());
        responseDto.setStatus("ERROR");
        responseDto.setCode(401);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApplicationResponseDto<?>> catchJwtExceptionError(JwtException exception) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.warn("Unauthorized access");
        responseDto.setMessage("Unauthorized access");
        responseDto.setStatus("ERROR");
        responseDto.setCode(401);
        return ResponseEntity
                .status(responseDto.getCode())
                .body(responseDto);
    }
}

