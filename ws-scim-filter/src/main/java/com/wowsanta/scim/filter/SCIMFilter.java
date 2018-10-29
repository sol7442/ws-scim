package com.wowsanta.scim.filter;

import java.util.ArrayList;
import java.util.List;

import com.wession.scim.Const;
import com.wowsanta.scim.exception.SCIMException;

public class SCIMFilter {

	private final SCIMFilterType filterType;
	private final AttributePath filterAttribute;
	private final String filterValue;
	private final boolean quoteFilterValue;
	private final List<SCIMFilter> filterComponents;

	public SCIMFilter(final SCIMFilterType filterType, final AttributePath filterAttribute, final String filterValue,
			final boolean quoteFilterValue, final List<SCIMFilter> filterComponents) {
		this.filterType = filterType;
		this.filterAttribute = filterAttribute;
		this.filterValue = filterValue;
		this.quoteFilterValue = quoteFilterValue;
		this.filterComponents = filterComponents;
	}

	public static SCIMFilter parse(final String filterString) throws SCIMException {
		final FilterParser2 parser = new FilterParser2(filterString, Const.schemas);
		return parser.parse();
	}

	public static SCIMFilter createAndFilter(final List<SCIMFilter> filterComponents) {
		return new SCIMFilter(SCIMFilterType.AND, null, null, false, new ArrayList<SCIMFilter>(filterComponents));
	}

	public static SCIMFilter createOrFilter(final List<SCIMFilter> filterComponents) {
		return new SCIMFilter(SCIMFilterType.OR, null, null, false, new ArrayList<SCIMFilter>(filterComponents));
	}

	public static SCIMFilter createEqualityFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.EQUALITY, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createContainsFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.CONTAINS, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createStartsWithFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.STARTS_WITH, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createPresenceFilter(final AttributePath filterAttribute) {
		return new SCIMFilter(SCIMFilterType.PRESENCE, filterAttribute, null, true, null);
	}

	public static SCIMFilter createGreaterThanFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.GREATER_THAN, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createGreaterOrEqualFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.GREATER_OR_EQUAL, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createLessThanFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.LESS_THAN, filterAttribute, filterValue, true, null);
	}

	public static SCIMFilter createLessOrEqualFilter(final AttributePath filterAttribute, final String filterValue) {
		return new SCIMFilter(SCIMFilterType.LESS_OR_EQUAL, filterAttribute, filterValue, true, null);
	}

	public SCIMFilterType getFilterType() {
		return filterType;
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		toString(builder);
		return builder.toString();
	}

	/**
	 * Append the string representation of the filter to the provided buffer.
	 *
	 * @param builder The buffer to which the string representation of the filter is
	 *                to be appended.
	 */
	public void toString(final StringBuilder builder) {
		switch (filterType) {
		case AND:
		case OR:
			builder.append('(');

			for (int i = 0; i < filterComponents.size(); i++) {
				if (i != 0) {
					builder.append(' ');
					builder.append(filterType);
					builder.append(' ');
				}

				builder.append(filterComponents.get(i));
			}

			builder.append(')');
			break;

		case EQUALITY:
		case CONTAINS:
		case STARTS_WITH:
		case GREATER_THAN:
		case GREATER_OR_EQUAL:
		case LESS_THAN:
		case LESS_OR_EQUAL:
			builder.append(filterAttribute);
			builder.append(' ');
			builder.append(filterType);
			builder.append(' ');

			if (quoteFilterValue) {
				builder.append(filterValue);

			} else {
				builder.append(filterValue);
			}
			break;

		case PRESENCE:
			builder.append(filterAttribute);
			builder.append(' ');
			builder.append(filterType);
			break;
		}
	}
}
