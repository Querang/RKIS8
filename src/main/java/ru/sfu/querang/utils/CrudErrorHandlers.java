package ru.sfu.querang.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.sfu.querang.exceptions.notCreated.ModelNotCreatedException;
import ru.sfu.querang.exceptions.notDeleted.ModelNotDeletedException;
import ru.sfu.querang.exceptions.notFound.ModelNotFoundException;
import ru.sfu.querang.exceptions.notUpdated.ModelNotUpdatedException;
import ru.sfu.querang.exceptions.responses.ErrorResponse;

public class CrudErrorHandlers
        <C extends ModelNotCreatedException, R extends ModelNotFoundException, U extends ModelNotUpdatedException, D extends ModelNotDeletedException> {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleCreateException(C e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleReadException(R e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleUpdateException(U e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDeleteException(D e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
