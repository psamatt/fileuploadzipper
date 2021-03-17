package com.psamatt.fileuploadzipper.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileZipper {

    public ByteArrayOutputStream zip(Collection<File> files) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            zip(files, byteOutputStream);
            return byteOutputStream;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void zip(Collection<File> files, ByteArrayOutputStream byteOutputStream)
            throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream);
        for (File file : files) {
            try {
                zip(file, zipOutputStream);
            } catch (UnableToAddFileToArchiveException e) {
                log.warn("Unable to add file to archive", e);
            }
        }
        zipOutputStream.close();
    }

    private static void zip(File file, ZipOutputStream zipOutputStream) {
        try {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, zipOutputStream);
            fileInputStream.close();
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new UnableToAddFileToArchiveException(file);
        }
    }
}
