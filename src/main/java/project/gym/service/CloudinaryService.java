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
    private final static String CLOUD_NAME_KEY = "cloud_name";
    private final static String API_KEY_KEY = "api_key";
    private final static String API_SECRET_KEY = "api_secret";
    private final static String SECURE_KEY = "secure";

    private final static String PUBLIC_ID_KEY = "public_id";
    private final static String RESOURCE_TYPE_KEY = "resource_type";
    private final static String OVERWRITE_KEY = "overwrite";
    private final static String RESOURCE_TYPE_IMAGE = "image";
    private final static String FOLDER_KEY = "folder";
    private final static String FOLDER_VALUE = "members/avatar";

    private final static String SECURE_URL_KEY = "secure_url";

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud.name}") String cloudName,
            @Value("${cloudinary.api.key}") String apiKey,
            @Value("${cloudinary.api.secret}") String apiSecret) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME_KEY, cloudName,
                API_KEY_KEY, apiKey,
                API_SECRET_KEY, apiSecret,
                SECURE_KEY, true));
    }

    @SuppressWarnings("unchecked")
    public String uploadImage(MultipartFile multipartFile, String publicID) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
                PUBLIC_ID_KEY, publicID,
                RESOURCE_TYPE_KEY, RESOURCE_TYPE_IMAGE,
                FOLDER_KEY, FOLDER_VALUE,
                OVERWRITE_KEY, true);
        Map<String, Object> response = cloudinary.uploader().upload(multipartFile.getBytes(), params);
        return (String) response.get(SECURE_URL_KEY);
    }

}
