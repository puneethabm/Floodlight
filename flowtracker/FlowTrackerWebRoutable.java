package net.floodlightcontroller.flowtracker;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import net.floodlightcontroller.restserver.RestletRoutable;

public class FlowTrackerWebRoutable implements RestletRoutable {
    @Override
    public Restlet getRestlet(Context context) {
        Router router = new Router(context);
        router.attach("/flowtracker/json", FlowTrackerResource.class);        
        router.attach("/protocoltracker/json", ProtocolTrackerResource.class);
        router.attach("/mactracker/json", MACTrackerResource.class);
        router.attach("/iptracker/json", IPTrackerResource.class);         
        router.attach("/switchtracker/json", SwitchTrackerResource.class);        
        return router;
    }

    @Override
    public String basePath() {
        return "/wm";
    }
}
