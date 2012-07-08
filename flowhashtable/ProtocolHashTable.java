package net.floodlightcontroller.flowhashtable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ProtocolHashTable {
	public static String protoName;
	public static Multimap<String, Integer> myMap = ArrayListMultimap.create();
	protected static Logger logger = LoggerFactory.getLogger(ProtocolHashTable.class);
	 
	public static void addEntry(String protoName,int hashKeyValue){
		myMap.put(protoName, hashKeyValue);			
	}
	
	public static Collection<Integer> getProtoKeys(String protoName){
		return myMap.get(protoName);
	}
	
	public String getProtoName(){
		return protoName;
	}
	
	public static void printProtocolHashTable(){
		logger.info("Protocol Hash Table");
		Set keySet = myMap.keySet();
		Iterator keyIterator = keySet.iterator();
		while( keyIterator.hasNext()) {
		    Object key = keyIterator.next();
		    logger.info("Key: " + key + ", ");
		    Collection values = (Collection) myMap.get((String) key);
		    Iterator<Integer> valuesIterator = values.iterator();
		    logger.info("Value: ");
		    while( valuesIterator.hasNext()) {
		    	logger.info(valuesIterator.next()+ ". ");	    			    	
		    }
		    logger.info("\n");
		}
	}
}