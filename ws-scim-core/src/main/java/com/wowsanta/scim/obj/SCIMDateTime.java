package com.wowsanta.scim.obj;

import java.util.Date;

public class SCIMDateTime extends Date {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1643646530096392106L;

	public SCIMDateTime(long readLong) {
        super(readLong);
    }

    public SCIMDateTime(Date date) {
        super(date.getTime());
    } 
}
