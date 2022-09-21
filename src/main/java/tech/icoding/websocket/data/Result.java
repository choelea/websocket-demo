package tech.icoding.websocket.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 业务返回值封装 - 用于接口层
 *
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable {

    protected final static int SUCCESS = 200;
    protected final static String SUCCESS_MSG = "SUCCESS";
    private static final long serialVersionUID = 4269011685109360994L;
    /**
     * 业务返回值编码
     * 统一code长度6位
     */
    private int code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 当前时间
     */
    private long timestamp;

    /**
     * 业务返回值
     */
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.msg = msg;
        this.data = null;
    }


    /**
     * 默认返回成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result success(T result) {
        return new Result<>(SUCCESS, SUCCESS_MSG, result);
    }

    public static <T> Result success(String meg, T result) {
        return new Result<>(SUCCESS, meg, result);
    }

    /***
     * 默认返回成功
     * @return
     */
    public static Result<?> success() {
        return new Result(SUCCESS, SUCCESS_MSG);
    }


    /***
     * 返回失败
     * @param errorCode  错误码
     * @param msg  错误信息
     * @return
     */
    public static Result fail(int errorCode, String msg) {
        return new Result(errorCode, msg);
    }


    public static <T> Result fail(int errorCode, String msg, T result) {
        return new Result(errorCode, msg, result);
    }

    /***
     * 是否返回成功结果
     * @return
     */
    public boolean isSuccess() {
        return SUCCESS == code ? true : false;
    }
}