package BridgeLabz.Book_Store_Application.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * All methods inside controller package
     */
    @Pointcut("execution(* BridgeLabz.Book_Store_Application..controller..*(..))")
    public void controllerLayer() {
    }

    /**
     * All methods inside service package
     */
    @Pointcut("execution(* BridgeLabz.Book_Store_Application..service..*(..))")
    public void serviceLayer() {
    }

    /**
     * Before Controller
     */
    @Before("controllerLayer()")
    public void logBeforeController(JoinPoint joinPoint) {

        log.info("========== REQUEST START ==========");
        log.info("Class  : {}", joinPoint.getTarget().getClass().getSimpleName());
        log.info("Method : {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            log.info("Argument : {}", arg);
        }
    }

    /**
     * After Controller
     */
    @AfterReturning(
            pointcut = "controllerLayer()",
            returning = "result"
    )
    public void logAfterController(JoinPoint joinPoint, Object result) {

        log.info("Method Completed : {}", joinPoint.getSignature().getName());
        log.info("Response : {}", result);
        log.info("========== REQUEST END ==========");
    }

    /**
     * Exception Logging
     */
    @AfterThrowing(
            pointcut = "controllerLayer() || serviceLayer()",
            throwing = "exception"
    )
    public void logException(
            JoinPoint joinPoint,
            Exception exception
    ) {

        log.error("Exception in {} : {}",
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception);
    }

    /**
     * Execution Time
     */
    @Around("serviceLayer()")
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        log.info("{} executed in {} ms",
                joinPoint.getSignature().getName(),
                (end - start));

        return result;
    }
}