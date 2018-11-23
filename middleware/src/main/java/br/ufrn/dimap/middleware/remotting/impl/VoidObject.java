/**
 * 
 */
package br.ufrn.dimap.middleware.remotting.impl;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object created to wrap null returns and make it possible to marshal
 * 
 * @author victoragnez
 *
 */
@XmlRootElement(name = "VoidObject")
public final class VoidObject {
	public VoidObject() {}
}
