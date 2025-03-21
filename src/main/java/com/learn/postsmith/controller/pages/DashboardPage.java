package com.learn.postsmith.controller.pages;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/dashbaord")
public class DashboardPage {
    @GetMapping
    public ResponseEntity<String> renderDashboardPage() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/learn/postsmith/controller/pages/DashboardPage.java"));
        StringBuilder htmlContent = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            htmlContent.append(line);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html");
        return new ResponseEntity<String>(htmlContent.toString(), headers, HttpStatus.OK);
    }
}
