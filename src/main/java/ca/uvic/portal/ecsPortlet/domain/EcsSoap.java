package ca.uvic.portal.ecsPortlet.domain;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
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
 * A simple domain shell
 * 
 */
public class EcsSoap {
    // Begin Spring injection params
    private static String exchangeURL;
    private static String exchangeUser;
    private static String exchangePassword;
    private static String exchangeDomain;
    private static String soapXML;
    private static String digesterRuleFile;
    // End Spring Injection Params
    

    private final Log logger = LogFactory.getLog(getClass());

    //Use constructor injection
    public EcsSoap(String url,
                   String user, 
                   String password,
                   String domain, 
                   EcsRemoteSoapCall soapCall,
                   String digesterFile) {

        exchangeURL        = url;
        exchangeUser       = user;
        exchangePassword   = password;
        exchangeDomain     = domain;
        soapXML            = soapCall.getSoapCall();
        digesterRuleFile   = digesterFile;
        
    }


    public void queryExchange()
      throws HttpException, IOException, SAXException {

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
        //String response;
        try {
            method.setRequestHeader("Content-Type", "text/xml;charset=utf-8");
            byte[] b = soapXML.getBytes();
            method.setRequestHeader("Content-Length", String.valueOf(b.length));
            method.setRequestEntity(new ByteArrayRequestEntity(b));
            client.executeMethod(method);
            
            response = method.getResponseBodyAsStream();
            //response = method.getResponseBodyAsString();
            //if(logger.isDebugEnabled()) {
            //  logger.debug("String response is: " + response);
            //}

            try {
                URL rules = getClass().getResource(digesterRuleFile);
                Digester digester = DigesterLoader.createDigester(rules);
                digester.push(this);
                digester.setNamespaceAware(true);
                digester.parse(response);
            } catch (Exception exc) {
                exc.printStackTrace();
            } finally {
                response.close();
            }
        } finally {
            method.releaseConnection();
        }

        if(logger.isDebugEnabled()) {
            Iterator <Object> exchangeIterator =
                this.getExchangeObjects().iterator();
            while(exchangeIterator.hasNext()) {
               logger.debug("Element toString " + exchangeIterator.next().toString());
            }
        }

    }
    //This needs to be public to work with Digester File object add injection
    public void addExchangeObject(Object obj) {
        exchangeObjects.add(obj);
    }

    private ConcurrentLinkedQueue<Object> exchangeObjects =
        new ConcurrentLinkedQueue<Object>();

    public ConcurrentLinkedQueue <Object> getExchangeObjects() {
        return exchangeObjects;
    }

}
