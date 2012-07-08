package net.floodlightcontroller.flowtracker;

import java.util.Collection;

import java.util.Map;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFType;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.flowhashtable.OFPacketRetrieve;
import java.util.ArrayList;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPacket;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.restserver.IRestApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openflow.protocol.OFMatch;

/**
 * 
 * Captures the packetIn messages i.e. flow entries from the switch
 * and stores it in Multimap<K,V> datastructure i.e. Single key, multiple values
 * 
 * @author puneetha
 */

public class FlowTracker implements IFloodlightModule, IOFMessageListener {
	protected IFloodlightProviderService floodlightProvider;
	protected IRestApiService restApi;
	protected static Logger logger;

	public void OFMatchObject(OFMatch matchObj) {
		OFPacketRetrieve ofpr = new OFPacketRetrieve(matchObj);
		return;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		l.add(IRestApiService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context)
			throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		restApi = context.getServiceImpl(IRestApiService.class);
		logger = LoggerFactory.getLogger(FlowTracker.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		restApi.addRestletRoutable(new FlowTrackerWebRoutable());
	}

	/**
	 * Checks the type of the packet i.e. ARP, ICMP,TCP, UDP,etc..
	 * 
	 * 
	 * @param sw
	 * @param pi
	 * @param cntx
	 */
	private void processPacketInMessage(IOFSwitch sw, OFPacketIn pi,
			FloodlightContext cntx) {
		Ethernet eth;
		IPacket p;

		eth = IFloodlightProviderService.bcStore.get(cntx,IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		p = eth.deserialize(pi.getPacketData(), 0, pi.getPacketData().length);
		OFMatch match = new OFMatch();
		match.loadFromPacket(pi.getPacketData(), pi.getInPort(), sw.getId());

		// Check if it is a ARP packet
		if (eth.getEtherType() == Ethernet.TYPE_ARP) {
			// set wildcards
			match.setWildcards(((Integer) sw.getAttribute(IOFSwitch.PROP_FASTWILDCARDS)).intValue()& ~OFMatch.OFPFW_ALL);
			logger.info("From ARP={}", new Object[] { match });
			OFMatchObject(match);
		}

		// Check if it is a ICMP packet
		if (match.getNetworkProtocol() == IPv4.PROTOCOL_ICMP
				|| match.getNetworkProtocol() == IPv4.PROTOCOL_TCP
				|| match.getNetworkProtocol() == IPv4.PROTOCOL_UDP) {
			match.setWildcards(((Integer) sw.getAttribute(IOFSwitch.PROP_FASTWILDCARDS)).intValue()& ~OFMatch.OFPFW_ALL);
			logger.info("ICMP Packet={}", new Object[] { match });
			OFMatchObject(match);
		}
		return;
	}

	@Override
	public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
		switch (msg.getType()) {
		case PACKET_IN:
			processPacketInMessage(sw, (OFPacketIn) msg, cntx);
			break;
		default:
			logger.info("Error in Packet In");
			break;
		}
		return Command.CONTINUE;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return FlowTracker.class.getPackage().getName();
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}
}
