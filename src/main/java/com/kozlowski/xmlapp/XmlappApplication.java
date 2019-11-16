package com.kozlowski.xmlapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

@SpringBootApplication
public class XmlappApplication {
    public static void main(String[] args) {
        SpringApplication.run(XmlappApplication.class, args);
    }
}

@Controller
class XmlappController {

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("message", "<p style=\"color:blue;\">KAMIL</p>");
        model.addAttribute("xmlAndFilters", new XmlAndFilters(""));
        return "index";
    }

    @PostMapping("/filterXml")
    public String filterXml(@ModelAttribute("xmlAndFilters") XmlAndFilters xmlAndFilters, BindingResult bindingResult, Model model) { //change string to custom pojo when filters ready

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        String validatorResource = Objects.requireNonNull(this.getClass().getClassLoader().getResource("validator.xsd")).getFile();
        Schema schema;
        boolean validationSuccesful = true;
        String errorMsg = "";
        String errorLineNumber = "";
        StreamSource source = null;
        String result = "";
        try {
            schema = schemaFactory.newSchema(new File(validatorResource));
            Validator validator = schema.newValidator();
            source = new StreamSource(new StringReader(xmlAndFilters.getXml()));
            validator.validate(source);
		} catch (IOException | SAXException e) {
            e.printStackTrace();
            validationSuccesful = false;
            errorMsg = e.getMessage();
            errorLineNumber = e instanceof SAXParseException ? "Linijka: " + ((SAXParseException) e).getLineNumber() : "";
        }
        if (source != null && validationSuccesful) {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				transformer = transformerFactory.newTransformer(new StreamSource(Objects.requireNonNull(this.getClass().getClassLoader().getResource("styler.xsl")).getFile()));
				StreamResult outputTarget = new StreamResult(new StringWriter());
				transformer.transform(new StreamSource(new StringReader(xmlAndFilters.getXml())), outputTarget);
				result = outputTarget.getWriter().toString();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		}
        model.addAttribute("xmlValid", String.valueOf(validationSuccesful));
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("errorLineNumber", errorLineNumber);
        model.addAttribute("formatted", result);
        return "index";
    }
}
