package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * This is a class to help with mock calls simulating exchange server.  It
 * simulates the response envelope and takes care of creating exchange objects,
 * similar to EcsSoap, but w/out reaching out to the soap server.
 * NOTE: this will need to change if anything about EcsSoap changes.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class EcsSoapMock {
    /**
     * private A fake response string, pointing to a test file resource
     * simulating an Exchange soap response.
     */
    private static String fakeResponseFile;
    /**
     * private A digesterFile.
     */
    private static String digesterFile;
    /**
     * private Commons Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor takes string references to file resources.  One resource
     * represents an xml soap envelope, simulating a response from exchange.
     * The other resource is the digester rules file for parsing the fake
     * response.
     * @param fakeResp  The fake xml soap envelope file reference.
     * @param digestFile The rules file used to parse the fake soap xml
     * response.
     */
    public EcsSoapMock(final String fakeResp, final String digestFile) {
       fakeResponseFile = fakeResp;
       digesterFile     = digestFile;
    }

    /**
     * Simulate the EcsSoap queryExchange() method, which parses the soap
     * response, and queues up exchange objects.
     * @throws IOException File IO read exception.
     * @throws SAXException XML parse exception.
     */
    public final void queryExchange() throws IOException, SAXException {

        try {
            URL rules = getClass().getResource(digesterFile);
            InputStream response =
                getClass().getResourceAsStream(fakeResponseFile);
            Digester digester = DigesterLoader.createDigester(rules);
            digester.push(this);
            digester.setNamespaceAware(true);
            digester.parse(response);
        } catch (Exception exc) {
            logger.error(getClass().toString() + exc.getMessage());
            exc.printStackTrace();
        }
    }

    //This needs to be public to work with Digester File object add injection
    /**
     * Add a java object constructed of the soap elements returned to the queue.
     * @param obj parameter representing a record
     */
    public final void addExchangeObject(final Object obj) {
        exchangeObjects.add(obj);
    }

    /**
     * private ConcurrentLinkedQueue of exchange objects.
     */
    private ConcurrentLinkedQueue < Object > exchangeObjects =
        new ConcurrentLinkedQueue < Object >();

    /**
     * Get the queue of built up exchange domain objects.
     * @return queue of exchange objects.
     */
    public final ConcurrentLinkedQueue < Object > getExchangeObjects() {
        return exchangeObjects;
    }
}
