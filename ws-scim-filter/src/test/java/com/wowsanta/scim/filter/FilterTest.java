package com.wowsanta.scim.filter;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FilterTest {

	@Parameter(0)
	public String testValidFilterStrings;
	
	public String testInvalidFilterStrings;


	@Parameters
	public static String[] getTestParameters() {
		return new String[]{ "userName Eq \"john\"", "Username eq \"john\"",
			"userName eq \"bjensen\"", "userName co \"jensen\"", "userName sw \"J\"", "title pr",
			"meta.lastModified gt \"2011-05-13T04:42:34Z\"", "meta.lastModified ge \"2011-05-13T04:42:34Z\"",
			"meta.lastModified lt \"2011-05-13T04:42:34Z\"", "meta.lastModified le \"2011-05-13T04:42:34Z\"",
			" title  pr  and  userType  eq  \"Employee\" ", "title pr or userType eq \"Intern\"",
			"userType eq \"Employee\" and " + "(email co \"example.com\" " + "or email co \"example.org\")",
			"userName co \"\\ufe00\\\"\\n\\t\\\\\"", "urn:extension:members eq 25",
			"urn:extension:members eq 25.52", "urn:extension:isActive eq true", "urn:extension:isActive eq false" };
		
		
//		return Arrays.asList(new Object[][] { { "userName Eq \"john\"", "Username eq \"john\"",
//				"userName eq \"bjensen\"", "userName co \"jensen\"", "userName sw \"J\"", "title pr",
//				"meta.lastModified gt \"2011-05-13T04:42:34Z\"", "meta.lastModified ge \"2011-05-13T04:42:34Z\"",
//				"meta.lastModified lt \"2011-05-13T04:42:34Z\"", "meta.lastModified le \"2011-05-13T04:42:34Z\"",
//				" title  pr  and  userType  eq  \"Employee\" ", "title pr or userType eq \"Intern\"",
//				"userType eq \"Employee\" and " + "(email co \"example.com\" " + "or email co \"example.org\")",
//				"userName co \"\\ufe00\\\"\\n\\t\\\\\"", "urn:extension:members eq 25",
//				"urn:extension:members eq 25.52", "urn:extension:isActive eq true", "urn:extension:isActive eq false" },
//				{ "", "(", ")", "()", "foo", "( title pr ) eq ", "username pr \"bjensen\"",
//						"meta.lastModified lte \"2011-05-13T04:42:34Z\"", "username eq",
//						"title pr and userType eq \"Employee\" eq", "userName eq 'bjensen'", "userName eq \"bjensen",
//						"userName eq \"bjensen\\", "userName eq \"\\a\"", "userName eq bjensen",
//						"userName co \"\\ufe\" or userName co \"a\"" }
//
//		});

	}

	@Test
	public void testParseValidFilter() throws Exception {
		System.out.println("testValidFilterStrings : " +  testValidFilterStrings);
		SCIMFilter filter = SCIMFilter.parse(testValidFilterStrings);
		System.out.println("filter : "+ filter);
		
	}
}
