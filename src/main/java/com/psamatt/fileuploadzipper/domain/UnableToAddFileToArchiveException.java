package com.psamatt.fileuploadzipper.domain;

import java.io.File;

public class UnableToAddFileToArchiveException extends RuntimeException {
    private final File file;

    public UnableToAddFileToArchiveException(File file) {
        super(String.format("Unable to add file [%s] to archive", file.getName()));
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
