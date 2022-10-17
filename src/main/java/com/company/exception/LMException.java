package com.company.exception;

public class LMException extends RuntimeException{

    public LMException(String message)
    {
        super(message);
    }
    public LMException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public LMException(Throwable cause){
        super(cause);
    }
}
