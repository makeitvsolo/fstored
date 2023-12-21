package github.makeitvsolo.fstored.boot.api.controller.user.access;

import github.makeitvsolo.fstored.boot.api.controller.user.access.request.UserCredentialsRequest;
import github.makeitvsolo.fstored.boot.api.message.OkMessage;
import github.makeitvsolo.fstored.boot.config.spring.session.authentication.Authenticated;
import github.makeitvsolo.fstored.boot.config.spring.session.cookie.SessionCookie;
import github.makeitvsolo.fstored.boot.config.spring.session.token.SessionToken;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MountRootFolderUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignInUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignOutUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.SignUpUserUsecase;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserCredentialsDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user-access")
public class UserAccessController {

    private final SignUpUserUsecase signUpUserUsecase;
    private final SignInUserUsecase<?> signInUserUsecase;
    private final SignOutUserUsecase<?> signOutUserUsecase;
    private final MountRootFolderUsecase<?> mountRootFolderUsecase;

    public UserAccessController(
            final SignUpUserUsecase signUpUserUsecase,
            final SignInUserUsecase<?> signInUserUsecase,
            final SignOutUserUsecase<?> signOutUserUsecase,
            final MountRootFolderUsecase<?> mountRootFolderUsecase
    ) {
        this.signUpUserUsecase = signUpUserUsecase;
        this.signInUserUsecase = signInUserUsecase;
        this.signOutUserUsecase = signOutUserUsecase;
        this.mountRootFolderUsecase = mountRootFolderUsecase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody final UserCredentialsRequest credentials
    ) {
        var payload = new UserCredentialsDto(credentials.name(), credentials.password());

        var user = signUpUserUsecase.invoke(payload);

        mountRootFolderUsecase.invoke(user.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OkMessage.from(HttpStatus.CREATED));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody final UserCredentialsRequest credentials
    ) {
        var payload = new UserCredentialsDto(credentials.name(), credentials.password());

        var access = signInUserUsecase.invoke(payload);

        var cookie = SessionCookie.from(access.token(), access.expiresAt());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(OkMessage.from(HttpStatus.OK, access));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@SessionToken final String token) {
        signOutUserUsecase.invoke(token);

        var removeCookie = SessionCookie.remove();

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, removeCookie.toString())
                .body(OkMessage.from(HttpStatus.NO_CONTENT));
    }

    @GetMapping("/active")
    public ResponseEntity<?> active(@Authenticated final UserDto activeUser) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(OkMessage.from(HttpStatus.OK, activeUser));
    }
}
