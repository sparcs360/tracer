package com.sparcs.tracer.capture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect Tracer {

    private final static Logger LOG = LoggerFactory.getLogger("TRACER");
    private final static CallStack callStack = new CallStack();

    pointcut tracedMethods():
            (execution(new(..)) ||
                    execution(* *(..))) &&
                    !within(com.sparcs.tracer.capture.*);

    Object around(): tracedMethods() {

        if (LOG.isTraceEnabled()) {

            Call call = callStack.push(thisJoinPoint);
            LOG.trace(call.toString());
        }

        Object result = proceed();

        if (LOG.isTraceEnabled()) {

            Call call = callStack.pop(result);
            LOG.trace(call.toString());
        }

        return result;
    }
}
