package com.jobizzz.ctrledu.dto;

public class ThreadContext {
    private static final ThreadLocal<String> THREAD_CONTEXT = new ThreadLocal<>();

    public static void setThreadContext(String schemaName){
        THREAD_CONTEXT.set(schemaName);
    }

    public static String getThreadContext(){
        return THREAD_CONTEXT.get();
    }

    public static void clear(){
        THREAD_CONTEXT.remove();
    }
}
