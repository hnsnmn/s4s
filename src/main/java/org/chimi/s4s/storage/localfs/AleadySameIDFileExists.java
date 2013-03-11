package org.chimi.s4s.storage.localfs;

@SuppressWarnings("serial")
public class AleadySameIDFileExists extends RuntimeException {

	public AleadySameIDFileExists(String msg) {
		super(msg);
	}

}
