package ru.swimmasters.jaxb;

import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: dedmajor
 * Date: Jul 13, 2010
 */
public class ContextHolder {
    private final JAXBContext jc;
    private final Schema schema;
    private final DefaultValidationEventHandler validationHandler;

    public ContextHolder() {
        try {
            jc = JAXBContext.newInstance("ru.swimmasters.domain");
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ClassPathResource classPathResource = new ClassPathResource("schema1.xsd");
        InputStream inputStream;
        try {
            inputStream = classPathResource.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        StreamSource source = new StreamSource(inputStream);
        try {
            schema = sf.newSchema(source);
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        }

        validationHandler = new DefaultValidationEventHandler();
    }


    public Unmarshaller createUnmarshaller() {
        Unmarshaller result;
        try {
            result = jc.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        result.setSchema(schema);
        try {
            result.setEventHandler(validationHandler);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    public Marshaller createMarshaller()  {
        Marshaller result;
        try {
            result = jc.createMarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        try {
            result.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }
}
