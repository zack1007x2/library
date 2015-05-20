package com.framework.observer;

import java.util.Vector;

public class Doll implements Subject {
	private Vector<Observer> os = new Vector<Observer>();
	private float price;
	public Doll(float price) {
		this.price = price;
		System.out.println("该款娃娃价格初始价格为："+price);
	}
	public void setPrice(float price) {
		this.price = price;
		notifyObservers();  //通知所有观察了该款产品的观察者价格已经发生改变
	}
	@Override
	public void registerObserver(Observer observer) {
		os.add(observer);
	}
	@Override
	public void removeObserver(Observer observer) {
		os.remove(observer);
	}
	@Override
	public void notifyObservers() {
		for(Observer o : os){
			o.update(price);
		}
	}
}
