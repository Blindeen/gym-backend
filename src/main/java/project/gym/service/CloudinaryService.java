package project.gym.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud.name}") String cloudName,
            @Value("${cloudinary.api.key}") String apiKey,
            @Value("${cloudinary.api.secret}") String apiSecret) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    public String uploadImage(MultipartFile multipartFile, String publicID) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
                "public_id", publicID,
                "resource_type", "image",
                "overwrite", true
        );
        Map<String, Object> response = cloudinary.uploader().upload(multipartFile.getBytes(), params);
        return (String) response.get("url");
    }

}
