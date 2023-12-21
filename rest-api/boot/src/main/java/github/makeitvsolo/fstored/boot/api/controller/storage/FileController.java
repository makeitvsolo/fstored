package github.makeitvsolo.fstored.boot.api.controller.storage;

import github.makeitvsolo.fstored.boot.api.message.ErrorMessage;
import github.makeitvsolo.fstored.boot.api.message.OkMessage;
import github.makeitvsolo.fstored.boot.config.spring.session.authentication.Authenticated;
import github.makeitvsolo.fstored.storage.application.usecase.file.FetchFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.MoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.RemoveFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.WriteFileUsecase;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.FileSourceDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.MoveFileDto;
import github.makeitvsolo.fstored.storage.application.usecase.file.dto.WriteMultipleFileDto;
import github.makeitvsolo.fstored.user.access.application.usecase.access.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/storage/files")
public class FileController {

    private final WriteFileUsecase<?> writeFileUsecase;
    private final MoveFileUsecase<?> moveFileUsecase;
    private final RemoveFileUsecase<?> removeFileUsecase;
    private final FetchFileUsecase<?> fetchFileUsecase;

    public FileController(
            final WriteFileUsecase<?> writeFileUsecase,
            final MoveFileUsecase<?> moveFileUsecase,
            final RemoveFileUsecase<?> removeFileUsecase,
            final FetchFileUsecase<?> fetchFileUsecase
    ) {
        this.writeFileUsecase = writeFileUsecase;
        this.moveFileUsecase = moveFileUsecase;
        this.removeFileUsecase = removeFileUsecase;
        this.fetchFileUsecase = fetchFileUsecase;
    }

    @PostMapping(value = "/**", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> writeFile(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser,
            @RequestPart(name = "file", required = false) final MultipartFile[] files,
            @RequestParam(name = "mvfrom", required = false) final String movedFrom
    ) throws IOException {
        var path = extractPathFrom(request.getRequestURI());

        if (movedFrom != null) {
            var payload = new MoveFileDto(activeUser.id(), movedFrom, path);

            moveFileUsecase.invoke(payload);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(OkMessage.from(HttpStatus.CREATED));
        }

        if (files != null) {
            var overwrite = false;

            uploadFiles(activeUser, path, files, overwrite);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(OkMessage.from(HttpStatus.CREATED));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.from(HttpStatus.BAD_REQUEST, "invalid payload"));
    }

    @PutMapping(value = "/**", consumes = "multipart/form-data")
    public ResponseEntity<?> overwriteFile(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser,
            @RequestPart(name = "file") final MultipartFile[] files
    ) throws IOException {
        var path = extractPathFrom(request.getRequestURI());
        var overwrite = true;

        uploadFiles(activeUser, path, files, overwrite);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(OkMessage.from(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping(value = "/**", consumes = "application/json")
    public ResponseEntity<?> removeFile(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser
    ) {
        var path = extractPathFrom(request.getRequestURI());
        var payload = new FileDto(activeUser.id(), path);

        removeFileUsecase.invoke(payload);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(OkMessage.from(HttpStatus.NO_CONTENT));
    }

    @GetMapping(value = "/**", consumes = "application/json")
    public ResponseEntity<?> fetchFile(
            final HttpServletRequest request,
            @Authenticated final UserDto activeUser
    ) {
        var path = extractPathFrom(request.getRequestURI());
        var payload = new FileDto(activeUser.id(), path);

        var file = fetchFileUsecase.invoke(payload);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", file.path())
                )
                .body(new InputStreamResource(file.stream()));
    }

    private String extractPathFrom(final String uri) {
        return uri.equals("/api/v1/storage/files")
                ? "/"
                : uri.replaceFirst("/api/v1/storage/files/", "/");
    }

    private void uploadFiles(
            final UserDto activeUser, final String path, final MultipartFile[] files, final boolean overwrite
    ) throws IOException {
        var sources = new ArrayList<FileSourceDto>(files.length);
        for (var file : files) {
            sources.add(new FileSourceDto(
                    file.getOriginalFilename(),
                    file.getInputStream(),
                    file.getSize()
            ));
        }

        var payload = new WriteMultipleFileDto(activeUser.id(), path, sources, overwrite);

        writeFileUsecase.invoke(payload);
    }
}