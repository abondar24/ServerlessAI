package org.abondar.experimental.imagerec.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.abondar.experimental.imagerec.data.ImageLabel;
import org.abondar.experimental.imagerec.data.ImageResult;
import org.abondar.experimental.imagerec.data.AnalysisResults;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnalyzerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJson() throws IOException {

        var ar = new AnalysisResults();

        Map<String, Integer> wordCloud = new HashMap<>();
        wordCloud.put("Cat", 3);
        wordCloud.put("Dog", 3);
        ar.setWordCloud(wordCloud);

        List<ImageResult> results = new ArrayList<>();
        ImageResult ir = new ImageResult("ai-as-a-service.s3-website-eu-west-1.amazonaws.com/cat1.png");

        List<ImageLabel> labels = new ArrayList<>();
        var label = new ImageLabel("Cat",0.99F);
        labels.add(label);

        ir.setLabels(labels);
        results.add(ir);
        ar.setResults(results);

        var json = mapper.writeValueAsString(ar);
        System.out.println(json);

        var res = mapper.readValue(json,AnalysisResults.class);

        assertEquals(res.getResults().size(),ar.getResults().size());
    }

}
