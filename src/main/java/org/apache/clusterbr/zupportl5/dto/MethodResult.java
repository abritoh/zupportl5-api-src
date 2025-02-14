package org.apache.clusterbr.zupportl5.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.clusterbr.zupportl5.enums.HttpStatusCodeEnum;
import org.apache.clusterbr.zupportl5.utils.JsonUtil;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/MethodResult_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class MethodResult<T>  implements Serializable {

    private T item;
    private Integer code = 0; 
    private Boolean success = false;
    private List<String> messageList =  new ArrayList<>();

    public MethodResult() {        
    }
    
    //-- getters & setters

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setCode(HttpStatusCodeEnum statusCode) {
        this.code = statusCode.value();
    }    

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messages) {
        this.messageList = messages;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
