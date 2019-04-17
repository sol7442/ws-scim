package com.wowsanta.scim.repository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.wowsanta.scim.util.Random;

public class Paginator {

	@Test
	public void test() {
		pagination(100);
		
	}
	
	public void pagination(int count) {
		while(count > 0) {
			int total_count  = Integer.parseInt(Random.number(100,10000));
			int page_count   = Integer.parseInt(Random.number(5,50));
			int page_index   = Integer.parseInt(Random.number(0,100));
					
			int total_page = total_count / page_count;
			if (total_count % page_count > 0) {
				total_page++;
			}
			
			int start_index = page_count * page_index + 1;
			int end_index   = page_count * (page_index + 1) + 1;
			
			System.out.println("["+count+"]" +"["+total_count+"]" + "["+page_count+"]"  + "["+page_index+"]"  + "["+total_page+"]"  + "["+start_index+"]" + "["+end_index+"]");

			assertTrue("test...",  (end_index - start_index) != page_count);
			//assertEquals(message, expected, actual);
			//assertArrayEquals("", expecteds, actuals);
			//True("test...", (end_index - start_index) != page_count);

			count--;
		}
	}
}
