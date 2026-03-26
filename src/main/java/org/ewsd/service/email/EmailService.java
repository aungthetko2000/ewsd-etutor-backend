package org.ewsd.service.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendHTMLMail(String toEmail, String studentEmail, String password) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("chanmyae.huawei@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Student Account Created");
            helper.setText(getAccountHtmlMessage(studentEmail,password), true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error sending HTML mail: ", e);
        }
    }

    public void sendInactivityEmail(String toEmail) {
        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("chanmyae.huawei@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("28 days inactive warning!");

            helper.setText(getInactivityMessage(), true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error("Error sending inactivity email: ", e);
        }
    }

    private String getInactivityMessage() {

        return """
        <html>
        <body style="font-family:Arial">

        <h2>28 days inactive warning!</h2>

        <p>You have not logged in for <b>28 days</b>.</p>

        <p>Please log in to continue your learning.</p>

        <br>

        <p>Best Regards,<br>
        E-Tutor Team</p>

        </body>
        </html>
        """;
    }

    private String getAccountHtmlMessage(String email,String password) {
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
                                Below is your edu email and temporary password.
                                Please change it after your first login.
                            </td>
                        </tr>
                        
                         <!-- LOGIN INFO -->
                         <tr>
                             <td style="padding-top:20px;font-size:15px;color:#444;">
                                  <p><strong>Login Email:</strong> %s</p>
                                     <p>
                                        <strong>Temporary Password:</strong><br/>
                                           <span style="
                                                   display:inline-block;
                                                   margin-top:8px;
                                                   padding:10px 15px;
                                                   background:#f3f4f6;
                                                   border-radius:6px;
                                                   font-weight:bold;
                                                   letter-spacing:1px;">
                                                   %s
                                           </span>
                                  </p>
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
        """.formatted(email,password);
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
