package github.makeitvsolo.fstored.boot.api.controller.storage;

import github.makeitvsolo.fstored.boot.api.message.OkMessage;
import github.makeitvsolo.fstored.boot.config.spring.session.authentication.Authenticated;
import github.makeitvsolo.fstored.storage.application.usecase.folder.FetchFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.FolderSearchUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MakeFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.MoveFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.RemoveFolderUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.FolderSearchDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.MoveFolderDto;
import github.makeitvsolo.fstored.storage.application.usecase.folder.dto.RootSearchDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/storage/folders")
public class FolderController {

    private static final Logger LOG = LoggerFactory.getLogger(FolderController.class);

    private final MakeFolderUsecase<?> makeFolderUsecase;
    private final MoveFolderUsecase<?> moveFolderUsecase;
    private final RemoveFolderUsecase<?> removeFolderUsecase;
    private final FetchFolderUsecase<?> fetchFolderUsecase;
    private final FolderSearchUsecase<?> folderSearchUsecase;

    public FolderController(
            final MakeFolderUsecase<?> makeFolderUsecase,
            final MoveFolderUsecase<?> moveFolderUsecase,
            final RemoveFolderUsecase<?> removeFolderUsecase,
            final FetchFolderUsecase<?> fetchFolderUsecase,
            final FolderSearchUsecase<?> folderSearchUsecase
    ) {
        this.makeFolderUsecase = makeFolderUsecase;
        this.moveFolderUsecase = moveFolderUsecase;
        this.removeFolderUsecase = removeFolderUsecase;
        this.fetchFolderUsecase = fetchFolderUsecase;
        this.folderSearchUsecase = folderSearchUsecase;
    }

    @PostMapping(value = "/**", consumes = "application/json")
    public ResponseEntity<?> makeFolder(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser,
            @RequestParam(name = "mvfrom", required = false) final String movedFrom
    ) {
        var path = extractPathFrom(request.getRequestURI());

        if (movedFrom != null) {
            LOG.info("trying to move folder...");
            var payload = new MoveFolderDto(activeUser.id(), movedFrom, path);
            moveFolderUsecase.invoke(payload);

            LOG.info("folder successfully moved");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(OkMessage.from(HttpStatus.CREATED));
        }

        LOG.info("trying to make folder...");
        var payload = new FolderDto(activeUser.id(), path);
        makeFolderUsecase.invoke(payload);

        LOG.info("folder successfully made");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OkMessage.from(HttpStatus.CREATED));
    }

    @DeleteMapping(value = "/**")
    public ResponseEntity<?> deleteFolder(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser
    ) {
        var path = extractPathFrom(request.getRequestURI());

        LOG.info("trying to remove folder...");
        var payload = new FolderDto(activeUser.id(), path);
        removeFolderUsecase.invoke(payload);

        LOG.info("folder successfully removed");
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(OkMessage.from(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/**")
    public ResponseEntity<?> fetchFolder(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser,
            @RequestParam(name = "search", required = false) final String prefix
    ) {
        var path = extractPathFrom(request.getRequestURI());

        if (prefix != null) {
            LOG.info("trying to search by query...");
            var searchResult = path.equals("/")
                    ? folderSearchUsecase.invoke(new RootSearchDto(activeUser.id(), prefix))
                    : folderSearchUsecase.invoke(new FolderSearchDto(activeUser.id(), path, prefix));

            LOG.info("sending search result...");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(OkMessage.from(HttpStatus.OK, searchResult));
        }

        LOG.info("trying to fetch folder content...");
        var folderContent = path.equals("/")
                ? fetchFolderUsecase.invoke(activeUser.id())
                : fetchFolderUsecase.invoke(new FolderDto(activeUser.id(), path));

        LOG.info("sending folder content...");
        return ResponseEntity.status(HttpStatus.OK)
                .body(OkMessage.from(HttpStatus.OK, folderContent));
    }

    private String extractPathFrom(final String uri) {
        return uri.equals("/api/v1/storage/folders")
                ? "/"
                : uri.replaceFirst("/api/v1/storage/folders/", "/");
    }
}
