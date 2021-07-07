package org.abondar.experimental.imagerec.api.controller;

import org.abondar.experimental.imagerec.api.service.RecognitionService;
import org.abondar.experimental.imagerec.data.EventMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/recognition")
public class RecognitionController {

    private final RecognitionService recognitionService;

    @Autowired
    public RecognitionController(RecognitionService recognitionService){
        this.recognitionService = recognitionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> triggerAnalysis(@RequestBody EventMsg msg) throws IOException {
       recognitionService.triggerAnalysis(msg);
       return ResponseEntity.ok("Analysis triggered");
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResults(@RequestParam(name="domain") String domain) throws IOException{
        var analysisJson = recognitionService.getResults(domain);
        return ResponseEntity.ok(analysisJson);
    }

}
