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

    pointcut annotatedMethod():
            cflow(@annotation(TraceSubject)) &&
                        !within(com.sparcs.tracer.capture.*);

    Object around(): annotatedMethod() {
    //Object around(): tracedMethods() {

        if (!thisJoinPointStaticPart.getKind().equals("method-call")) {
            Call callOut = callStack.push(thisJoinPoint);
            if (LOG.isTraceEnabled()) {
                LOG.trace(callOut.toString());
            }
        }

        Object result = proceed();

        if (!thisJoinPointStaticPart.getKind().equals("method-call")) {
            Call callIn = callStack.pop(result);
            if (LOG.isTraceEnabled()) {

                LOG.trace(callIn.toString());
            }
        }

        return result;
    }
}
