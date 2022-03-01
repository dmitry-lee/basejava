package com.dmitrylee.webapp;

import com.dmitrylee.webapp.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Resume resume = new Resume("test");
        Method toString = resume.getClass().getDeclaredMethod("toString");
        System.out.println(toString.invoke(resume));
    }
}
