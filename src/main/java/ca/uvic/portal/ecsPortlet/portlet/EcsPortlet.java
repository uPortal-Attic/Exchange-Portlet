package ca.uvic.portal.ecsPortlet.portlet;

import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A simple uPortal specific portlet shell
 *
 */
public class EcsPortlet extends GenericPortlet {

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException
    {

        //Get the USER_INFO from portlet.xml, which gets it from personDirs.xml
        Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO);
        String firstname = (String)userInfo.get("user.name.given");
        String lastname  = (String)userInfo.get("user.name.family");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Hello World, and '" + firstname + " " + lastname + "'!");
        out.flush();
    }

}
