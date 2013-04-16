//  Reflect.java -- interfaces to reflection functions
//
//  These methods give indirect access to a class that might not be present
//  in all implementations of Java.  They throw various exceptions in case
//  of failure. 



package jcon;

import java.lang.reflect.*;



public final class Reflect {




//  construct(classname, sigs[], args[]) -- instantiate object

static Object construct(String classname, Class<?>[] sigs, Object[] args) 
throws Exception {
    Class<?> c = Class.forName(classname);
    return c.getConstructor(sigs).newInstance(args);
}



//  field(classname, fieldname) -- access static variable

static Field field(String classname, String fieldname)
throws Exception {
    return Class.forName(classname).getDeclaredField(fieldname);
}



//  call(instance, name, sigs[], args[]) -- invoke instance.name(args)

static Object call(Object instance, String name, Class<?>[] sigs, Object[] args)
throws Exception {
    Class<?> c = instance.getClass();
    Method m = c.getDeclaredMethod(name, sigs);
    return m.invoke(instance, args);
}



//  call(classname, methname, sigs[], args[]) -- invoke static method

static Object call(String classname,String name,Class<?>[] sigs,Object[] args)
throws Exception {
    Class<?> c = Class.forName(classname);
    Method m = c.getDeclaredMethod(name, sigs);
    return m.invoke(null, args);
}



} // class Reflect
