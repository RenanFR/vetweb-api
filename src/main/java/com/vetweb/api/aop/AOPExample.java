package com.vetweb.api.aop;

import org.springframework.aop.framework.ProxyFactory;

public class AOPExample {
	
	public static void main(String[] args) {
		
		Person person = new Person();//Target object
		ProxyFactory proxyFactory = new ProxyFactory();//Creates the proxy for original object and do the weaving of the advice
		proxyFactory.addAdvice(new MethodLoggerAdvice());//Which advice
		proxyFactory.addAdvice(new MethodTimerAdvice());//Which advice
		proxyFactory.setTarget(person);//Target object which methods will be intercepted
		
		Person proxy = (Person) proxyFactory.getProxy();//Retrieves the proxied version of the original target
		proxy.talk();//Original method with advice behavior
		
	}

}

class Person {
	void talk() {
		System.out.println("Hello World");
	}
}
