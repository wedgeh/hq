//
/**
 * NOTE: This copyright does *not* cover user programs that use HQ
 * program services by normal system calls through the application
 * program interfaces provided as part of the Hyperic Plug-in Development
 * Kit or the Hyperic Client Development Kit - this is merely considered
 * normal use of the program, and does *not* fall under the heading of
 *  "derived work".
 *
 *  Copyright (C) [2009-2011], VMware, Inc.
 *  This file is part of HQ.
 *
 *  HQ is free software; you can redistribute it and/or modify
 *  it under the terms version 2 of the GNU General Public License as
 *  published by the Free Software Foundation. This program is distributed
 *  in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *
 */

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.07 at 01:58:59 PM CST 
//


package org.hyperic.hq.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EscalationState complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EscalationState">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ackedBy" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="escalationId" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="nextActionTime" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EscalationState")
public class EscalationState {

    @XmlAttribute(name = "ackedBy", required = true)
    protected String ackedBy;
    @XmlAttribute(name = "escalationId", required = true)
    protected int escalationId;
    @XmlAttribute(name = "nextActionTime", required = true)
    protected long nextActionTime;

    /**
     * Gets the value of the ackedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckedBy() {
        return ackedBy;
    }

    /**
     * Sets the value of the ackedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckedBy(String value) {
        this.ackedBy = value;
    }

    /**
     * Gets the value of the escalationId property.
     * 
     */
    public int getEscalationId() {
        return escalationId;
    }

    /**
     * Sets the value of the escalationId property.
     * 
     */
    public void setEscalationId(int value) {
        this.escalationId = value;
    }

    /**
     * Gets the value of the nextActionTime property.
     * 
     */
    public long getNextActionTime() {
        return nextActionTime;
    }

    /**
     * Sets the value of the nextActionTime property.
     * 
     */
    public void setNextActionTime(long value) {
        this.nextActionTime = value;
    }

}
