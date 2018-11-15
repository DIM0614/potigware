package br.ufrn.dimap.middleware.lifecycle;

import br.ufrn.dimap.middleware.lifecycle.statictest.NonStaticClassExample;
import br.ufrn.dimap.middleware.lifecycle.statictest.StaticClassExample;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Class for testing separating classes that are annotated with @Static and those who are not.
 * <p>
 * Example: https://marcin-chwedczuk.github.io/creating-and-using-adnotations-in-java
 */
public class StaticTest {

    public static void main(String[] args) {

        List<Class<?>> classes =
            Collections.emptyList();

        classes.add(StaticClassExample.class);
        classes.add(NonStaticClassExample.class);

        List<StaticData> staticClazzes =
//        List<Static> staticClazzes =
            Collections.emptyList();

        for (Class<?> clazz : classes) {
            Static aStatic = clazz.getAnnotation(Static.class);

            if (aStatic == null) {
                continue;
            }

            Object instance;
            Method method;
            try {
                instance = clazz.newInstance();
                method = clazz.getMethod(aStatic.method());

                staticClazzes.add(new StaticData(instance, method, aStatic.priority()));

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (ReflectiveOperationException e) {

            }

        }

        Collections.sort(staticClazzes,
            Comparator
                .<StaticData>comparingInt(value -> value.priority)
                .reversed());


        for (StaticData data : staticClazzes) {
            try {
                data.invokeMethod();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }


    }

    private static class StaticData {
        Object object;
        Method method;
        int priority;

        public StaticData(Object object, Method method, int priority) {
            this.object = object;
            this.method = method;
            this.priority = priority;
        }

        public void invokeMethod() throws InvocationTargetException, IllegalAccessException {
            method.invoke(object);
        }

        public int getPriority() {
            return priority;
        }
    }

}
