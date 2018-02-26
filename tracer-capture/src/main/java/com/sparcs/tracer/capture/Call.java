package com.sparcs.tracer.capture;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.FieldSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

public class Call {

    private static final String nl = System.lineSeparator();
    private static final String indentation = "                            ";

    private boolean isExecuting;
    private final InstanceIdentifer instanceIdentifer;
    private final JoinPoint joinPoint;
    private final int depth;
    private Object result;

    public static Call from(JoinPoint joinPoint, int depth) {

        return new Call(joinPoint, depth);
    }

    private Call(JoinPoint joinPoint, int depth) {

        this.isExecuting = true;
        this.instanceIdentifer = new InstanceIdentifer(joinPoint.getStaticPart().getSignature().getDeclaringType(), joinPoint.getThis());
        this.joinPoint = joinPoint;
        this.depth = depth;
    }

    public InstanceIdentifer getInstanceIdentifier() {
        return instanceIdentifer;
    }

    public String getMethodName() {
        return joinPoint.getSignature().toShortString();
    }

    void markExecuted(Object result) {
        isExecuting = false;
        this.result = result;
    }

    public Object getReturnValue() {
        return result;
    }

    @Override
    public String toString() {
        return invocationToString(joinPoint.getStaticPart(), joinPoint, isExecuting);
    }

    private String invocationToString(JoinPoint.StaticPart thisJoinPointStaticPart, JoinPoint thisJoinPoint,
                                      boolean isEntering) {

        StringBuilder sb = new StringBuilder();

        String indent = indentation.substring(0, depth);
        sb.append(nl)
                .append(indent).append(isEntering ? "+" : "-")
                .append(thisJoinPointStaticPart.getKind()).append(" ")
                .append(thisJoinPointStaticPart.getSignature()).append(" @ ")
                .append(thisJoinPointStaticPart.getSourceLocation())
                .append(nl)
                //.append(indent).append(isEntering ? "+" : "-").append(thisJoinPointStaticPart.toLongString()).append(nl)
                //.append(indent).append(isEntering ? "+" : "-").append(thisJoinPointStaticPart.toShortString()).append(nl)
                .append(indent).append(isEntering ? "+" : "-").append(thisJoinPoint.getThis());
        printParameters(sb, thisJoinPoint);
        sb.append(nl);

        if (!isExecuting) {
            sb.append(indent).append("=").append(result).append(nl);
        }

        return sb.toString();
    }

    private void printParameters(StringBuilder sb, JoinPoint thisJoinPoint) {

        sb.append("(");

        Object[] args = thisJoinPoint.getArgs();
        Signature signature = thisJoinPoint.getSignature();

        if (signature instanceof CodeSignature) {

            String[] names = ((CodeSignature) signature).getParameterNames();
            Class[] types = ((CodeSignature) signature).getParameterTypes();

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
        } else {

            Field field = ((FieldSignature) signature).getField();
            sb.append(field.getName());

        }

        sb.append(")");
    }
}
