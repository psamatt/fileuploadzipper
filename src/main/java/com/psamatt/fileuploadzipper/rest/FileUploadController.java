package com.psamatt.fileuploadzipper.rest;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.psamatt.fileuploadzipper.domain.FileConverter;
import com.psamatt.fileuploadzipper.domain.FileZipper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    private final FileConverter converter;
    private final FileZipper fileZipper;

    public FileUploadController(FileConverter converter, FileZipper fileZipper) {
        this.converter = converter;
        this.fileZipper = fileZipper;
    }

    @PostMapping(
            value = "/fileupload",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = "application/zip")
    public byte[] fileupload(@RequestPart("files") MultipartFile[] multipartFiles) {
        Collection<File> files = converter.toFiles(Arrays.asList(multipartFiles));
        ByteArrayOutputStream bytes = fileZipper.zip(files);
        return bytes.toByteArray();
    }

    @ExceptionHandler({UncheckedIOException.class})
    public ResponseEntity<Object> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error");
    }
}
