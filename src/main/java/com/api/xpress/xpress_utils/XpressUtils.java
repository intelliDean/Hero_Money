package com.api.xpress.xpress_utils;

import com.api.xpress.exceptions.XpressException;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Base64;

public class XpressUtils {
    public static final String BEARER = "Bearer ";
    public static final String NOT_NULL = "Cannot be null";
    public static final String NOT_BLANK = "Cannot be blank";
    public static final int MAX_NUMBER_PER_PAGE = 5;

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private XpressUtils() {}

    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new XpressException("Uploaded file cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/jpg") || contentType.equals("image/png") || contentType.equals("application/pdf"))) {
            throw new XpressException("Unsupported file type. Only JPEG, PNG, and PDF files are allowed.");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new XpressException("File size exceeds the maximum limit of 5MB");
        }
    }

    public static final String VALID_PASSWORD =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%!*?^&+=])[A-Za-z\\d@#$%!*?^&+=]{8,}$";
    public static final String VALID_NUMBER = "^(?:\\+234|0)[7-9][01][0-9]{8}$";
}
