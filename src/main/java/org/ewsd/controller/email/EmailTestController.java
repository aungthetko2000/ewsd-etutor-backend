package org.ewsd.controller.email;

import lombok.RequiredArgsConstructor;
import org.ewsd.service.email.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        emailService.sendHTMLMail();
     return ResponseEntity.ok().build();
    }
}
