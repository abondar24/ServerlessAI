package org.abondar.experimental.imagerec;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.imagerec.api.controller.RecognitionController;
import org.abondar.experimental.imagerec.api.service.RecognitionService;
import org.abondar.experimental.imagerec.data.AnalysisResults;
import org.abondar.experimental.imagerec.data.EventMsg;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RecognitionController.class, RecognitionService.class})
@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
public class RecognitionControllerTest {

    @MockBean
    private RecognitionService recognitionService;



    @Autowired
    private MockMvc mockMvc;


    @Test
    @Disabled
    public void testTriggerEvent() throws Exception {
        var mapper = new ObjectMapper();
        var msg = new EventMsg("some-url.org");
        var body = mapper.writeValueAsString(msg);

        doNothing().when(recognitionService).triggerAnalysis(msg);


        mockMvc.perform(post("/recognition")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void testGetEvent() throws Exception {
        var mapper = new ObjectMapper();
        var msg = new EventMsg("some-url.org");
        var analysis = mapper.writeValueAsString(new AnalysisResults());
        when(recognitionService.getResults(msg.getUrl())).thenReturn(analysis);

        mockMvc.perform(get("/recognition")
                .queryParam("domain", msg.getUrl())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

}
