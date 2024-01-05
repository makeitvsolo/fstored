package github.makeitvsolo.fstored.boot.api.exception.handling;

import github.makeitvsolo.fstored.boot.api.message.ErrorMessage;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.FileDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.file.exception.WrongFileHandleException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderAlreadyExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.FolderDoesNotExistsException;
import github.makeitvsolo.fstored.storage.application.usecase.folder.exception.WrongFolderHandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StorageExceptionHandling {

    private static final Logger LOG = LoggerFactory.getLogger(StorageExceptionHandling.class);

    @ExceptionHandler({WrongFileHandleException.class, WrongFolderHandleException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleWrongFileHandleException(final Throwable ex) {
        LOG.info(String.format("client error occurs: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.from(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({FileDoesNotExistsException.class, FolderDoesNotExistsException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleFileDoesNotExistsException(final Throwable ex) {
        LOG.info(String.format("client error occurs: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.from(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler({FileAlreadyExistsException.class, FolderAlreadyExistsException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ResponseEntity<?> handleFileAlreadyExistsException(final Throwable ex) {
        LOG.info(String.format("client error occurs: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorMessage.from(HttpStatus.CONFLICT, ex.getMessage()));
    }
}
