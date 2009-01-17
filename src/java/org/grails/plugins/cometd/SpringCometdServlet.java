package org.grails.plugins.cometd;

import org.cometd.Bayeux;
import org.mortbay.cometd.AbstractBayeux;
import org.mortbay.cometd.continuation.ContinuationCometdServlet;

import javax.servlet.ServletException;

public class SpringCometdServlet extends ContinuationCometdServlet {

    public void init() throws ServletException {
        // do not call ContinuationCometdServlet.init();
        synchronized (SpringCometdServlet.class) {
            _bayeux = (AbstractBayeux) getServletContext().getAttribute(Bayeux.DOJOX_COMETD_BAYEUX);
            if (_bayeux == null) {
                _bayeux = newBayeux();
            }
        }

        getServletContext().setAttribute(Bayeux.DOJOX_COMETD_BAYEUX, _bayeux); //only for handle newBayeux case, that probably won't happen
    }


}
