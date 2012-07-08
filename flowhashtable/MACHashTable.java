package net.floodlightcontroller.flowhashtable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MACHashTable {
	public static String src_mac;
	public static Multimap<String, Integer> myMap = ArrayListMultimap.create();
	protected static Logger logger = LoggerFactory.getLogger(MACHashTable.class);

	public static void addMACKeys(String mac,int macKey){
		myMap.put(mac, macKey);
	}
	
	public static Collection<Integer> getMACKeys(String mac){
		return myMap.get(mac);
	}
	
	public static void printMACEntry(String mac){
		logger.info("Print Values for a given MAC");
		Collection values = (Collection) myMap.get(mac);
	    Iterator valuesIterator = values.iterator();
	    logger.info("Key=> "+mac+ " "+"Value=> ");
	    while( valuesIterator.hasNext()) {
	    	logger.info(valuesIterator.next() + ". ");
	    }
	    logger.info("\n");
	}
	
	public static void printMACKeys(){
			logger.info("All MAC Keys");
			Set keySet = myMap.keySet();
			Iterator valuesIterator = keySet.iterator();
			logger.info("Keys=> ");
		    while( valuesIterator.hasNext()) {
		    	logger.info(valuesIterator.next() + ". ");
		    }
		    logger.info("\n");
	}
	
	public static void printMACHashTable(){
		String logKeyStr;
		String logStr;
		logger.info("MAC Hash Table");
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