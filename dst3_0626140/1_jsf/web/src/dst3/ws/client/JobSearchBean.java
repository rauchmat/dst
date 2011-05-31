
package dst3.ws.client;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "JobSearchBean", targetNamespace = "http://ws.dst3/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface JobSearchBean {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<dst3.ws.client.JobDto>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "find", targetNamespace = "http://ws.dst3/", className = "dst3.ws.client.Find")
    @ResponseWrapper(localName = "findResponse", targetNamespace = "http://ws.dst3/", className = "dst3.ws.client.FindResponse")
    public List<JobDto> find(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}