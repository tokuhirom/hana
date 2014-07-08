package me.geso.hana;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractStatement {
	final static Logger logger = LoggerFactory.getLogger(HanaSession.class);
	protected final HanaSession session;

	public AbstractStatement(HanaSession hanaSession) {
		this.session = hanaSession;
	}

	// TODO Quote identifiers correctly.
	public String quote(String ident) {
		logger.info(ident);
		return ident;
	}
}