# Replace
Command line utility to replace the filtered AST node from the input table with an instance of the desired new literal given as a flag.

# Getting started
Clone the repository
```
git clone https://github.com/IdrissRio/Replace.git
```

Move into the repository folder

```
cd Replace
```

Run `./gradlew build`. This command will generate a jar file in `app/replace.jar`.

## Example

Let's suppose we have java source code:
```
public class Example{
    String bar(){
        int foo = 0; 
        return "Bar";
    }
}
```

We can replace the filter with the following command:
```java
java -jar replace.jar -replacewith=bar -fix

The output would be the source file with the replaced literal.
```
public class Example{
    String bar(){
        int bar = 0; 
        return "Bar";
    }
}
```

If you remove the parameter ```-fix``` from the command line then you get the output on the terminal. 
