package ru.practicum;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class LaterApplication {
    private final static int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.start();
    }
}
