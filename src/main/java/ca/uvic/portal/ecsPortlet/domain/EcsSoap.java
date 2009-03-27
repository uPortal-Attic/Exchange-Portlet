package ca.uvic.portal.ecsPortlet.domain;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.URL;
import org.xml.sax.SAXException;

/**
 * A class that is responsible for marshalling a soap call and sending it to
 * the Exchange server, parsing the soap response, and building domain objects.
 * @author cpfrank
 * @version svn:$Id$
 *
 */
public class EcsSoap {
    // Begin Spring injection params
    /**
     * private String exchangeURL Spring constructor injection.
     */
    private static String exchangeURL;
    /**
     * private String exchangeUser Spring constructor injection.
     */
    private static String exchangeUser;
    /**
     * private String exchangePassword Spring constructor injection.
     */
    private static String exchangePassword;
    /**
     * private String exchangeDomain Spring constructor injection.
     */
    private static String exchangeDomain;
    /**
     * private String digesterRuleFile Spring constructor injection.
     */
    private static String digesterRuleFile;
    /**
     * private String soapXML soap request xml from digesterRuleFile.
     */
    // End Spring Injection Params
    private static String soapXML;
    /**
     * private commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    //Use constructor injection
    /**
     * Constructor uses Spring injection.
     * @param url the resource located for the exchange server to query
     * @param user the exchange user
     * @param password the exchange user password
     * @param domain the exchange domain
     * @param soapCall the EcsRemoteCall soapCall type
     * @param digesterFile the Digester rule file for mapping the soap response
     * to an object
     */
    public EcsSoap(final String url,
                   final String user,
                   final String password,
                   final String domain,
                   final EcsRemoteSoapCall soapCall,
                   final String digesterFile) {

        exchangeURL        = url;
        exchangeUser       = user;
        exchangePassword   = password;
        exchangeDomain     = domain;
        soapXML            = soapCall.getSoapCall();
        digesterRuleFile   = digesterFile;

    }

    /**
     * Runs the marshalled soap query against the exchange server.
     * Method is responsible for collecting credentials, marshalling a soap
     * request call, querying the exchange server, and creating an stack of
     * domain objects according to a Digester file specification.
     * @throws IOException
     * @throws SAXException
     * @see org.apache.commons.digester.xmlrules.DigesterLoader
     */
    public final void queryExchange() throws IOException, SAXException {

        HttpClient client = new HttpClient();
        Credentials credentials = new NTCredentials(
                exchangeUser,
                exchangePassword,
                "paramDoesNotSeemToMatter",
                exchangeDomain);
        client.getState().setCredentials(AuthScope.ANY, credentials);

        PostMethod method = new PostMethod(exchangeURL);
        // Allow method declaration IOException to propagate error, but make
        // sure to close connections no matter what.
        InputStream response;
        try {
            method.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
            byte[] b = soapXML.getBytes();
            method.setRequestHeader("Content-Length", String.valueOf(b.length));
            method.setRequestEntity(new ByteArrayRequestEntity(b));
            client.executeMethod(method);
            response = method.getResponseBodyAsStream();

            //String responseString = method.getResponseBodyAsString();
            //if(logger.isDebugEnabled()) {
            //  logger.debug("String response is: " + responseString);
            //}

            try {
                URL rules = getClass().getResource(digesterRuleFile);
                Digester digester = DigesterLoader.createDigester(rules);
                digester.push(this);
                digester.setNamespaceAware(true);
                digester.parse(response);
            } catch (Exception exc) {
                logger.error(getClass().toString() + exc.getMessage());
                exc.printStackTrace();
            } finally {
                response.close();
            }
        } finally {
            method.releaseConnection();
        }

        if (logger.isDebugEnabled()) {
            Iterator < Object > exchangeIterator =
                this.getExchangeObjects().iterator();
            while (exchangeIterator.hasNext()) {
               logger.debug("Element toString " + exchangeIterator.next().
                       toString());
            }
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
