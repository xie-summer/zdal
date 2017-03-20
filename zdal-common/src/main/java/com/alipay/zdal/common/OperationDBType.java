/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common;

/**
 * @author added by fanzeng
 *         增加这个枚举类型的目的是，在进行读写重试的时候，根据read或者是write的类型来进行判定，如果是读，可以进行
 *         读重试，而如果是写，只有PriorityDbGroupSelector的时候才可以进行写重试，即优先写p0，如果写p0失败后重试写p1；
 */
public enum OperationDBType {
	readFromDb(0), writeIntoDb(1);
	private int i;

	private OperationDBType(int i) {
		this.i = i;
	}

	public int value() {
		return this.i;
	}

	public static OperationDBType valueOf(int i) {
		for (OperationDBType t : values()) {
			if (t.value() == i) {
				return t;
			}
		}
		throw new IllegalArgumentException("Invalid operationDBType:" + i);
	}
}
