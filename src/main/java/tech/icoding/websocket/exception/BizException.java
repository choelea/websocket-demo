package tech.icoding.websocket.exception;

/**
 * 业务异常
 * @author : Joe
 * @date : 2022/9/21
 */
public class BizException extends RuntimeException{
    private static final long serialVersionUID = -4657060710564902601L;

    public int getCode() {
        return code;
    }

    /**
     * 业务返回值编码
     * 统一code长度6位
     */
    private int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message){
        this(499, message);
    }

}
