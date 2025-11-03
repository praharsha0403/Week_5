import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.*;

//main class that analyzes a given JAR file.
public class week5 {

    public static void main(String[] args) {
        //checked if user provided a JAR file name as an argument.
        if (args.length < 1) {
            System.out.println("Please provide a JAR file name as input.");
            return;
        }

        String jarFileName = args[0]; //stored the jar file name.

        try {
            //opened the given JAR file.
            JarFile jarFile = new JarFile(jarFileName);
            //listed all entries (files/classes) inside the JAR.
            Enumeration<JarEntry> entries = jarFile.entries();

            //used this list to store class names for later sorting.
            List<String> classList = new ArrayList<>();

            //looped through every entry (file) in the JAR file.
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                //checked if entry is a class file and not an inner class.
                if (name.endsWith(".class") && !name.contains("$")) {
                    //converted file path to Java-style class name (package.className).
                    String className = name.replace("/", ".").replace(".class", "");
                    classList.add(className); //added to class list.
                }
            }

            //sorted all class names alphabetically for consistent output.
            Collections.sort(classList);

            //prepared a URLClassLoader to load the JAR classes dynamically.
            URL jarURL = new File(jarFileName).toURI().toURL();
            URL[] urls = new URL[]{ jarURL };
            ClassLoader loader = new URLClassLoader(urls);

            //for each discovered class, tried to load it and inspect it via reflection.
            for (String className : classList) {
                try {
                    //loaded the class using reflection without initializing it.
                    Class<?> cls = Class.forName(className, false, loader);

                    //fetched all declared methods and fields of the class.
                    Method[] methods = cls.getDeclaredMethods();
                    Field[] fields = cls.getDeclaredFields();

                    //counters for method types.
                    int publicCount = 0;
                    int privateCount = 0;
                    int protectedCount = 0;
                    int staticCount = 0;

                    //counted method modifiers.
                    for (Method method : methods) {
                        int modifiers = method.getModifiers();

                        if (Modifier.isPublic(modifiers)) publicCount++;
                        else if (Modifier.isPrivate(modifiers)) privateCount++;
                        else if (Modifier.isProtected(modifiers)) protectedCount++;

                        if (Modifier.isStatic(modifiers)) staticCount++;
                    }

                    //printed summary for each class.
                    System.out.println("---------- " + className + " ----------");
                    System.out.println("  Public methods: " + publicCount);
                    System.out.println("  Private methods: " + privateCount);
                    System.out.println("  Protected methods: " + protectedCount);
                    System.out.println("  Static methods: " + staticCount);
                    System.out.println("  Fields: " + fields.length);

                } catch (Throwable e) {
                    //if class cannot be loaded (dependencies missing, etc.), print a message.
                    System.out.println("Could not load class: " + className);
                    // Uncomment for debugging: e.printStackTrace();
                }
            }

            //closed the JAR file after processing.
            jarFile.close();

        } catch (IOException ex) {
            //handles invalid file or file access errors.
            System.out.println("Failed to read jar file: " + jarFileName);
        }
    }
}