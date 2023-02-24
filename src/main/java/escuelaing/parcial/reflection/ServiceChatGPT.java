package main.java.escuelaing.parcial.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class ServiceChatGPT {

    public ArrayList<String> getResponse(String command, String key) {
        if (Objects.equals(command, "Class")) {
            return getClass(key);
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getClass(String name) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(name);
            for (Method m: c.getMethods()) {
                response.add(m.toString());
            }
            for (Method m: c.getDeclaredMethods()) {
                response.add(m.toString());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        }
        return response;
    }

    public ArrayList<String> getInvoke(String classname, String method, String type, String param) {
        ArrayList<String> response = new ArrayList<>();
        int type_cast = 0;
        if (Objects.equals(type, "int")) {
            type_cast = Integer.parseInt(param);
        }
        try {
            Class c = Class.forName(classname);
            if (Objects.equals(type, "String")) {
                Class[] clas = new Class[]{String.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, param).toString());
            } else if (Objects.equals(type, "int")) {
                Class[] clas = new Class[]{int.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Integer.parseInt(param)).toString());
            } else if (Objects.equals(type, "Double")) {
                Class[] clas = new Class[]{Double.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Double.parseDouble(param)).toString());
            } else {
                response.add("Solo funciona con Parametros de tipo int y String");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
