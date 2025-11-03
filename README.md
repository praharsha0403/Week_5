# Week5: Java Reflection on JAR Files

### Program: JarClasses.java

This program reads a .jar file, extracts the classes inside it, and for each class, uses Java Reflection to list:

- Number of declared public, private, and protected methods
- Number of static methods
- Number of declared fields

The output is sorted alphabetically by class name.

#### Example Output

```
$ java JarClasses json-20231013.jar

----------org.json.JSONArray----------
  Public methods: 72
  Private methods: 5
  Protected methods: 0
  Static methods: 2
  Fields: 1

----------org.json.JSONObject----------
  Public methods: 78
  Private methods: 11
  Protected methods: 3
  Static methods: 27
  Fields: 3

... etc.

```

#### How to Compile and Run

```
javac week5.java
java -cp .:json-20231013.jar week5 json-20231013.jar
```

#### On Windows, replace : with ; in the `-cp` classpath separator:

```
java -cp .;json-20231013.jar JarClasses json-20231013.jar
```
