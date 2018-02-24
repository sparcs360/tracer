package com.sparcs.tracer;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Collection;

public aspect Tracer {

    protected static int callDepth = 0;
    private static String indentation = "                            ";

    private String invocationToString(JoinPoint.StaticPart thisJoinPointStaticPart, JoinPoint thisJoinPoint,
                                      boolean isEntering) {

        StringBuilder sb = new StringBuilder();

        if (isEntering) {
            callDepth++;
        }

        sb.append(indentation.substring(0, callDepth));

        sb.append(isEntering ? "+" : "-");
        sb.append(thisJoinPointStaticPart.getSignature().getName());
//                .append(thisJoinPointStaticPart.getSignature().getDeclaringType().getName());
        printParameters(sb, thisJoinPoint);

        if (!isEntering) {
            callDepth--;
        }

        return sb.toString();
    }

    private void printParameters(StringBuilder sb, JoinPoint thisJoinPoint) {

        Object[] args = thisJoinPoint.getArgs();
        String[] names = ((CodeSignature) thisJoinPoint.getSignature()).getParameterNames();
        Class[] types = ((CodeSignature) thisJoinPoint.getSignature()).getParameterTypes();

        sb.append("(");
        for (int i = 0; i < args.length; i++) {
            sb.append(names[i]).append("=");
            if (types[i].isArray()) {
                sb.append(StringUtils.arrayToCommaDelimitedString((Object[]) args[i]));
            } else {
                sb.append(args[i].toString());
            }
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
    }

    final static Logger LOG = LoggerFactory.getLogger("TRACER");

    pointcut tracedMethods():
            execution(* *(..)) && !within(Tracer);

    Object around(): tracedMethods() {

        if (LOG.isTraceEnabled()) {

            LOG.trace(invocationToString(thisJoinPointStaticPart, thisJoinPoint, true));
        }

        Object result = proceed();

        if (LOG.isTraceEnabled()) {
            LOG.trace(invocationToString(thisJoinPointStaticPart, thisJoinPoint, false));
        }

        return result;
    }
}
