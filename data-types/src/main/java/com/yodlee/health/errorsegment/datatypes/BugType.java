package com.yodlee.health.errorsegment.datatypes;
public enum BugType {

	BUGZILLA(0), SYNTHETIC(1);

	private int id;

	private BugType(int num) {
		this.id = num;
	}

	public int getId() {
		return id;
	}
}