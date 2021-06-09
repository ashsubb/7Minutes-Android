package com.zenlabs.sevenminuteworkout.plistparser;

import java.io.Serializable;

public class PListObject extends Object implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5258056855425643835L;

	private PListObjectType type;

	/**
	 * @return the type
	 */
	public PListObjectType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(PListObjectType type) {
		this.type = type;
	}

}
