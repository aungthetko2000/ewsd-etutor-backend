//package org.ewsd.controller.email;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.ewsd.dto.notification.NotificationResponseDto;
//import org.ewsd.dto.response.ApiResponse;
//import org.ewsd.service.email.EmailService;
//import org.ewsd.service.notification.NotificationService;
//import org.ewsd.util.JwtUtil;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/email")
//@RequiredArgsConstructor
//public class EmailTestController {
//    private final EmailService emailService;
//    @GetMapping
//    public ResponseEntity<?> getAll() {
//
//        emailService.sendHTMLMail(
//                "chanmyae.cma30@gmail.com", // test receiver
//                "Test1234"                  // test password
//        );
//
//        return ResponseEntity.ok("Test email sent");
//    }
//}
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
