package kr.ac.kopo.travel_home.global.payload.exception.handler;

import kr.ac.kopo.travel_home.global.payload.code.BaseErrorCode;
import kr.ac.kopo.travel_home.global.payload.exception.GeneralException;

public class ErrorHandler extends GeneralException {

    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }

}
