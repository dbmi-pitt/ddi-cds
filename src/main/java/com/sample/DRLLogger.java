package com.sample;

import org.kie.api.runtime.rule.RuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DRLLogger {

	private DRLLogger() {
	}

	protected static Logger getLogger(final RuleContext drools) {
	    final String category = drools.getRule().getPackageName() + "." + drools.getRule().getName();
	    final Logger logger = LoggerFactory.getLogger(category);
	    return logger;
	}

	public static void info(final RuleContext drools, final String message, final Object... parameters) {
	    final Logger logger = getLogger(drools);
	    logger.info(message, parameters);
	}

	public static void debug(final RuleContext drools, final String message, final Object... parameters) {
	    final Logger logger = getLogger(drools);
	    logger.debug(message, parameters);
	}

	public static void error(final RuleContext drools, final String message, final Object... parameters) {
	    final Logger logger = getLogger(drools);
	    logger.error(message, parameters);
	}
}
