package net.floodlightcontroller.flowtracker;

import java.util.Hashtable;
import net.floodlightcontroller.flowhashtable.FlowEntry;
import net.floodlightcontroller.flowhashtable.FlowHashTable;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class FlowTrackerResource extends ServerResource {
    @Get("json")
    public Hashtable<Integer,FlowEntry> retrieve() {       
        return FlowHashTable.ht;
    }   
}

