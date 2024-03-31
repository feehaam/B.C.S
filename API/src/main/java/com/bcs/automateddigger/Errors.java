package com.bcs.automateddigger;

import java.util.ArrayList;
import java.util.List;

public class Errors {
    public static boolean hasAny(List<Object> results){
        for(Object object: results){
            if(object instanceof Exception){
                return true;
            }
        }
        return false;
    }

    public static List<Object> listAll(List<Object> results){
        List<Object> errors = new ArrayList<>();
        for(Object object: results){
            if(object instanceof Exception){
                errors.add(object);
            }
        }
        return errors;
    }

    public static List<Object> listSuccess(List<Object> results){
        List<Object> suc = new ArrayList<>();
        for(Object object: results){
            if(!(object instanceof Exception)){
                suc.add(object);
            }
        }
        return suc;
    }
}
