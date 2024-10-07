package kr.ac.kopo.travel_home.global.payload.exception;

import kr.ac.kopo.travel_home.global.payload.code.BaseErrorCode;
import kr.ac.kopo.travel_home.global.payload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
