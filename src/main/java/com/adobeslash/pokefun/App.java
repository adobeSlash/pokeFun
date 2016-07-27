package com.adobeslash.pokefun;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.adobeslash.webservice"})
public class App 
{
	final static Logger logger = Logger.getLogger(App.class);
	
    public static void main( String[] args ) throws InterruptedException
    {
    	SpringApplication.run(App.class, args);    	
    }
}
