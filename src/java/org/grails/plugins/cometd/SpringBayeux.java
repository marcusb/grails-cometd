package org.grails.plugins.cometd;

import org.mortbay.cometd.continuation.ContinuationBayeux;
import org.mortbay.cometd.filter.JSONDataFilter;
import org.cometd.DataFilter;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;
import java.util.List;

/**
 * SpringBayeux is overriden Continuation/AbstractBayeux. It do not assume:
 * - initialize(ServletContext) is called at the beginning. instead, properties are set before initialize()
 * - initialize doesn't take a ServletContext argument and use Spring ServletContextAware injection
 */
public class SpringBayeux extends ContinuationBayeux implements ServletContextAware {
    protected List filters;
    protected ServletContext __context;

    public void initialize() {
        try {
            super.initialize(__context);

            if (this.filters != null) {
                try {
                    for (Map filterDef : (List<Map>) this.filters) {
                        String channel = (String) filterDef.get("channels");
                        DataFilter filter = (DataFilter) filterDef.get("filter");
                        this.getChannel(channel, true).addDataFilter(filter);
                    }
                } catch (Exception e) {
                    __context.log("could not parse: " + this.filters, e);
                    //throw new ServletException(e);
                }
            }

            super.setMaxInterval(this._maxInterval);

            this.__context = null; //AbstractBayeux keeps a copy already
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMaxInterval(long ms) {
        this._maxInterval = ms;
        //remarks: ContinuationBayeux.setMaxInterval() assumes initialize is called
    }

    public List getFilters() {
        return filters;
    }

    public void setFilters(List filters) {
        this.filters = filters;
    }

    public void setServletContext(ServletContext servletContext) {
        this.__context = servletContext;
    }
}
