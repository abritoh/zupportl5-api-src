package org.apache.clusterbr.zupportl5.dto;

import java.io.Serializable;

import org.apache.clusterbr.zupportl5.utils.JsonUtil;
/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/MethodResultXml_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */

public class MethodResultXml<T>  implements Serializable {

    private T result;
    private Boolean success = false;
    private String message =  new String("");

    public MethodResultXml() {
    }

    public MethodResultXml(T result, boolean success, String message) {
        this.result = result;
        this.success = success;
        this.message = message;
    }    
    
    //-- getters & setters
    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
