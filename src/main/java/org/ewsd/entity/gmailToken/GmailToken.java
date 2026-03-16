package org.ewsd.entity.gmailToken;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class GmailToken {

    @Id
    private Long userId;

    private String accessToken;

    private String refreshToken;

    private Instant expiryDate;
}