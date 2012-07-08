package net.floodlightcontroller.flowtracker;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.floodlightcontroller.flowhashtable.FlowEntry;
import net.floodlightcontroller.flowhashtable.FlowHashTable;
import net.floodlightcontroller.flowhashtable.ProtocolHashTable;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ProtocolTrackerResource extends ServerResource {
	@Get("json")
	public Map<String, Collection<Hashtable<Integer, FlowEntry>>> retrieve() {
		int id;
		Multimap<String, Hashtable<Integer, FlowEntry>> myProto = ArrayListMultimap.create();
		Hashtable<Integer, FlowEntry> ht1 = null;
		Set keySet = ProtocolHashTable.myMap.keySet();
		Iterator keyIterator = keySet.iterator();

		while (keyIterator.hasNext()) {
			Object key = keyIterator.next();
			Collection values = (Collection) ProtocolHashTable.myMap.get((String) key);
			Iterator<Integer> valuesIterator = values.iterator();
			while (valuesIterator.hasNext()) {
				ht1 = new Hashtable<Integer, FlowEntry>();
				id = (int) valuesIterator.next();
				ht1.put(id, FlowHashTable.getFlowEntry(id));
				myProto.put((String) key, ht1);
			}
		}
		// asMap()-->Converts MultiMap to Map Object
		return myProto.asMap();
	}
}
