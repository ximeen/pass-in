package passin.domain.event.exceptions;

public class eventNotFoundException extends RuntimeException{
    public eventNotFoundException(String message){
        super(message);
    }
}
