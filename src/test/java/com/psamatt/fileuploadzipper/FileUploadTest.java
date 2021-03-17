package com.psamatt.fileuploadzipper;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileUploadTest {

    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;

    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    void shouldReturnZippedFiles(@TempDir Path directory) throws IOException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = twoFiles();

        byte[] bytes = restTemplate.postForObject(buildUrl(), requestEntity, byte[].class);

        ZipFile zipFile = toZipArchive(directory, bytes);
        Iterator<? extends ZipEntry> iterator = zipFile.entries().asIterator();
        assertThatFileHasContents(zipFile, iterator.next(), "Foo Bar");
        assertThatFileHasContents(zipFile, iterator.next(), "Hello World");
    }

    private static ZipFile toZipArchive(Path directory, byte[] bytes) throws IOException {
        String zipDir = directory.toString() + "/test.zip";
        OutputStream outputStream = new FileOutputStream(zipDir);
        outputStream.write(bytes);
        outputStream.close();
        return new ZipFile(zipDir);
    }

    private static void assertThatFileHasContents(ZipFile zipFile, ZipEntry entry, String contents)
            throws IOException {
        InputStream inputStream = zipFile.getInputStream(entry);
        String actual = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());

        assertThat(actual).isEqualTo(contents);
    }

    private static HttpEntity<MultiValueMap<String, Object>> twoFiles() {
        MultiValueMap<String, Object> body = buildFormData();
        HttpHeaders headers = buildHeaders();
        return new HttpEntity<>(body, headers);
    }

    private static HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MULTIPART_FORM_DATA);
        return headers;
    }

    private static MultiValueMap<String, Object> buildFormData() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("files", new ClassPathResource("file1.txt"));
        body.add("files", new ClassPathResource("file2.txt"));
        return body;
    }

    private String buildUrl() {
        return format("http://localhost:%s/fileupload", port);
    }
}
