package com.wowsanta.scim.filter;

import org.junit.Test;

public class FilterTest {

	@Test
	public void filter_parser_test() {
        String filter = "(userName eq x and ((groups.value gt xx-yy ) or (id eq y))) or active eq \"true\"";
        FilterNode node = FilterParser.parse( filter );

        System.out.println(node.toString());
        System.out.println(node.getOperator());
     
       if( node instanceof BranchNode ) {
    	   BranchNode bn = ( BranchNode ) node;
    	   
//    	   System.out.println("right : " +bn.getRightNode());
//    	   System.out.println("op : " +bn.getOperator());
//    	   System.out.println("left : " +bn.getLeftNode());
    	   
    	   if( bn.getRightNode() instanceof BranchNode ) {
    		   
    	   }else {
    		   TerminalNode tn = ( TerminalNode ) bn.getRightNode();
    		   
    		   System.out.println("right : " +tn.toString());
    		   
               System.out.println(tn.getAttribute());
               System.out.println(tn.getValue());
               System.out.println(tn.getOperator());
    	   }
    	   
    	   if( bn.getLeftNode() instanceof BranchNode ) {
    		   
    	   }else {
    		   TerminalNode tn = ( TerminalNode ) bn.getLeftNode();
    		   
    		   System.out.println("left : " +tn.toString());
    		   
               System.out.println(tn.getAttribute());
               System.out.println(tn.getValue());
               System.out.println(tn.getOperator());
    	   }
    	   
       }else {
           TerminalNode tn = ( TerminalNode ) node;
           System.out.println(tn.getAttribute());
           System.out.println(tn.getValue());
           System.out.println(tn.getOperator());
       }

        
        

	}
	
}
