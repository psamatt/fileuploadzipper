package com.psamatt.fileuploadzipper.domain;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileConverter {

    public Collection<File> toFiles(Collection<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(FileConverter::toFile).collect(Collectors.toList());
    }

    public static File toFile(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("tmp-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
