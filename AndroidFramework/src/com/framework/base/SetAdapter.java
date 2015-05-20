package com.framework.base;

import java.util.Set;
/**
 * 支持Set集合的适配器
 * @author lee
 * @param <D>
 */
public abstract class SetAdapter<D> extends ArrayAdapter<D> {
	public SetAdapter(Set<D> set) {
		super((D[]) set.toArray());
	}
}
