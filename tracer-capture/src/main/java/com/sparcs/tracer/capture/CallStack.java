package com.sparcs.tracer.capture;

import org.aspectj.lang.JoinPoint;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class CallStack {

    private Set<InstanceIdentifer> participants = new HashSet<>();
    private StringBuilder sb = new StringBuilder();
    private Stack<Call> stack = new Stack<>();

    public Call push(JoinPoint joinPoint) {

        Call call = Call.from(joinPoint, stack.size());
        participants.add(call.getInstanceIdentifier());

        InstanceIdentifer caller = stack.isEmpty() ? InstanceIdentifer.GOD : stack.peek().getInstanceIdentifier();

        sb.append(caller.getId())
                .append("->")
                .append(call.getInstanceIdentifier().getId())
                .append(":")
                .append(call.getMethodName())
                .append("\n");

        stack.push(call);
        return call;
    }

    public Call pop(Object result) {

        Call call = stack.pop();
        call.markExecuted(result);

        InstanceIdentifer caller = stack.isEmpty() ? InstanceIdentifer.GOD : stack.peek().getInstanceIdentifier();

        if (!caller.equals(call.getInstanceIdentifier())) {

            sb
                    .append(call.getInstanceIdentifier().getId())
                    .append("-->")
                    .append(caller.getId())
                    .append(":")
                    .append(call.getReturnValue())
                    .append("\n");

            dumpSequence();
        }

        return call;
    }

    private void dumpSequence() {
        participants.stream().filter(p -> !p.equals(InstanceIdentifer.GOD)).forEach(p -> System.out.println("participant \"" + p.toString() + "\" as " + p.getId()));
        System.out.println("\n" + sb.toString());
    }
}
