package com.sparcs.tracer.capture;

import java.util.Objects;

public class InstanceIdentifer implements Comparable<InstanceIdentifer> {

    private static int nextOrder = 0;
    public static final InstanceIdentifer GOD = new InstanceIdentifer();
    private final int order;
    private final Class<?> clazz;
    private final boolean isStatic;
    private final int hashCode;

    InstanceIdentifer(Class<?> clazz, Object _this) {

        this.order = InstanceIdentifer.nextOrder++;
        this.clazz = clazz;

        if (_this == null) {
            this.isStatic = true;
            this.hashCode = clazz.hashCode();
        } else {
            this.isStatic = false;
            this.hashCode = _this.hashCode();
        }
    }

    private InstanceIdentifer() {
        this.order = -1;
        this.clazz = God.class;
        this.isStatic = true;
        this.hashCode = 0;
    }

    private String getHashCodeAsHexString() {

        return Integer.toHexString(hashCode);
    }

    public String getId() {

        if (this.equals(GOD)) {
            return "[";
        }
        if (this.isStatic) {
            return "C" + getHashCodeAsHexString();
        }
        return "O" + getHashCodeAsHexString();
    }

    private String getClassName() {

        if (this.equals(GOD)) {
            return "GOD";
        }
        return clazz.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstanceIdentifer that = (InstanceIdentifer) o;
        return isStatic == that.isStatic &&
                hashCode == that.hashCode &&
                Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {

        return hashCode;
    }

    @Override
    public int compareTo(InstanceIdentifer o) {

        return order - o.order;
    }

    @Override
    public String toString() {

        if (this.isStatic) {
            return "**" + getClassName() + "**";
        }

        return getHashCodeAsHexString() + "\\n(**" + getClassName() + "**)";
    }

    private static class God {
    }
}
