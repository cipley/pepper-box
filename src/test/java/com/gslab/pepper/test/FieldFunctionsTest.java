package com.gslab.pepper.test;

import com.gslab.pepper.input.FieldDataFunctions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by satish on 5/3/17.
 */
public class FieldFunctionsTest {

    @Test
    public void verifyTimeFunctions() {

        assertTrue(FieldDataFunctions.TIMESTAMP() <= System.currentTimeMillis(), "Invalid timestamp value");
        assertTrue(FieldDataFunctions.TIMESTAMP("01-05-1988 12:12:12+0700", "01-05-2000 12:12:12+0700") > 568363332000L, "Invalid timestamp between interval value");
        assertNotNull( FieldDataFunctions.DATE("dd-MM-yyyy HH:mm:ss"), "Invalid date value");

    }

    @Test
    public void verifyRandomFunctions() {

        assertNotNull(FieldDataFunctions.RANDOM_STRING("1", "2"), "Invalid random string value");
        assertTrue(FieldDataFunctions.RANDOM_INT(1, 2) > 0, "Invalid int value");
        assertTrue(FieldDataFunctions.RANDOM_DOUBLE(1.0, 2.0) > 0, "Invalid double value");
        assertTrue(FieldDataFunctions.RANDOM_FLOAT(1.0F, 2.0F) > 0, "Invalid float value");
        assertTrue(FieldDataFunctions.RANDOM_LONG(1, 2) > 0, "Invalid long value");
        assertEquals(FieldDataFunctions.RANDOM_ALPHA_NUMERIC("A", 3), "AAA", "Invalid random string");


    }

    @Test
    public void verifyRandomRangeFunctions() {

        assertTrue(FieldDataFunctions.RANDOM_INT_RANGE(1, 3) <= 3, "Invalid int range value");
        assertTrue(FieldDataFunctions.RANDOM_LONG_RANGE(1, 3) <= 3, "Invalid long range value");
        assertTrue(FieldDataFunctions.RANDOM_FLOAT_RANGE(1.0F, 3.0F) <= 3, "Invalid float range value");
        assertTrue(FieldDataFunctions.RANDOM_DOUBLE(1.0, 3.0) <= 3, "Invalid double range value");

    }

    @Test
    public void verifyUserFunctions() {

        assertNotNull(FieldDataFunctions.FIRST_NAME(), "Invalid first name value");
        assertNotNull(FieldDataFunctions.LAST_NAME(), "Invalid last name value");
        assertNotNull(FieldDataFunctions.USERNAME(), "Invalid user name value");
        assertNotNull(FieldDataFunctions.EMAIL("test.com"), "Invalid email address value");
        assertNotNull(FieldDataFunctions.GENDER(), "Invalid Gender");
        assertNotNull(FieldDataFunctions.PHONE(), "Invalid PHONE number");

    }

    @Test
    public void verifyUtilFunctions(){
        assertNotNull(FieldDataFunctions.UUID(), "Invalid UUID value");
        assertNotNull(FieldDataFunctions.IPV4(), "Invalid IP4 Address");
        assertNotNull(FieldDataFunctions.IPV6(), "Invalid IPV6 Address");
        boolean currentBoolean = FieldDataFunctions.BOOLEAN();
        assertTrue(currentBoolean == true || currentBoolean == false, "Invalid boolean value");
        assertEquals(FieldDataFunctions.SEQUENCE("randomSeq", 1, 1), 1, "Invalid sequence");
    }
}
