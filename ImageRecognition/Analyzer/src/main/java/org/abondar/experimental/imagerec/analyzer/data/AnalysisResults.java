package org.abondar.experimental.imagerec.analyzer.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnalysisResults {

    @JsonProperty("analysisResults")
    private List<ImageResult> results;

    private Map<String,Integer> wordCloud;
}
