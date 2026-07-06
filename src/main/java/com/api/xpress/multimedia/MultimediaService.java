package com.api.xpress.multimedia;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MultimediaService {

    String uploadFile(MultipartFile file);

    List<String> upload(Set<MultipartFile> images);
}
