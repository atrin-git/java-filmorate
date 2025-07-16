package ru.yandex.practicum.filmorate.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.AuditDbStorage;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditDbStorage auditStorage;
    private final BeanFactory beanFactory;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(auditAnnotation)")
    public Object logAuditEvent(ProceedingJoinPoint joinPoint, Auditable auditAnnotation) throws Throwable {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(beanFactory));

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        Long userId = null;
        if (Boolean.parseBoolean(auditAnnotation.isDeleting())) {
            userId = evaluateIds(auditAnnotation.userId(), context);
        }

        Object result = joinPoint.proceed();
        context.setVariable("result", result);

        Long entityId = evaluateIds(auditAnnotation.entityId(), context);
        if (userId == null) {
            userId = evaluateIds(auditAnnotation.userId(), context);
        }

        if (entityId != null && userId != null) {
            auditStorage.addEvent(
                    userId,
                    auditAnnotation.eventName(),
                    auditAnnotation.operationName(),
                    entityId
            );
            log.info("В Аудит записано событие {}.{} для пользователя {}", auditAnnotation.eventName(), auditAnnotation.operationName(), userId);
        } else {
            log.warn("Не удалось записать действие в Аудит для события {}.{}", auditAnnotation.eventName(), auditAnnotation.operationName());
        }

        return result;
    }

    private Long evaluateIds(String expression, StandardEvaluationContext context) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("SpEL выражение не может быть пустым");
        }

        Expression expr = parser.parseExpression(expression);
        return expr.getValue(context, Long.class);
    }
}
