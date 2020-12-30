package com.liferay.guru.search.lucene;

import java.util.Objects;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;

public class AutocompleteListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {

		Message response = MessageBusUtil.createResponseMessage( message );
		
		Object payload = message.getPayload();
        if ( payload instanceof SearchContext ) {
            response.setPayload( getSuggestions( (SearchContext) payload ) );
        }
        else {
            _log.error( "Message payload is invalid: " + Objects.toString( payload ) );
            response.setPayload( JSONFactoryUtil.createJSONArray() );
        }
		
		MessageBusUtil.sendMessage( response.getDestinationName(), response );
		
		
	}
	
	protected JSONArray getSuggestions( SearchContext searchContext ) {
		JSONArray array = JSONFactoryUtil.createJSONArray();
		try {
			Hits hits = LuceneHelper.search(searchContext);
			for (Document doc:hits.getDocs()) {
				array.put(doc.getField(Field.TITLE).getValue());
			}
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return array;
	}
	
	private Log _log = LogFactoryUtil.getLog(AutocompleteListener.class);

}
