package com.wowsanta.scim.filter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.wowsanta.scim.exception.SCIMException;


public class FilterParser {
	private final String filterString;
	private final String defaultSchema;
	private int endPos;
	private int currentPos;
	private int markPos;

	class Node {
		private final int pos;

		public Node(final int pos) {
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}
	}

	class FilterNode extends Node {
		private final SCIMFilter filterComponent;

		public FilterNode(final SCIMFilter filterComponent, final int pos) {
			super(pos);
			this.filterComponent = filterComponent;
		}

		public SCIMFilter getFilterComponent() {
			return filterComponent;
		}

		public String toString() {
			return "FilterNode{" + "filterComponent=" + filterComponent + "} " + super.toString();
		}
	}

	class OperatorNode extends Node {
		private final SCIMFilterType filterType;

		public OperatorNode(final SCIMFilterType filterType, final int pos) {
			super(pos);
			this.filterType = filterType;
		}

		public SCIMFilterType getFilterType() {
			return filterType;
		}

		public int getPrecedence() {
			switch (filterType) {
			case AND:
				return 2;

			case OR:
			default:
				return 1;
			}
		}

		public String toString() {
			return "OperatorNode{" + "filterType=" + filterType + "} " + super.toString();
		}
	}

	class LeftParenthesisNode extends Node {
		public LeftParenthesisNode(final int pos) {
			super(pos);
		}
	}

	public FilterParser(final String filterString, final String defaultSchema) {
		this.filterString = filterString;
		this.endPos = filterString.length();
		this.currentPos = 0;
		this.markPos = 0;
		this.defaultSchema = defaultSchema;
	}

	public SCIMFilter parse() throws SCIMException {
		try {
			return readFilter();
		} catch (Exception e) {
			throw new SCIMException();
		}
	}
	
	 private SCIMFilter readFilter()
	  {
	    final Stack<Node> expressionStack = new Stack<Node>();

	    // Employ the shunting-yard algorithm to parse into reverse polish notation,
	    // where the operands are filter components and the operators are the
	    // logical AND and OR operators. This algorithm ensures that operator
	    // precedence and parentheses are respected.
	    final List<Node> reversePolish = new ArrayList<Node>();
	    for (String word = readWord(); word != null; word = readWord())
	    {
	      if (word.equalsIgnoreCase("and") || word.equalsIgnoreCase("or"))
	      {
	        final OperatorNode currentOperator;
	        if (word.equalsIgnoreCase("and"))
	        {
	          currentOperator = new OperatorNode(SCIMFilterType.AND, markPos);
	        }
	        else
	        {
	          currentOperator = new OperatorNode(SCIMFilterType.OR, markPos);
	        }
	        while (!expressionStack.empty() &&
	               (expressionStack.peek() instanceof OperatorNode))
	        {
	          final OperatorNode previousOperator =
	              (OperatorNode)expressionStack.peek();
	          if (previousOperator.getPrecedence() <
	              currentOperator.getPrecedence())
	          {
	            break;
	          }
	          reversePolish.add(expressionStack.pop());
	        }
	        expressionStack.push(currentOperator);
	      }
	      else if (word.equals("("))
	      {
	        expressionStack.push(new LeftParenthesisNode(markPos));
	      }
	      else if (word.equals(")"))
	      {
	        while (!expressionStack.empty() &&
	               !(expressionStack.peek() instanceof LeftParenthesisNode))
	        {
	          reversePolish.add(expressionStack.pop());
	        }
	        if (expressionStack.empty())
	        {
	          final String msg =
	              String.format("No opening parenthesis matching closing " +
	                            "parenthesis at position %d", markPos);
	          throw new IllegalArgumentException(msg);
	        }
	        expressionStack.pop();
	      }
	      else
	      {
	        rewind();
	        final int pos = currentPos;
	        final SCIMFilter filterComponent = readFilterComponent();
	        reversePolish.add(new FilterNode(filterComponent, pos));
	      }
	    }

	    while  (!expressionStack.empty())
	    {
	      final Node node = expressionStack.pop();
	      if (node instanceof LeftParenthesisNode)
	      {
	        final String msg =
	            String.format("No closing parenthesis matching opening " +
	                          "parenthesis at position %d", node.getPos());
	        throw new IllegalArgumentException(msg);
	      }
	      reversePolish.add(node);
	    }

	    // Evaluate the reverse polish notation to create a single complex filter.
	    final Stack<FilterNode> filterStack = new Stack<FilterNode>();
	    for (final Node node : reversePolish)
	    {
	      if (node instanceof OperatorNode)
	      {
	        final FilterNode rightOperand = filterStack.pop();
	        final FilterNode leftOperand = filterStack.pop();

	        final OperatorNode operatorNode = (OperatorNode)node;
	        if (operatorNode.getFilterType().equals(SCIMFilterType.AND))
	        {
	          final SCIMFilter filter = SCIMFilter.createAndFilter(
	              Arrays.asList(leftOperand.getFilterComponent(),
	                            rightOperand.getFilterComponent()));
	          filterStack.push(new FilterNode(filter, leftOperand.getPos()));
	        }
	        else
	        {
	          final SCIMFilter filter = SCIMFilter.createOrFilter(
	              Arrays.asList(leftOperand.getFilterComponent(),
	                            rightOperand.getFilterComponent()));
	          filterStack.push(new FilterNode(filter, leftOperand.getPos()));
	        }
	      }
	      else
	      {
	        filterStack.push((FilterNode)node);
	      }
	    }

	    if (filterStack.size() == 0)
	    {
	      final String msg = String.format("Empty filter expression");
	      throw new IllegalArgumentException(msg);
	    }
	    else if (filterStack.size() > 1)
	    {
	      final String msg = String.format(
	          "Unexpected characters at position %d", expressionStack.get(1).pos);
	      throw new IllegalArgumentException(msg);
	    }

	    return filterStack.get(0).filterComponent;
	  }
	 
	 private String readWord()
	  {
	    skipWhitespace();
	    markPos = currentPos;

	    loop:
	    while (currentPos < endPos)
	    {
	      final char c = filterString.charAt(currentPos);
	      switch (c)
	      {
	        case '(':
	        case ')':
	          if (currentPos == markPos)
	          {
	            currentPos++;
	          }
	          break loop;

	        case ' ':
	          break loop;

	        default:
	          currentPos++;
	          break;
	      }
	    }

	    if (currentPos - markPos == 0)
	    {
	      return null;
	    }

	    final String word = filterString.substring(markPos, currentPos);

	    skipWhitespace();
	    return word;
	  }
	 
	 private SCIMFilter readFilterComponent()
	  {
	    String word = readWord();
	    if (word == null)
	    {
	      final String msg = String.format(
	          "End of input at position %d but expected a filter expression",
	          markPos);
	      throw new IllegalArgumentException(msg);
	    }

	    final AttributePath filterAttribute;
	    try
	    {
	      filterAttribute = AttributePath.parse(word, defaultSchema);
	    }
	    catch (final Exception e)
	    {
	      Debug.debugException(e);
	      final String msg = String.format(
	          "Expected an attribute reference at position %d: %s",
	          markPos, e.getMessage());
	      throw new IllegalArgumentException(msg);
	    }

	    final String operator = readWord();
	    if (operator == null)
	    {
	      final String msg = String.format(
	          "End of input at position %d but expected an attribute operator",
	          markPos);
	      throw new IllegalArgumentException(msg);
	    }

	    final SCIMFilterType filterType;
	    if (operator.equalsIgnoreCase("eq"))
	    {
	      filterType = SCIMFilterType.EQUALITY;
	    }
	    else if (operator.equalsIgnoreCase("co"))
	    {
	      filterType = SCIMFilterType.CONTAINS;
	    }
	    else if (operator.equalsIgnoreCase("sw"))
	    {
	      filterType = SCIMFilterType.STARTS_WITH;
	    }
	    else if (operator.equalsIgnoreCase("pr"))
	    {
	      filterType = SCIMFilterType.PRESENCE;
	    }
	    else if (operator.equalsIgnoreCase("gt"))
	    {
	      filterType = SCIMFilterType.GREATER_THAN;
	    }
	    else if (operator.equalsIgnoreCase("ge"))
	    {
	      filterType = SCIMFilterType.GREATER_OR_EQUAL;
	    }
	    else if (operator.equalsIgnoreCase("lt"))
	    {
	      filterType = SCIMFilterType.LESS_THAN;
	    }
	    else if (operator.equalsIgnoreCase("le"))
	    {
	      filterType = SCIMFilterType.LESS_OR_EQUAL;
	    }
	    else
	    {
	      final String msg = String.format(
	          "Unrecognized attribute operator '%s' at position %d. " +
	          "Expected: eq,co,sw,pr,gt,ge,lt,le", operator, markPos);
	      throw new IllegalArgumentException(msg);
	    }

	    final Object filterValue;
	    if (!filterType.equals(SCIMFilterType.PRESENCE))
	    {
	      filterValue = readValue();
	      if (filterValue == null)
	      {
	        final String msg = String.format(
	            "End of input at position %d while expecting a value for " +
	            "operator %s", markPos, operator);
	        throw new IllegalArgumentException(msg);
	      }
	    }
	    else
	    {
	      filterValue = null;
	    }
	    final String filterValueString = (filterValue != null) ?
	        filterValue.toString() : null;
	    return new SCIMFilter(
	        filterType, filterAttribute,
	        filterValueString,
	        (filterValue != null) && (filterValue instanceof String),
	        null);
	  }
	  private void rewind()
	  {
	    currentPos = markPos;
	  }
}
