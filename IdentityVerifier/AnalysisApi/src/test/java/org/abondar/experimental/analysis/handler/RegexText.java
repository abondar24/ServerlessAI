package org.abondar.experimental.analysis.handler;

import org.abondar.experimental.identity.analysis.handler.BlockFields;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegexText {

    @Test
    public void regexTest(){
        var regex = BlockFields.DateOfBirth.getVal();

        var str = "Date of birth/Date de naissance / Fecht de: nacimiento";
        var pt= Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

        var matcher = pt.matcher(str);

        var res = "";
        while (matcher.find()){
            res = matcher.group();
        }

        System.out.println(res);
        assertNotNull(res);

    }

    @Test
    public void regexNationalityTest(){
        var regex = BlockFields.Nationality.getVal();

        var str = "Nationality";
        var pt= Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

        var matcher = pt.matcher(str);

        var res = "";
        while (matcher.find()){
            res = matcher.group();
        }

        assertEquals(str,res);

    }
}
