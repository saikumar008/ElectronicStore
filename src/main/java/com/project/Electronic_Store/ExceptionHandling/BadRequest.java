package com.project.Electronic_Store.ExceptionHandling;

public class BadRequest extends RuntimeException{

    public BadRequest(String message){
        super(message);
    }

    public BadRequest(){
        super("Bad Request !!");
    }
}
