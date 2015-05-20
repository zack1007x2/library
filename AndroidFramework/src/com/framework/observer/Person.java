package com.framework.observer;

public class Person implements Observer {
	private String name;
	
	public Person(String name) {
		this.name = name;
	}

	@Override
	public void update(float price) {
		System.out.println(name+"关注的娃娃的价格已经更新为："+price+"Ԫ");
	}

}
