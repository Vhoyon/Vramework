package io.github.vhoyon.interfaces;

public interface Auditable extends Outputtable {

	void audit(String auditText, boolean hasAppendedDate, boolean shouldPrependAudit);
	
}
