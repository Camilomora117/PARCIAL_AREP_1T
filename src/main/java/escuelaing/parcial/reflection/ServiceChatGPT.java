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


    public ArrayList<String> getInvoke(String classname, String method) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            Method m = c.getDeclaredMethod(method,  null);
            response.add(m.invoke(null).toString());
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    public ArrayList<String> getUnaryInvoke(String classname, String method, String type, String param) {
        ArrayList<String> response = new ArrayList<>();
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
            } else if (Objects.equals(type, "double")) {
                Class[] clas = new Class[]{Double.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Double.parseDouble(param)).toString());
            } else {
                response.add("Solo funciona con Parametros de tipo int y String y Double");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public ArrayList<String> getBinaryInvoke(String classname, String method, String type1, String param1, String type2, String param2) {
        ArrayList<String> response = new ArrayList<>();
        try {
            Class c = Class.forName(classname);
            if (Objects.equals(type1, "String")) {
                Class[] clas = new Class[]{String.class, String.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, param1, param2).toString());
            } else if (Objects.equals(type1, "int")) {
                Class[] clas = new Class[]{int.class, int.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Integer.parseInt(param1),Integer.parseInt(param2)).toString());
            } else if (Objects.equals(type1, "double")) {
                Class[] clas = new Class[]{Double.class, Double.class};
                Method m = c.getDeclaredMethod(method,  clas);
                response.add(m.invoke(null, Double.parseDouble(param1), Double.parseDouble(param2)).toString());
            } else {
                response.add("Solo funciona con Parametros de tipo int y String y Double");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
