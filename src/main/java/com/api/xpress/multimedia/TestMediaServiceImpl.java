package com.api.xpress.multimedia;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@Profile("!dev")
public class TestMediaServiceImpl implements MultimediaService{


    @Override
    public String uploadFile(MultipartFile file) {

        return "File: %s is uploaded successfully".formatted(file.getName());
    }

    @Override
    public List<String> upload(Set<MultipartFile> images) {
        return List.of("Files uploaded successfully");
    }
}
