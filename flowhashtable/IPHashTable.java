package net.floodlightcontroller.flowhashtable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class IPHashTable {
	public static String src_mac;
	public static Multimap<String, Integer> myMap = ArrayListMultimap.create();
	protected static Logger logger = LoggerFactory.getLogger(IPHashTable.class);	

	public static void addIPKeys(String ip,int ipKey){
		myMap.put(ip, ipKey);
	}
	
	public static Collection<Integer> getIPKeys(String ip){
		return myMap.get(ip);
	}	
	
	public static void printIPHashTable(){
		String logKeyStr;
		String logStr;
		logger.info("\nIP Hash Table");
		Set keySet = myMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while( keyIterator.hasNext()) {
			logKeyStr = "";
			logStr = "";
		    Object key = keyIterator.next();
		    logKeyStr = "Key: " + key + ", ";
		    Collection values = (Collection) myMap.get((String) key);
		    Iterator valuesIterator = values.iterator();
		    logStr += "Value: ";
		    while( valuesIterator.hasNext()) {
		    	logStr += valuesIterator.next() + ". ";		    	
		    }
		    logger.info(logKeyStr + logStr);
		}
	}
}