package uz.xnarx.soliq.Exception;

public class InvalidCardBalanceException extends RuntimeException {
    public InvalidCardBalanceException(String message) {
        super(message);
    }
}
