package br.ufrn.dimap.middleware.remotting.interfaces;

/**
 * Lists the invocation asynchrony patterns,
 * except for the Callback Result, which is
 * treated separately
 * @author victoragnez
 *
 */
public enum InvocationAsynchronyPattern {
	FIRE_AND_FORGET,
	SYNC_WITH_SERVER,
	POLL_OBJECT
}
