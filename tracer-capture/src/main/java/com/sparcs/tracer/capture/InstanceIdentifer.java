package com.sparcs.tracer.capture;

import org.aspectj.lang.JoinPoint;

import java.util.Objects;

public class InstanceIdentifer {

    private static class God extends Object {}
    public static final InstanceIdentifer GOD = new InstanceIdentifer();

    private final Class<? extends Object> clazz;
    private final boolean isStatic;
    private final int hashCode;

    public InstanceIdentifer(JoinPoint joinPoint) {

        this.clazz = joinPoint.getStaticPart().getSignature().getDeclaringType();

        if (joinPoint.getThis() == null) {
            this.isStatic = true;
            this.hashCode = 0;
        } else {
            this.isStatic = false;
            this.hashCode = joinPoint.getThis().hashCode();
        }
    }

    private InstanceIdentifer() {
        this.clazz = God.class;
        this.isStatic = true;
        this.hashCode = 0;
    }

    public String getHashCodeAsHexString() {

        return Integer.toHexString(hashCode);
    }

    public String getId() {

        if (this.equals(GOD)) {
            return "[";
        }
        return "O" + getHashCodeAsHexString();
    }

    public String getClassName() {

        if (this.equals(GOD)) {
            return "GOD";
        } else {
            return clazz.getSimpleName();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceIdentifer that = (InstanceIdentifer) o;
        return hashCode == that.hashCode &&
                Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clazz, hashCode);
    }

    @Override
    public String toString() {

        return getHashCodeAsHexString() + "\\n(**" + getClassName() + "**)";
    }
}
