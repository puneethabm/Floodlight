package net.floodlightcontroller.flowhashtable;

import java.util.Calendar;
import java.util.Date;

public class FlowEntry {
	public int flowSeqID;
	public long input_switch_id;
	public short in_port;
	public String src_mac;
	public String dst_mac;
	public int data_layer_type;
	public short vlan_id;
	public byte vlan_priority;
	public String src_ip;
	public int src_masklen;
	public String dst_ip;
	public int dst_masklen;
	public int protocol;
	public byte tos;
	public int tcp_src;
	public int tcp_dst;
	public int wildcards;
	// Timestamp
	public Date start_timestamp;
	public Date end_timestamp;
	public long diffSeconds;
	public long diffMinutes;
	public long diffHours;
	public long diffDays;
	
	private static final short TYPE_ARP = 0x0806;
	private static final short TYPE_IPv4 = 0x0800;	
	private static final byte PROTOCOL_ARP_REQUEST = 0x1;
	private static final byte PROTOCOL_ARP_REPLY = 0x2;
	private static final byte PROTOCOL_ICMP = 0x1;
	private static final byte PROTOCOL_TCP = 0x6;
	private static final byte PROTOCOL_UDP = 0x11;
	
	private static final int SRC_MAC = 12;
	private static final int DST_MAC = 2;
	private static final int TCP_SRC = 9;
	private static final int TCP_DST = 10;
	private static final int SRC_IP = 6;
	private static final int DST_IP = 7;
	private static final int VLAN_ID = 13;
	private static final int VLAN_PRIORITY = 14;
	private static final int SRC_MASKLEN = 15;
	private static final int DST_MASKLEN = 16;
	private static final int TOS = 17;
	private static final int WILDCARDS = 18;
	private static final int DATA_LAYER_TYPE = 3;
	private static final int IN_PORT = 4;
	private static final int SWITCH_ID = 5;
	private static final int PROTOCOL = 8;

	public FlowEntry(int flowSeqID, long input_switch_id, short in_port,
			byte[] src_mac, byte[] dst_mac, int data_layer_type, short vlan_id,
			byte vlan_priority, int src_ip, int src_masklen, int dst_ip,
			int dst_masklen, int protocol, byte tos, short tcp_src,
			short tcp_dst, int wildcards) {
		// Key of MAC HashTable
		int macHashKeyValue;
		String macKeyString;
		
		// Key of IP HashTable
		int ipHashKeyValue;
		String ipKeyString;
		
		// Key of Switch HashTable
		int switchHashKeyValue;
		long switchKeyString;
		
		this.flowSeqID = flowSeqID;
		this.input_switch_id = input_switch_id;
		this.in_port = in_port;
		this.src_mac = FlowEntry.MACByteToString(src_mac);
		this.dst_mac = FlowEntry.MACByteToString(dst_mac);
		this.data_layer_type = data_layer_type;
		this.vlan_id = vlan_id;
		this.vlan_priority = vlan_priority;
		this.src_ip = FlowEntry.IPInttoString(src_ip);
		this.src_masklen = src_masklen;
		this.dst_ip = FlowEntry.IPInttoString(dst_ip);
		this.dst_masklen = dst_masklen;
		this.protocol = protocol;
		this.tos = tos;
		this.tcp_src = FlowEntry.toUint32(tcp_src);
		this.tcp_dst = FlowEntry.toUint32(tcp_dst);
		this.wildcards = wildcards;

		if (FlowHashTable.getFlowEntry(this.hashCode()) == null) {
			// New Flow
			this.start_timestamp = new Date();
			this.end_timestamp = this.start_timestamp;

			// FlowHashTable
			FlowHashTable.addFlowEntry(this.hashCode(), this);

			// MAC HashTable(Src_MAC)
			macHashKeyValue = this.hashCode();
			macKeyString = MACByteToString(src_mac);
			MACHashTable.addMACKeys(macKeyString, macHashKeyValue);

			// MAC HashTable(Dst_MAC)
			macKeyString = MACByteToString(dst_mac);
			MACHashTable.addMACKeys(macKeyString, macHashKeyValue);

			// IP HashTable(Src_IP)
			ipHashKeyValue = this.hashCode();
			ipKeyString = IPInttoString(src_ip);
			IPHashTable.addIPKeys(ipKeyString, ipHashKeyValue);

			// IP HashTable(Dst_IP)
			ipKeyString = IPInttoString(dst_ip);
			IPHashTable.addIPKeys(ipKeyString, ipHashKeyValue);

			// Switch HashTable
			switchHashKeyValue = this.hashCode();
			switchKeyString = input_switch_id;
			SwitchHashTable.addSwitchKeys(switchKeyString, switchHashKeyValue);

						
			String protoName;
			if (data_layer_type == TYPE_ARP && protocol == PROTOCOL_ARP_REQUEST) {
				protoName = "arp-request";
			} else if (data_layer_type == TYPE_ARP && protocol == PROTOCOL_ARP_REPLY) {
				protoName = "arp-reply";
			} else if (data_layer_type == TYPE_IPv4 && protocol == PROTOCOL_ICMP) {
				protoName = "icmp";
			} else if (tcp_dst == 80 || tcp_src == 80) {
				protoName = "http";
			} else if (protocol == PROTOCOL_TCP) {
				protoName = "tcp";
			} else if (protocol == PROTOCOL_UDP) {
				protoName = "udp";
			} else {
				protoName = "unknown";
			}
			ProtocolHashTable.addEntry(protoName, macHashKeyValue);
		} else {
			// Existing Flow
			this.start_timestamp = FlowHashTable.getStartTimeStamp(this.hashCode());
			this.end_timestamp = FlowHashTable.getEndTimeStamp(this.hashCode());
			this.end_timestamp = new Date();
			calculateDuration();
			FlowHashTable.addFlowEntry(this.hashCode(), this);
		}
	}

	// Calculate Duration -- Time flows are maintained in the controller
	public void calculateDuration(){
		long milliseconds1, milliseconds2, diff;
		Calendar calendar1, calendar2;
		
		calendar1 = Calendar.getInstance();
		calendar2 = Calendar.getInstance();
		calendar1.setTime(this.start_timestamp);
		calendar2.setTime(this.end_timestamp);
		milliseconds1 = calendar1.getTimeInMillis();
		milliseconds2 = calendar2.getTimeInMillis();
		diff = milliseconds2 - milliseconds1;

		this.diffSeconds = diff / 1000;
		this.diffMinutes = diff / (60 * 1000);
		this.diffHours = diff / (60 * 60 * 1000);
		this.diffDays = diff / (24 * 60 * 60 * 1000);
	}
	
	@Override
	public boolean equals(Object obj) {
		// object must be tested at this point
		FlowEntry fe1 = (FlowEntry) obj;
		return input_switch_id == fe1.input_switch_id
				&& in_port == fe1.in_port
				&& (src_mac == fe1.src_mac || (src_mac != null && src_mac
						.equals(fe1.src_mac)))
				&& (dst_mac == fe1.dst_mac || (dst_mac != null && dst_mac
						.equals(fe1.dst_mac)))
				&& data_layer_type == fe1.data_layer_type 
				&& vlan_id == fe1.vlan_id && vlan_priority == fe1.vlan_priority 
				&& src_ip == fe1.src_ip && src_masklen == fe1.src_masklen
				&& dst_ip == fe1.dst_ip && dst_masklen == fe1.dst_masklen
				&& protocol == fe1.protocol && tos == fe1.tos 
				&& tcp_src == fe1.tcp_src && tcp_dst == fe1.tcp_dst 
				&& wildcards == fe1.wildcards;
	}

	@Override
	public int hashCode() {
		int hash = 11;
		int i,j;
		String switch_id_str;
		Long l;
		
		hash = (int) (SRC_MAC * hash + (null == src_mac ? 0 : src_mac.hashCode()));
		hash = (int) (DST_MAC * hash + (null == dst_mac ? 0 : dst_mac.hashCode()));
		hash = (int) (TCP_SRC * hash + tcp_src);
		hash = (int) (TCP_DST * hash + tcp_dst);
		hash = (int) (SRC_IP * hash + (null == src_ip ? 0 : src_ip.hashCode()));
		hash = (int) (DST_IP * hash + (null == dst_ip ? 0 : dst_ip.hashCode()));
		hash = (int) (VLAN_ID * hash + vlan_id);
		hash = (int) (VLAN_PRIORITY * hash + vlan_priority);
		hash = (int) (SRC_MASKLEN * hash + src_masklen);
		hash = (int) (DST_MASKLEN * hash + dst_masklen);
		hash = (int) (TOS * hash + tos);
		hash = (int) (WILDCARDS * hash + wildcards);
		hash = (int) (DATA_LAYER_TYPE * hash + data_layer_type);
		hash = (int) (IN_PORT * hash + in_port);
		switch_id_str = String.valueOf(input_switch_id);
		l = new Long(switch_id_str);
		i = l.intValue();
		j = i << 16;
		hash = (int) (SWITCH_ID * hash + j);
		hash = (int) (PROTOCOL * hash + protocol);
		return Math.abs((int) hash);
	}

	public static int toUint32(short s) {
		return s & 0xFFFF;
	}

	public static String MACByteToString(byte[] mac) {
		StringBuilder sb = new StringBuilder(18);
		for (byte b : mac) {
			if (sb.length() > 0)
				sb.append(':');
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static String IPInttoString(int i) {
		return ((i >> 24) & 0xFF) + "." +
		((i >> 16) & 0xFF) + "." +
		((i >> 8) & 0xFF) + "." +
		(i & 0xFF);
	}
}