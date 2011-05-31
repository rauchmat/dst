
package dst3.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "JobSearchBeanService", targetNamespace = "http://ws.dst3/", wsdlLocation = "http://localhost:8080/dst3/JobSearchBeanService?wsdl")
public class JobSearchBeanService
    extends Service
{

    private final static URL JOBSEARCHBEANSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(dst3.ws.client.JobSearchBeanService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = dst3.ws.client.JobSearchBeanService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/dst3/JobSearchBeanService?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/dst3/JobSearchBeanService?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        JOBSEARCHBEANSERVICE_WSDL_LOCATION = url;
    }

    public JobSearchBeanService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public JobSearchBeanService() {
        super(JOBSEARCHBEANSERVICE_WSDL_LOCATION, new QName("http://ws.dst3/", "JobSearchBeanService"));
    }

    /**
     * 
     * @return
     *     returns JobSearchBean
     */
    @WebEndpoint(name = "JobSearchBeanPort")
    public JobSearchBean getJobSearchBeanPort() {
        return super.getPort(new QName("http://ws.dst3/", "JobSearchBeanPort"), JobSearchBean.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns JobSearchBean
     */
    @WebEndpoint(name = "JobSearchBeanPort")
    public JobSearchBean getJobSearchBeanPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.dst3/", "JobSearchBeanPort"), JobSearchBean.class, features);
    }

}