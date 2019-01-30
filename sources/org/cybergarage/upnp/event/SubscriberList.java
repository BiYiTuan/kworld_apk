package org.cybergarage.upnp.event;

import java.util.Vector;

public class SubscriberList extends Vector {
    public C0793d getSubscriber(int i) {
        Object obj = null;
        try {
            obj = get(i);
        } catch (Exception e) {
        }
        return (C0793d) obj;
    }
}
