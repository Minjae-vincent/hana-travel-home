package kr.ac.kopo.travel_home.global.payload.exception;

public abstract class BaseException extends RuntimeException{
    public abstract BaseExceptionType getExceptionType();
}