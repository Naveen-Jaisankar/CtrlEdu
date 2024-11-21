package com.jobizzz.ctrledu.dto;

public class ThreadContext {
    private static final ThreadLocal<String> THREAD_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<String> DATABASE_CONTEXT = new ThreadLocal<>();

    public static void setThreadContext(String schemaName,String databaseName){

        THREAD_CONTEXT.set(schemaName);
        DATABASE_CONTEXT.set(databaseName);
    }

    public static String getThreadContext(){
        return THREAD_CONTEXT.get();
    }

    public static String getDatabaseName() {
        return DATABASE_CONTEXT.get();
    }

    public static void clear() {
        THREAD_CONTEXT.remove();
        DATABASE_CONTEXT.remove();
    }
}
