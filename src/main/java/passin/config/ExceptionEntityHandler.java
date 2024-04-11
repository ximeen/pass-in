package passin.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import passin.domain.event.exceptions.eventNotFoundException;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(eventNotFoundException.class)
    public ResponseEntity handleEventNotFound(eventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
}
