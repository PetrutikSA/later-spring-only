package ru.practicum;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

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

        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        // класс Wrapper позволяет задать дополнительные настройки для сервлета
        Wrapper dispatcherWrapper = Tomcat.addServlet(tomcatContext,"dispatcher", dispatcherServlet);
        // addMapping() сопоставляет URL-путь с сервлетом
        dispatcherWrapper.addMapping("/");
        dispatcherWrapper.setLoadOnStartup(1);
        tomcat.start();
    }
}
