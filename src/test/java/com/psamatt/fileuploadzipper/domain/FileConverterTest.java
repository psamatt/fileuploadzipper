package com.psamatt.fileuploadzipper.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileConverterTest {

    private FileConverter converter;

    @BeforeEach
    void setUp() {
        converter = new FileConverter();
    }

    @Test
    void shouldConvertMultipleFiles() throws IOException {
        MockMultipartFile mock1 = buildFile("file1.txt", "File 1");
        MockMultipartFile mock2 = buildFile("file2.txt", "File 2");

        Collection<File> files = converter.toFiles(Arrays.asList(mock1, mock2));

        assertFileHasContents(files, 0, "File 1");
        assertFileHasContents(files, 1, "File 2");
    }

    private void assertFileHasContents(Collection<File> files, int index, String contents)
            throws IOException {
        File file = files.toArray(new File[] {})[index];
        String actual = FileUtils.readFileToString(file, Charset.defaultCharset());

        assertThat(actual).isEqualTo(contents);
    }

    public static MockMultipartFile buildFile(String filename, String contents) {
        return new MockMultipartFile(filename, contents.getBytes(StandardCharsets.UTF_8));
    }
}
