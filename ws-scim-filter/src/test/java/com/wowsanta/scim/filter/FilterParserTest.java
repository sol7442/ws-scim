/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package com.wowsanta.scim.filter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.wowsanta.scim.filter.search.BranchNode;
import com.wowsanta.scim.filter.search.FilterNode;
import com.wowsanta.scim.filter.search.FilterParser;
import com.wowsanta.scim.filter.search.Operator;
import com.wowsanta.scim.filter.search.TerminalNode;

public class FilterParserTest
{
    @Test
    public void testParseSimpleFilter()
    {
        //String filter = "((( x eq \"(y)\" ))) and (y eq \"x\\\"\" )";
        //String filter = "(x eq y) and userName   eq \"bjensen\"";

        String filter = "userName   eq \"bjensen\"";
        FilterNode node = FilterParser.parse( filter );
        
        if (node instanceof BranchNode) {
			BranchNode new_name = (BranchNode) node;
			System.out.println("BranchNode " + new_name);
		}
        
        System.out.println("node " + node);
        
        System.out.println("node hasChilde : " + node.hasChild());
        
        assertNotNull( node );
        assertTrue( node instanceof TerminalNode );
        assertEquals( Operator.EQ, node.getOperator() );
        TerminalNode tn = ( TerminalNode ) node;
        
        assertEquals( tn.getAttribute(), "userName" );
        assertEquals( tn.getValue(), "bjensen" );
    }
    

    @Test
    public void testParseAndFilter()
    {
        String filter = "userName eq x and id gt xx-yy";
        FilterNode node = FilterParser.parse( filter );
        
        System.out.println("node " + node);
        System.out.println("node hasChilde : " + node.hasChild());
        
        assertNotNull( node );
        assertTrue( node instanceof BranchNode );
        assertEquals( Operator.AND, node.getOperator() );
        BranchNode bn = ( BranchNode ) node;
        
        assertEquals( ( ( TerminalNode ) bn.getLeftNode() ) .getAttribute(), "userName" );
        assertEquals( ( ( TerminalNode ) bn.getLeftNode() ) .getValue(), "x" );
        
        assertEquals( ( ( TerminalNode ) bn.getRightNode() ) .getAttribute(), "id" );
        assertEquals( ( ( TerminalNode ) bn.getRightNode() ) .getValue(), "xx-yy" );

        filter = "(userName eq x and ((groups.value gt xx-yy ) or (id eq y))) or active eq \"true\"";
        node = FilterParser.parse( filter );
        System.out.println("node " + node);
        System.out.println("node hasChilde : " + node.hasChild());
        
        BranchNode b_node = (BranchNode)node;
        System.out.println("both : " + b_node.hasBothChildren());
        System.out.println("Left node1 : " + b_node.getLeftNode());
        System.out.println("Left node2 : " + b_node.getLeftNode().getOperator());
        
        assertNotNull( node );
        assertTrue( node instanceof BranchNode );
        assertEquals( Operator.OR, node.getOperator() );
        bn = ( BranchNode ) node;
        
        BranchNode left = ( BranchNode ) bn.getLeftNode();
        assertEquals( ( ( TerminalNode ) left.getLeftNode() ) .getAttribute(), "userName" );
        assertEquals( ( ( TerminalNode ) left.getLeftNode() ) .getValue(), "x" );
        
        BranchNode right = ( BranchNode ) left.getRightNode();
        assertEquals( ( ( TerminalNode ) right.getLeftNode() ) .getAttribute(), "groups.value" );
        assertEquals( ( ( TerminalNode ) right.getLeftNode() ) .getValue(), "xx-yy" );
        
        assertEquals( ( ( TerminalNode ) right.getRightNode() ) .getAttribute(), "id" );
        assertEquals( ( ( TerminalNode ) right.getRightNode() ) .getValue(), "y" );
        
        
        TerminalNode extremeRight = ( TerminalNode ) bn.getRightNode();
        assertEquals( extremeRight.getAttribute(), "active" );
        assertEquals( extremeRight.getValue(), "true" );

    }
    
}
