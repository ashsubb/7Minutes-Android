package com.zenlabs.sevenminuteworkout.plistparser;


public class False extends PListObject implements IPListSimpleObject<Boolean> {

	private static final long serialVersionUID = -8533886020773567552L;

	public False() {
		setType(PListObjectType.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#getValue()
	 */
	@Override
	public Boolean getValue() {
		return Boolean.valueOf(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.Object)
	 */
	@Override
	public void setValue(Boolean val) {
		// noop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.String)
	 */
	@Override
	public void setValue(String val) {
		// noop
	}

}
