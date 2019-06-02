package com.yodlee.health.errorsegment.datatypes;
public enum SyntheticBugStatus {
	ACTIVE(0), INACTIVE(2), INVALID(3), CLOSED(4);

	private int id;

	private SyntheticBugStatus(int num) {
		this.id = num;
	}

	public int getId() {
		return id;
	}
}
