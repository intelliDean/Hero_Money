package com.api.xpress.multimedia;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.api.xpress.exceptions.XpressException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements MultimediaService {


    private final Cloudinary cloudinary;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    @Override
    public String uploadFile(MultipartFile image) {
        validateImage(image);
        try {
            Map<?, ?> response = cloudinary.uploader()
                    .upload(image.getBytes(), buildUploadOptions());
            return Optional.ofNullable(response.get("secure_url"))
                    .map(Object::toString)
                    .orElseThrow(() -> new XpressException("Failed to get URL from Cloudinary"));
        } catch (IOException ex) {
            throw new XpressException("Image upload failed: " + ex.getMessage());
        }
    }

    @Override
    public List<String> upload(Set<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new XpressException("Images set cannot be null or empty");
        }
        return images.stream()
                .map(this::uploadFile) // reuses single upload ✅
                .toList();
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new XpressException("Image file cannot be null or empty");
        }
        final String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new XpressException(
                    "Invalid file type. Allowed: JPEG, PNG, WEBP, GIF"
            );
        }
    }

    private Map<String, Object> buildUploadOptions() {
        return ObjectUtils.asMap(
                "folder", "artezan",
                "resource_type", "image",
                "quality", "auto",
                "fetch_format", "auto"
        );
    }
}
