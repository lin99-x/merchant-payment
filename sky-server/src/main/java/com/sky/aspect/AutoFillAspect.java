package com.sky.aspect;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * Point cut
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * Before notification
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("Start auto filling...");

        // Get the current query type
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        // Get parameters from the current method
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        // Prepare data for auto fill
        OffsetDateTime now = OffsetDateTime.now();

        // Based on the current operation type, auto fill data
        if (operationType == OperationType.INSERT) {
            try {
                Method setCreatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, OffsetDateTime.class);
                Method setUpdatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, OffsetDateTime.class);

                // Invoke the value to entity
                setCreatedAt.invoke(entity, now);
                setUpdatedAt.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, OffsetDateTime.class);

                // Invoke the value to entity
                setUpdatedAt.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
