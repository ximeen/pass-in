package passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import passin.domain.attendee.exceptions.AttendeeNotFoundException;
import passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import passin.domain.event.exceptions.EventIsFullException;
import passin.domain.event.exceptions.eventNotFoundException;
import passin.dto.general.ErrorResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {
    @ExceptionHandler(eventNotFoundException.class)
    public ResponseEntity handleEventNotFound(eventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EventIsFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventIsFull(EventIsFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExists(AttendeeAlreadyExistException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
