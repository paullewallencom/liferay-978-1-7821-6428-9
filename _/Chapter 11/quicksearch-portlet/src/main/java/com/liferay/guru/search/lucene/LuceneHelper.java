package com.liferay.guru.search.lucene;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;

public class LuceneHelper {
	public static Hits search(SearchContext searchContext)
			throws SearchException, ParseException {
		
		String keywords = searchContext.getKeywords();
		keywords += "*";

		BooleanQuery booleanQuery = BooleanQueryFactoryUtil
				.create(searchContext);
		booleanQuery.addTerm(Field.TITLE, keywords);
		booleanQuery.addTerm(Field.DESCRIPTION, keywords);

		Hits hits = SearchEngineUtil.search(searchContext, booleanQuery);
		return hits;
	}
}
