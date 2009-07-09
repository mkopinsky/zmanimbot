/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package zmanimbot;

import java.io.*;

/**
 *
 */
public class ZmanimCommandLine extends ZmanimMessageListener {
	
	
	public ZmanimCommandLine (String s) {
		parser = new ZmanimParser();
        System.out.println(parse(s,"Command Line"));
	}
	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	protected void log(String s) {
		; //Do nothing, because we don't want log message going into the output
	}
	public static void main(String[] args) {
		String str="";
		for (String s:args)
			str+=s+" ";
		ZmanimCommandLine zcl = new ZmanimCommandLine(str);
	}
	
}
