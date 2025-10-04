package ru.nsu.rush_c.error.exception;

public class CrmException extends RuntimeException {
    protected String message;
    protected Integer code;
    public CrmException(String message, Integer id) {
        this.message = message;
        this.code = id;
    }

    public CrmException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public Integer getId(){
        return code;
    }
}
