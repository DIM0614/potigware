package br.ufrn.dimap.middleware.lifecycle;

import br.ufrn.dimap.middleware.lifecycle.staticexample.NonStaticClassExample;
import br.ufrn.dimap.middleware.lifecycle.staticexample.StaticClassExample;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class for testing separating classes that are annotated with @Static and
 * those who are not.
 * <p>
 * Example: https://marcin-chwedczuk.github.io/creating-and-using-adnotations-in-java
 */
public class StaticUsageExample {

    public static void main(String[] args) {

        List<Class<?>> classes = loadClasses();

        List<StaticData> staticClazzes = Collections.emptyList();

        for (Class<?> clazz : classes) {
            if (!clazz.isAnnotationPresent(Static.class)) {
                continue;
            }

            Static aStatic = clazz.getAnnotation(Static.class);

            staticClazzes.add(new StaticData(clazz, aStatic.priority()));
        }

        Collections.sort(staticClazzes,
            Comparator
                .<StaticData>comparingInt(value -> value.priority)
                .reversed());


        for (StaticData data : staticClazzes) {
            try {
                data.newInstance();
            } catch (ReflectiveOperationException e) {
                System.err.println("Error instantiating class " + data.clazz.getCanonicalName());
//                e.printStackTrace();
            }
        }

    }

    /**
     * Simulates the class loader behavior, should load app classes from package
     * or project.
     *
     * @return a list of classes from package or project
     */
    private static List<Class<?>> loadClasses() {
        List<Class<?>> classes =
            Collections.emptyList();

        classes.add(StaticClassExample.class);
        classes.add(NonStaticClassExample.class);

        return classes;
    }

    /**
     * Encapsulate the static data from classes to order before instantiating.
     */
    private static class StaticData {
        Class<?> clazz;
        int priority;

        public StaticData(Class<?> clazz, int priority) {
            this.clazz = clazz;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public Static newInstance() throws IllegalAccessException, InstantiationException {
            return (Static) clazz.newInstance();
        }
    }

}
