package com.loan.hero.cloud_service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.loan.hero.exceptions.HeroException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            final Map<?, ?> response = cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.emptyMap()
                    );
            return response.get("url").toString();
        } catch (IOException ex) {
            throw new HeroException(ex.getMessage());
        }
    }
}
