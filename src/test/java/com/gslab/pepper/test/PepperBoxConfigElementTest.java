package com.gslab.pepper.test;

import com.gslab.pepper.config.plaintext.PlainTextConfigElement;
import com.gslab.pepper.config.plaintext.PlainTextConfigElementBeanInfo;
import com.gslab.pepper.config.serialized.SerializedConfigElement;
import com.gslab.pepper.config.serialized.SerializedConfigElementBeanInfo;
import com.gslab.pepper.input.SchemaProcessor;
import com.gslab.pepper.input.serialized.ClassPropertyEditor;
import com.gslab.pepper.model.FieldExpressionMapping;
import com.gslab.pepper.util.PropsKeys;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by satish on 26/2/17.
 */
public class PepperBoxConfigElementTest {

    @BeforeAll
    static void setUp(){
        JMeterContext jmcx = JMeterContextService.getContext();
        jmcx.setVariables(new JMeterVariables());
    }

    @Test
    public void plainTextConfigTest(){

        PlainTextConfigElement plainTextConfigElement = new PlainTextConfigElement();
        plainTextConfigElement.setJsonSchema(TestInputUtils.testSchema);
        plainTextConfigElement.setPlaceHolder(PropsKeys.MSG_PLACEHOLDER);
        plainTextConfigElement.iterationStart(null);
        Object object = JMeterContextService.getContext().getVariables().getObject(PropsKeys.MSG_PLACEHOLDER);
        JSONObject jsonObject = new JSONObject(object.toString());
        Assertions.assertTrue((Integer)jsonObject.get("messageId") > 0, "Failed to run config element");

    }

    @Test
//    @Test(expected = ClassFormatError.class)
    public void plainTextExceptionTest(){

        PlainTextConfigElement plainTextConfigElement = new PlainTextConfigElement();
        plainTextConfigElement.setJsonSchema(TestInputUtils.defectSchema);
        plainTextConfigElement.setPlaceHolder(PropsKeys.MSG_PLACEHOLDER);
        JMeterContextService.getContext().getVariables().remove(PropsKeys.MSG_PLACEHOLDER);
        Assertions.assertThrows(ClassFormatError.class, () -> {
            plainTextConfigElement.iterationStart(null);
            JMeterContextService.getContext().getVariables().getObject(PropsKeys.MSG_PLACEHOLDER);
        });
//        Object object = JMeterContextService.getContext().getVariables().getObject(PropsKeys.MSG_PLACEHOLDER);
//        Assert.assertNull("Failed to run config element", object);

    }

    @Test
    public void serializedConfigTest(){

        List<FieldExpressionMapping> fieldExpressionMappings = TestInputUtils.getFieldExpressionMappings();
        SerializedConfigElement serializedConfigElement = new SerializedConfigElement();
        serializedConfigElement.setClassName("com.gslab.pepper.test.Message");
        serializedConfigElement.setObjProperties(fieldExpressionMappings);
        serializedConfigElement.setPlaceHolder(PropsKeys.MSG_PLACEHOLDER);
        JMeterContextService.getContext().getVariables().remove(PropsKeys.MSG_PLACEHOLDER);
        serializedConfigElement.iterationStart(null);
        Message message = (Message)JMeterContextService.getContext().getVariables().getObject(PropsKeys.MSG_PLACEHOLDER);
        Assertions.assertEquals("Test Message", message.getMessageBody(), "Failed to run config element");

    }

    public void serializedConfigErrorTest(){

        List<FieldExpressionMapping> fieldExpressionMappings = TestInputUtils.getWrongFieldExpressionMappings();
        SerializedConfigElement serializedConfigElement = new SerializedConfigElement();
        serializedConfigElement.setClassName("com.gslab.pepper.test.Message");
        serializedConfigElement.setObjProperties(fieldExpressionMappings);
        serializedConfigElement.setPlaceHolder(PropsKeys.MSG_PLACEHOLDER);
        JMeterContextService.getContext().getVariables().remove(PropsKeys.MSG_PLACEHOLDER);
        serializedConfigElement.iterationStart(null);
        Message message = (Message)JMeterContextService.getContext().getVariables().getObject(PropsKeys.MSG_PLACEHOLDER);
        Assertions.assertNull(message, "Failed to run config element");

    }

    @Test
    public void validateSchemaProcessor(){

        try {

            SchemaProcessor schemaProcessor = new SchemaProcessor();
            Assertions.assertTrue(schemaProcessor.getPlainTextMessageIterator(TestInputUtils.testSchema) instanceof Iterator, "Failed to generate Iterator from input schema");

        } catch (Exception e) {
            Assertions.assertTrue( false, "Failed to generate Iterator from input schema : " + e.getMessage());
        }
    }


    @Test
//    @Test(expected = Exception.class)
    public void validateClassPropertyEditor(){
        Assertions.assertThrows(Exception.class, () -> {
            ResourceBundle.getBundle(PlainTextConfigElement.class.getName());
            PlainTextConfigElementBeanInfo pbeanInfo = new PlainTextConfigElementBeanInfo();
            Assertions.assertEquals(3, pbeanInfo.getPropertyDescriptors().length, "Failed to validate serialized property descriptors");

            ResourceBundle.getBundle(SerializedConfigElement.class.getName());
            SerializedConfigElementBeanInfo sbeanInfo = new SerializedConfigElementBeanInfo();
            Assertions.assertEquals(3, sbeanInfo.getPropertyDescriptors().length, "Failed to validate serialized property descriptors");

            PropertyDescriptor propertyDescriptor = sbeanInfo.getPropertyDescriptors()[1];
            ClassPropertyEditor classPropertyEditor = new ClassPropertyEditor(propertyDescriptor);
            classPropertyEditor.setValue("com.gslab.pepper.test.Message");
            classPropertyEditor.actionPerformed(null);
            Assertions.assertEquals("com.gslab.pepper.test.Message", classPropertyEditor.getValue(), "Failed to validate serialized property descriptors");
        });
    }

}
