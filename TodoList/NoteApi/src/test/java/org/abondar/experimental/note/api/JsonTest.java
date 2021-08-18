package org.abondar.experimental.note.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.note.api.data.NoteParams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonTest {

    private static ObjectMapper mapper;


    @BeforeAll
    public static void init(){
        mapper = new ObjectMapper();
    }

    @Test
    public void paramsTest() throws Exception{
        var testJson = "{\n" +
                "    \"noteLang\": \"en-US\",\n" +
                "    \"noteUri\": \"https://s3-eu-west-1.amazonaws.com/td-str/public/b499d1a4-a170-472e-a8f0-4e955856bb18.wav\",\n" +
                "    \"noteFormat\": \"wav\",\n" +
                "    \"noteName\": \"b499d1a4-a170-472e-a8f0-4e955856bb18\",\n" +
                "    \"noteSampleRate\": 48000\n" +
                "}";

        var note = mapper.readValue(testJson, NoteParams.class);

        assertEquals("en-US",note.getLanguageCode());
        assertNull(note.getSettings());
    }


}
