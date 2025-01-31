package com.ctrledu.MasterService.model;

public class Thread {

    private static final ThreadLocal<String> email = new ThreadLocal<>();
    public static String getEmail(){
        return email.get();
    }

    public static void setEmail(String emailId){
        email.set(emailId);
    }

    public static void clear(){
        email.remove();
    }
}
