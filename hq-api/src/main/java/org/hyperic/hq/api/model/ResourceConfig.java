/* **********************************************************************
/*
 * NOTE: This copyright does *not* cover user programs that use Hyperic
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 * "derived work".
 *
 * Copyright (C) [2004-2010], VMware, Inc.
 * This file is part of Hyperic.
 *
 * Hyperic is free software; you can redistribute it and/or modify
 * it under the terms version 2 of the GNU General Public License as
 * published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */
package org.hyperic.hq.api.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
 
@XmlType(name="ResourceConfigType", namespace=RestApiConstants.SCHEMA_NAMESPACE)
public class ResourceConfig implements Serializable{
	
	private static final long serialVersionUID = 8233944180632888593L;
	
	private String resourceID; 
	private Map<String,String> mapProps ; 
	
	public ResourceConfig() {}//EOM
	
	public ResourceConfig(final String resourceID, final Map<String,String> mapProps) { 
		this.resourceID = resourceID ; 
		this.mapProps = mapProps ; 
	}//EOM 
	
	public final void setResourceID(final String resourceID) { 
		this.resourceID = resourceID ; 
	}//EOM 
	
	public final String getResourceID() { 
		return this.resourceID ; 
	}//EOM 
	
	public final void setMapProps(final Map<String,String> configValues) { 
		this.mapProps= configValues ; 
	}//EOM 
	
	public final Map<String,String> getMapProps() { 
		return this.mapProps ; 
	}//EOM 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resourceID == null) ? 0 : resourceID.hashCode());
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
		ResourceConfig other = (ResourceConfig) obj;
		if (resourceID == null) {
			if (other.resourceID != null)
				return false;
		} else if (!resourceID.equals(other.resourceID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder() ; 
		return this.toString(builder, "").toString() ; 
	}//EOM 
	
	public final StringBuilder toString(final StringBuilder builder, final String indentation) { 
		return builder.append(indentation).append("ResourceConfig [resourceID=").append(resourceID).append("\n").append(indentation).append(", mapProps=").
			append(mapProps.toString().replaceAll(",",  "\n"+indentation + " ")).append("]") ; 
	}//EOM 
	
}//EOC 