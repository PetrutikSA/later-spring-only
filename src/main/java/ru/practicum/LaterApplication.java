package ru.practicum;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class LaterApplication {
    private final static int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(PORT);
        // то самое «приложение» или «контекст» с пустым путём
        Context tomcatContext = tomcat.addContext("", null);
        AnnotationConfigWebApplicationContext applicationContext =
                new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(tomcatContext.getServletContext());
        applicationContext.scan("ru.practicum");
        applicationContext.refresh();

        // класс Wrapper позволяет задать дополнительные настройки для сервлета
        Wrapper testServletWrapper = Tomcat.addServlet(tomcatContext,"testServlet", new TestServlet());
        // addMapping() сопоставляет URL-путь с сервлетом
        testServletWrapper.addMapping("/test");
        tomcat.start();
    }
}
