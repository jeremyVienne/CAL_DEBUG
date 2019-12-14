authors : Irakoze Franco Davy / Vienne Jeremy

## Java JDI
This module contains articles about JDI, the Java Debug Interface.

https://kloum.io/costiou/teaching/materials/practicals/debugging_TP_3.pdf

### Relevant articles

- [An Intro to the Java Debug Interface (JDI)](https://www.baeldung.com/java-debug-interface)


javac -g -cp "/usr/lib/jvm/jdk1.8.0_212/lib/tools.jar" *.java

java -cp "/usr/lib/jvm/jdk1.8.0_212/lib/tools.jar:." JDIExampleDebugger

Commandes implémentées: 

- step (step in the code)
- stack (print the stack)
- arguments (print arguments of the method running)
- break (set breakpoints)
- breakpoints (print all breakpoints setted)
- method (print the method running)
- replay (active le mode trace)
- replayoff (desactive le mode trace)



**En mode trace**
- next 
- previous