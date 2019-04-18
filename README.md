# features-javac
A javac plugin for extracting a feature graph for plugging in to machine learning models

## Prerequisite
JDK 1.10+



## How to extrace the graph features

#### Step1: compile
```
 mvn clean compile package
```

#### Step2: to generate .dot and .proto files in below command
``` 
 javac -cp target/features-javac-1.0.0-SNAPSHOT-jar-with-dependencies.jar -Xplugin:FeaturePlugin Source_code 

```

#### Step3: to generate a graph image based on .dot file
```
 dot -Tpng Souce.java.dot > Souce.java.png
```

#### Examples:
The generated [graph](https://github.com/kth-tcs/features-javac/blob/master/examples/example1.java.png) and the [feature nodes](https://github.com/kth-tcs/features-javac/blob/master/examples/example1.feature.node.txt)
```
public class Test {}
```



The generated [graph](https://github.com/kth-tcs/features-javac/blob/master/examples/HANOI.java.png) and the [feature nodes]() of [HANOI.java]() from QuixBugs.
