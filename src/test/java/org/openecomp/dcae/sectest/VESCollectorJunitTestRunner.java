/*-
 * ============LICENSE_START=======================================================
 * PROJECT
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

/*
 * Purpose: CommonCollectorJunitTestRunner is the main class where test suit execution starts its test cases execution
 * the common collector test suit has been written in order to incorporate functional and logical testing of collector features
 * 
 */

package org.openecomp.dcae.sectest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.openecomp.dcae.commonFunction.CommonStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VESCollectorJunitTestRunner {

	public static void main(String[] args) {

		System.out.println("STARTING TEST SUITE EXECUTION.....");
		
		Result result = JUnitCore.runClasses(VESCollectorJunitTest.class);

		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		
		System.out.println("Execution Final result : "+result.wasSuccessful());
	}
	
}
