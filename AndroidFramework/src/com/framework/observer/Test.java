package com.framework.observer;
public class Test {
	public static void main(String[] args) {
		Doll doll = new Doll(3000);
		Person p1 = new Person("小白");
		Person p2 = new Person("小黑");
		
		doll.registerObserver(p1);
		doll.registerObserver(p2);
		System.out.println("第一次促销");
		doll.setPrice(2698);
		System.out.println("第二次促销");
		doll.setPrice(2299);
		doll.removeObserver(p2);
		System.out.println("第三次促销");
		doll.setPrice(1998);
	}
}
