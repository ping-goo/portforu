package org.pinggu.portforu;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorTestController {

    @GetMapping("/error-test")
    public ResponseEntity<String> error() {
        throw new RuntimeException("500 Internal Server Error for Prometheus test");
    }
}