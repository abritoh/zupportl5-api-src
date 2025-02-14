package org.apache.clusterbr.zupportl5.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.clusterbr.zupportl5.dto.MethodResult;
import org.apache.clusterbr.zupportl5.enums.HttpStatusCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import jakarta.annotation.PostConstruct;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ValidatorXmlXsd_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class ValidatorXmlXsd {

    private static final Logger logger = LoggerFactory.getLogger(ValidatorXmlXsd.class);

    private Schema schema;
    private final String xsdFilePath;

    public ValidatorXmlXsd(String xsdFilePath) {
        this.xsdFilePath = xsdFilePath;
    }

    @PostConstruct
    private void Init() {
        try (InputStream xsdStream = new ClassPathResource(xsdFilePath).getInputStream()) {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            this.schema = factory.newSchema(new StreamSource(xsdStream));   
            
            logger.info("[Info] (Init) XSD-schema LOADED");

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (Init) xsdStream error", ex);
        }
    }

    public MethodResult<String> validate(String xmlString, String fileName) {

        MethodResult<String> result = new MethodResult<>();
        
        try(InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes())  ) {

            StreamSource xmlSource = new StreamSource(inputStream);

            result = this.validateXML(this.schema, xmlSource, fileName);

        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (validate) xml-inputStream error", ex);
        }

        return result;
    }

    private MethodResult<String> validateXML(
            javax.xml.validation.Schema schema,
            javax.xml.transform.Source xmlSource,
            String xmlSourcePath) {

        MethodResult<String> result = new MethodResult<>();

        try {

            if( schema != null ) {
                Validator validator = schema.newValidator();
                validator.setErrorHandler(new CustomErrorHandler(result.getMessageList()));
                validator.validate(xmlSource);

                if (result.getMessageList().isEmpty()) {
                    result.setSuccess(true);
                    result.setCode( HttpStatusCodeEnum.OK.value() );
                    result.setItem(AppConstants.VALID);
                    
                    logger.info("[SUCCESS] (ValidatorXmlXsd::validateXML) PASSED)");
                }
            } else {
                result.setCode( HttpStatusCodeEnum.NotFound.value() );
                result.getMessageList().add("Validation error: Schema file XSD not found");
            }

        } catch (SAXException ex1) {
            ex1.printStackTrace();
            logger.error("[Exception] (ValidatorXmlXsd::validateXML) FAIL", ex1);
        } catch (Exception ex2) {
            ex2.printStackTrace();
            logger.error("[Exception] (ValidatorXmlXsd::validateXML) ERROR", ex2);
        }
        return result;
    }

    
    static class CustomErrorHandler implements ErrorHandler {

        private final List<String> errorList;

        public CustomErrorHandler(List<String> errorList) {
            this.errorList = errorList;
        }

        @Override
        public void warning(SAXParseException parsingExc) {
            String msg = formatMessage("Warning", parsingExc);
            errorList.add(msg);
            logger.debug(msg);
        }

        @Override
        public void error(SAXParseException parsingExc) {
            String msg = formatMessage("Error", parsingExc);
            errorList.add(msg);
            logger.debug(msg);
        }

        @Override
        public void fatalError(SAXParseException parsingExc) {
            String msg = formatMessage("Fatal Error", parsingExc);
            errorList.add(msg);
            logger.debug(msg);
        }

        private String formatMessage(String type, SAXParseException ex) {
            return String.format("SAXParse: %s at line %d, column %d: %s",
                    type, ex.getLineNumber(), ex.getColumnNumber(), ex.getMessage());
        }
    }
}
