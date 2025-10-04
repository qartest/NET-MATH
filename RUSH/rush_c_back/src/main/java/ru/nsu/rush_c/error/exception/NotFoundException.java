package ru.nsu.rush_c.error.exception;

public class NotFoundException extends CrmException {
    public NotFoundException(String message, Integer id) {
        super(message, id);
    }
}
