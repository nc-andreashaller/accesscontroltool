/*
 * (C) Copyright 2015 Netcentric AG.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package biz.netcentric.cq.tools.actool.helper;

import java.security.Principal;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.security.Privilege;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlEntry;

/**
 * Wraps an {@link JackrabbitAccessControlEntry} and stores an
 * additional path information. Created and used during the reading of ACEs from a system, in order
 * to create an ACE Dump.
 * 
 * @author jochenkoschorke
 *
 */
public class AceWrapper implements JackrabbitAccessControlEntry {
    private JackrabbitAccessControlEntry ace;
    private String jcrPath;

    public AceWrapper(JackrabbitAccessControlEntry ace, String jcrPath) {
        super();
        this.ace = ace;
        this.jcrPath = jcrPath;
    }

    public String getJcrPath() {
        return jcrPath;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ace == null) ? 0 : ace.hashCode());
		result = prime * result + ((jcrPath == null) ? 0 : jcrPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AceWrapper other = (AceWrapper) obj;
		if (ace == null) {
			if (other.ace != null)
				return false;
		} else if (!ace.equals(other.ace))
			return false;
		if (jcrPath == null) {
			if (other.jcrPath != null)
				return false;
		} else if (!jcrPath.equals(other.jcrPath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AceWrapper [ace=" + ace + ", jcrPath=" + jcrPath + "]";
	}

	public String getRestrictionAsString(String name)
            throws RepositoryException {
        Value val = ((JackrabbitAccessControlEntry) ace).getRestriction(name);
        if (val != null) {
            return ((JackrabbitAccessControlEntry) ace).getRestriction(name)
                    .getString();
        }
        // fix for #11: All ACE from groups not contained in a config get an empty repGlob after installation
        return null;
    }

    public String getPrivilegesString() {
        Privilege[] privileges = this.ace.getPrivileges();
        String privilegesString = "";
        for (Privilege privilege : privileges) {
            privilegesString = privilegesString + privilege.getName() + ",";
        }
        privilegesString = StringUtils.chop(privilegesString);
        return privilegesString;
    }

	@Override
	public Principal getPrincipal() {
		return ace.getPrincipal();
	}

	@Override
	public Privilege[] getPrivileges() {
		return ace.getPrivileges();
	}

	@Override
	public boolean isAllow() {
		return ace.isAllow();
	}

	@Override
	public String[] getRestrictionNames() throws RepositoryException {
		return ace.getRestrictionNames();
	}

	@Override
	public Value getRestriction(String restrictionName)
			throws ValueFormatException, RepositoryException {
		return ace.getRestriction(restrictionName);
	}

	@Override
	public Value[] getRestrictions(String restrictionName)
			throws RepositoryException {
		return ace.getRestrictions(restrictionName);
	}

}
