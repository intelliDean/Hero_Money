package com.loan.hero.cloud_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CloudinaryServiceImplTest {

    @Autowired
    private CloudService cloudService;

    private MockMultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        file =
                new MockMultipartFile(
                        "myself",
                        new FileInputStream("C:\\Users\\Dean\\hero\\src\\main\\resources\\p1wbidsbjpnwgtcdtla8.jpg")
                );
    }

    @Test
    void uploadFileTest() {
        final String cloudinaryImageUrl = cloudService.uploadFile(file);
        assertThat(cloudinaryImageUrl).isNotNull();
    }
}