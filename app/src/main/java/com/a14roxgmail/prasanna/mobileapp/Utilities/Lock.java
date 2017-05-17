package com.a14roxgmail.prasanna.mobileapp.Utilities;

/**
 * Created by prasanna on 5/17/17.
 */

public class Lock {
    boolean lock;
    public Lock(){
        lock = false;
    }
    public boolean setLock(){
        if(lock){
            return false;
        }else {
            lock = true;
            return true;
        }
    }
    public boolean unLock(){
        if(lock){
            lock = false;
            return  true;
        }else{
            return false;
        }
    }
    public boolean isLock(){
        return lock;
    }
}
