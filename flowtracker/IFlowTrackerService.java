package net.floodlightcontroller.flowtracker;

import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.flowhashtable.FlowHashTable;

public interface IFlowTrackerService extends IFloodlightService {
    public ConcurrentCircularBuffer<FlowHashTable> getFlow();      
}
