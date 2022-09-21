package tech.icoding.websocket.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.icoding.websocket.data.Result;


/** 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Result globalException(final Throwable e) {
        log.error("System Error!", e);
        return Result.fail(99999, "系统异常，请联系管理员");
    }


    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result busException(final BizException e) {
        log.info("Business Error!" + e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }
}