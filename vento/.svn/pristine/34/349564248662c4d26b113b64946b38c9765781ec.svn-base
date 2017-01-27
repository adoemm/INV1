package jspread.core.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author VIOX
 */
public final class Executor {

    public static void invoke(String ClassForName, String Method, Class[] parametersTypes, Object[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = Class.forName(ClassForName);
        Method m = c.getDeclaredMethod(Method, parametersTypes);
        Object i = c.newInstance();
        m.invoke(i, args);
    }

    public static Object invokeReturnObject(String ClassForName, String Method, Class[] parametersTypes, Object[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = Class.forName(ClassForName);
        Method m = c.getDeclaredMethod(Method, parametersTypes);
        Object i = c.newInstance();
        Object r = m.invoke(i, args);
        return r;
    }

    public static Object staticInvokeReturnObject(String ClassForName, String Method, Class[] parametersTypes, Object[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = Class.forName(ClassForName);
        Method m = c.getDeclaredMethod(Method, parametersTypes);
        Object r = m.invoke(null, args);
        return r;
    }

    public static Object staticInvoke(String ClassForName, String Method, Class[] parametersTypes, Object[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class c = Class.forName(ClassForName);
        Method m = c.getDeclaredMethod(Method, parametersTypes);
        Object r = m.invoke(null, args);
        return r;
    }
}
