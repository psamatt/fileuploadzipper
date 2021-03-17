package com.psamatt.fileuploadzipper.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UnableToAddFileToArchiveExceptionTest {

    @TempDir Path directory;

    @Test
    void shouldHaveMessage() throws IOException {
        File file = buildFile();

        UnableToAddFileToArchiveException exception = new UnableToAddFileToArchiveException(file);

        assertThat(exception.getMessage()).isEqualTo("Unable to add file [myfile.txt] to archive");
    }

    @Test
    void shouldCreateExceptionWithFile() throws IOException {
        File file = buildFile();

        UnableToAddFileToArchiveException exception = new UnableToAddFileToArchiveException(file);

        assertThat(exception.getFile()).isEqualTo(file);
    }

    private File buildFile() throws IOException {
        return Files.createFile(directory.resolve("myfile.txt")).toFile();
    }
}
