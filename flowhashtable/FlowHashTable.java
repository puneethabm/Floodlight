package net.floodlightcontroller.flowhashtable;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowHashTable {
	public static Hashtable<Integer,FlowEntry> ht = new Hashtable<Integer,FlowEntry>();	
	public static Integer id;
	public static FlowEntry flowentry;	
	protected static Logger logger = LoggerFactory.getLogger(FlowHashTable.class);	
	
	public static void addFlowEntry(Integer fk,FlowEntry fe){
		if(FlowHashTable.getFlowEntry(fk)!=null){
			ht.remove(fk);
		}
		ht.put(fk, fe);
	}
	
	public static FlowEntry getFlowEntry(Integer fk){
		return ht.get(fk);
	}
	
	public static Date getStartTimeStamp(Integer fk){
		return getFlowEntry(fk).start_timestamp;
	}
	
	public static Date getEndTimeStamp(Integer fk){
		return getFlowEntry(fk).end_timestamp;
	}	
	
	public static void printFlowHashTable(){
		logger.info("Flow Hash Table");
		Map<Integer, FlowEntry> map = new TreeMap<Integer, FlowEntry>(ht);
		Set<Integer> set = map.keySet();
		Iterator<Integer> itr = set.iterator();

		while (itr.hasNext()) {
			id = itr.next();
			flowentry = ht.get(id);
			logger.info("Key:" + id + ",   ID=" + flowentry.flowSeqID
					+ ",SwitchID=" + flowentry.input_switch_id 
					+ ",Inport=" + flowentry.in_port + ",SrcMAC=" + flowentry.src_mac
					+ ",DstMAC=" + flowentry.dst_mac 
					+ ",Type=" + flowentry.data_layer_type 
					+ ",VlanID=" + flowentry.vlan_id 
					+ ",VlanPriority=" + flowentry.vlan_priority 
					+ ",Src_IP=" + flowentry.src_ip
					+ ",Src_Masklen=" + flowentry.src_masklen 
					+ ",Dst_IP=" + flowentry.dst_ip 
					+ ",Dst_Masklen=" + flowentry.dst_masklen 
					+ ",Protocol=" + flowentry.protocol
					+ ",TOS=" + flowentry.tos 
					+ ",TCP_Src=" + flowentry.tcp_src
					+ ",TCP_Dst=" + flowentry.tcp_dst 
					+ ",Wildcards=" + flowentry.wildcards 
					+ " ,Date="	+ flowentry.start_timestamp 
					+ " ,Minutes=" + flowentry.diffMinutes 
					+ " ,Seconds=" + flowentry.diffSeconds);
		}
	}
	
	//Prints the flow, given the key
	public static void printFlowHashTableEntry(int id) {
		flowentry = ht.get(id);
		logger.info("Key:" + id + ",   ID=" + flowentry.flowSeqID
				+ ",SwitchID=" + flowentry.input_switch_id 
				+ ",Inport=" + flowentry.in_port + ",SrcMAC=" + flowentry.src_mac
				+ ",DstMAC=" + flowentry.dst_mac 
				+ ",Type=" + flowentry.data_layer_type 
				+ ",VlanID=" + flowentry.vlan_id 
				+ ",VlanPriority=" + flowentry.vlan_priority 
				+ ",Src_IP=" + flowentry.src_ip
				+ ",Src_Masklen=" + flowentry.src_masklen 
				+ ",Dst_IP=" + flowentry.dst_ip 
				+ ",Dst_Masklen=" + flowentry.dst_masklen 
				+ ",Protocol=" + flowentry.protocol
				+ ",TOS=" + flowentry.tos 
				+ ",TCP_Src=" + flowentry.tcp_src
				+ ",TCP_Dst=" + flowentry.tcp_dst 
				+ ",Wildcards=" + flowentry.wildcards 
				+ " ,Date="	+ flowentry.start_timestamp 
				+ " ,Minutes=" + flowentry.diffMinutes 
				+ " ,Seconds=" + flowentry.diffSeconds);
	}	
}