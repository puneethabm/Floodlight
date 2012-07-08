package net.floodlightcontroller.flowhashtable;

import java.util.Hashtable;
import net.floodlightcontroller.flowhashtable.FlowEntry;
import net.floodlightcontroller.flowhashtable.FlowHashTable;
import net.floodlightcontroller.flowhashtable.IPHashTable;
import net.floodlightcontroller.flowhashtable.MACHashTable;
import net.floodlightcontroller.flowhashtable.ProtocolHashTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openflow.protocol.OFMatch;

public class OFPacketRetrieve{	
	private static Logger logger;	
	private OFMatch matchObj;    
	public static Hashtable<Integer,FlowEntry> ht = new Hashtable<Integer,FlowEntry>();	
	public static Integer id;
	public static FlowEntry flowentry;	
	public static Integer flowSeqID = 0;
    	
    public OFPacketRetrieve(OFMatch matchObj) {	
        this.matchObj = matchObj;
        logger = LoggerFactory.getLogger(OFPacketRetrieve.class);
        logger.info("OFPacketRetrieve -- pushRoute match={}",new Object[] {matchObj});	        
        FlowEntry fe = new FlowEntry(flowSeqID++,
        							 matchObj.getSwitchDataPathId(),
        							 matchObj.getInputPort(),        							 
        							 matchObj.getDataLayerSource(),
        							 matchObj.getDataLayerDestination(),
        							 matchObj.getDataLayerType(),        							 
        							 matchObj.getDataLayerVirtualLan(),
        							 matchObj.getDataLayerVirtualLanPriorityCodePoint(),        							 
        							 matchObj.getNetworkSource(),
        							 matchObj.getNetworkSourceMaskLen(),
        							 matchObj.getNetworkDestination(),
        							 matchObj.getNetworkDestinationMaskLen(),
        							 matchObj.getNetworkProtocol(),
        							 matchObj.getNetworkTypeOfService(),        							 
        							 matchObj.getTransportSource(),
        							 matchObj.getTransportDestination(),        							 
        							 matchObj.getWildcards()
        							 );    
        FlowHashTable.printFlowHashTable();
        MACHashTable.printMACHashTable();  
        IPHashTable.printIPHashTable();
        ProtocolHashTable.printProtocolHashTable();
        SwitchHashTable.printSwitchHashTable();  
    }  
}
