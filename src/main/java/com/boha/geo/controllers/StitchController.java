package com.boha.geo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/")

public class StitchController {
    private static final Logger logger = Logger.getLogger(StitchController.class.getSimpleName());

    @GetMapping("return")
    public String handleStitchCallback(String id) {
        logger.info("GET Message from Stitch: " + id);
        return """ 
                   <html>
                     <head>
                       <title>Received Message</title>
                     </head>
                     <body>
                       <h1>Received Message""</h1>
                       <div class="message-placeholder">$message</div>
                     </body>
                   </html>
                """;

    }
}
