package org.ewsd.service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ewsd.entity.student.Student;
import org.ewsd.entity.tutor.Tutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendHTMLMail() {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("chanmyae.huawei@gmail.com");
            helper.setTo("chanmyae.cma30@gmail.com");
            helper.setSubject("email sent");
            helper.setText(getOtpHtmlMessage("123456"),true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending HTML mail: ", e);
        }
    }

    @Async
    public void sendReallocationMailToStudent(Student student, Tutor tutor) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(student.getUser().getEmail());
            helper.setSubject("Tutor Reallocation Notice");

            helper.setText("""
            <h2>Hello %s</h2>
            <p>Your tutor has been updated.</p>
            <p><b>New Tutor:</b> %s</p>
            """.formatted(student.getFullName(), tutor.getFullName()), true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Student mail failed", e);
        }
    }

    private String getAccountHtmlMessage(String password) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
        </head>

        <body style="margin:0;padding:0;background:#f2f4f6;font-family:Arial,Helvetica,sans-serif;">

        <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" style="padding:40px 0;">

                    <table width="600" cellpadding="0" cellspacing="0"
                           style="background:#ffffff;border-radius:10px;
                           padding:40px;box-shadow:0 4px 15px rgba(0,0,0,0.08);">

                        <tr>
                            <td align="center">
                                <h2 style="color:#333;margin-bottom:5px;">
                                    Welcome to e-Tutor
                                </h2>
                                <p style="color:#888;font-size:14px;">
                                    Your account has been created successfully
                                </p>
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top:25px;font-size:15px;color:#444;">
                                Hello, This is test email,
                                <br/><br/>
                                Your account has been successfully created.
                                Below is your temporary password.
                                Please change it after your first login.
                            </td>
                        </tr>

                        <tr>
                            <td align="center" style="padding:35px 0;">
                                <div style="
                                    font-size:22px;
                                    font-weight:bold;
                                    padding:15px 25px;
                                    border:2px dashed #4F46E5;
                                    border-radius:8px;
                                    color:#4F46E5;
                                    display:inline-block;">
                                    %s
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td style="font-size:14px;color:#666;">
                                For security reasons, please do not share this password with anyone.
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top:30px;font-size:14px;color:#444;">
                                Thank you,<br/>
                                <b>e-Tutor Team</b>
                            </td>
                        </tr>

                        <tr>
                            <td align="center"
                                style="padding-top:40px;font-size:12px;color:#999;">
                                © 2026 e-Tutor. All rights reserved.
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

        </body>
        </html>
        """.formatted(password);
    }

    private String getOtpHtmlMessage(String otp) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
        </head>
        <body style="margin:0;padding:0;background-color:#f2f4f6;font-family:Arial,Helvetica,sans-serif;">
        
        <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" style="padding:40px 0;">
                
                    <table width="600" cellpadding="0" cellspacing="0" 
                           style="background:#ffffff;border-radius:10px;padding:40px;
                           box-shadow:0 4px 15px rgba(0,0,0,0.08);">
                       
                        <tr>
                            <td align="center">
                                <h2 style="color:#333;margin-bottom:10px;">
                                    e-Tutor Verification
                                </h2>
                                <p style="color:#888;font-size:14px;">
                                    Secure OTP Authentication
                                </p>
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top:25px;color:#444;font-size:15px;">
                                Hello,
                                <br/><br/>
                                Use the verification code below to complete your login.
                                This code will expire in <b>2 minutes</b>.
                            </td>
                        </tr>

                        <tr>
                            <td align="center" style="padding:35px 0;">
                                <div style="
                                    font-size:32px;
                                    font-weight:bold;
                                    letter-spacing:6px;
                                    padding:18px 30px;
                                    background:#4F46E5;
                                    color:white;
                                    display:inline-block;
                                    border-radius:8px;">
                                    %s
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td style="font-size:14px;color:#666;">
                                If you didn't request this code, you can safely ignore this email.
                            </td>
                        </tr>

                        <tr>
                            <td style="padding-top:30px;font-size:14px;color:#444;">
                                Regards,<br/>
                                <b>e-Tutor Team</b>
                            </td>
                        </tr>

                        <tr>
                            <td align="center"
                                style="padding-top:40px;font-size:12px;color:#999;">
                                © 2026 e-Tutor. All rights reserved.
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>

        </body>
        </html>
        """.formatted(otp);
    }
}
