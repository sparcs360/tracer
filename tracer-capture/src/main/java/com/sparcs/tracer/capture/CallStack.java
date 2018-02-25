package com.sparcs.tracer.capture;

import org.aspectj.lang.JoinPoint;

import java.util.Stack;

public class CallStack {

    private Stack<Call> stack = new Stack<>();

    public Call push(JoinPoint joinPoint) {

        Call call = Call.from(joinPoint, stack.size());
        return stack.push(call);
    }

    public Call pop(Object result) {

        Call call = stack.pop();
        call.markExecuted(result);
        return call;
    }
}
