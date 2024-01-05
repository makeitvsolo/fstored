package github.makeitvsolo.fstored.user.access.application.usecase.access.dto;

import java.time.LocalDateTime;

public record AccessTokenDto(String token, LocalDateTime expiresAt, UserDto active) {
}
