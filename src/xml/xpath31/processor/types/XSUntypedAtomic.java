/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau                    - initial API and implementation
 *     Mukul Gandhi                     - source code reused and adapted for use 
 *                                        with XalanJ's XPath 3.1 processor.
 *******************************************************************************/

package xml.xpath31.processor.types;

import javax.xml.transform.TransformerException;

import org.apache.xalan.xslt.util.XslTransformEvaluationHelper;
import org.apache.xpath.XPathCollationSupport;
import org.apache.xpath.objects.ResultSequence;
import org.apache.xpath.objects.XObject;

/**
 * This class represents an XML Schema data type xs:untypedAtomic.
 *  
 * As per XPath 3.1 spec, xs:untypedAtomic is an XML Schema data type 
 * that is used to denote untyped atomic data, such as text that has not 
 * been assigned a more specific type.
 */
public class XSUntypedAtomic extends XSCtrType {

    private static final long serialVersionUID = 3034074443706977457L;
    
    private static final String XS_UNTYPED_ATOMIC = "xs:untypedAtomic";
    
    private String _value;
    
    public XSUntypedAtomic() {
        this(null);
    }
    
    public XSUntypedAtomic(String str) {
        _value = str;
    }

    @Override
    public ResultSequence constructor(ResultSequence arg) {
        ResultSequence resultSeq = new ResultSequence();
        
        if (arg.size() == 0) {
           return resultSeq;     
        }
        
        XSAnyAtomicType xsAnyAtomicType = (XSAnyAtomicType)arg.item(0);
        
        resultSeq.add(new XSUntypedAtomic(xsAnyAtomicType.stringValue()));
        
        return resultSeq;
    }

    @Override
    public String typeName() {
        return "untypedAtomic";
    }

    @Override
    public String stringType() {
        return XS_UNTYPED_ATOMIC;
    }

    @Override
    public String stringValue() {
        return _value;
    }
    
    /*
     * Check equality between this XSUntypedAtomic value and an XObject value.  
     */
    public boolean equals(XObject xObject) {
        boolean isEquals = false;
        
        if (xObject instanceof XSUntypedAtomic) {
           isEquals = _value.equals(((XSUntypedAtomic)xObject).stringValue()); 
        }
        else if (xObject instanceof XSUntyped) {
           isEquals = _value.equals(((XSUntyped)xObject).stringValue());  
        }
        else {
           isEquals = _value.equals(XslTransformEvaluationHelper.getStrVal(xObject)); 
        }
        
        return isEquals;
    }
    
    /*
     * Check equality between this XSUntypedAtomic value and an XObject value, considering
     * collation for string comparison. 
     */
    public boolean equals(XObject xObject, String collationUri, XPathCollationSupport xpathCollationSupport) 
    		                                                                        throws TransformerException {
        boolean isEquals = false;
        
        if (collationUri == null) {
        	isEquals = equals(xObject);  	
        }
        else {
	        if (xObject instanceof XSUntypedAtomic) {
	           int strComparisonResult = xpathCollationSupport.compareStringsUsingCollation(_value, 
                                                                               ((XSUntypedAtomic)xObject).stringValue(), collationUri);
               if (strComparisonResult == 0) {
                  isEquals = true; 
               } 
	        }
	        else if (xObject instanceof XSUntyped) {
	           int strComparisonResult = xpathCollationSupport.compareStringsUsingCollation(_value, 
                                                                                 ((XSUntyped)xObject).stringValue(), collationUri);
               if (strComparisonResult == 0) {
                   isEquals = true; 
               }  
	        }
	        else {
	           int strComparisonResult = xpathCollationSupport.compareStringsUsingCollation(_value, 
	        		                                                            XslTransformEvaluationHelper.getStrVal(xObject), collationUri);
               if (strComparisonResult == 0) {
                  isEquals = true; 
               }
	        }
        }
        
        return isEquals;
    }

}
