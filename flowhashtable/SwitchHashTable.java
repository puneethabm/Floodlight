package net.floodlightcontroller.flowhashtable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SwitchHashTable {
	public static long switch_id;
	public static Multimap<Long, Integer> myMap = ArrayListMultimap.create();
	protected static Logger logger = LoggerFactory.getLogger(SwitchHashTable.class);

	public static void addSwitchKeys(long switch_id,int switchKey){
		myMap.put(switch_id, switchKey);
	}
	
	public static Collection<Integer> getSwitchKeys(long switch_id){
		return myMap.get(switch_id);
	}	
	
	public static void printSwitchHashTable(){
		String logKeyStr;
		String logStr;
		logger.info("Switch Hash Table");
		Set keySet = myMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while( keyIterator.hasNext()) {
			logKeyStr = "";
			logStr = "";
		    Object key = keyIterator.next();
		    logKeyStr = "Key: " + key + ", ";
		    Collection values = (Collection) myMap.get((Long) key);
		    Iterator valuesIterator = values.iterator();
		    logStr += "Value: ";
		    while( valuesIterator.hasNext()) {
		    	logStr += valuesIterator.next() + ". ";		    	
		    }
		    logger.info(logKeyStr + logStr);
		}		
	}
}