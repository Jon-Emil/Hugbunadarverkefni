package is.hi.hbv501g.hbv1.extras.helpers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        )
        );
    }

    //Saves game cover images to game_catalog folder on cloudinary
    public String uploadGameImage(MultipartFile file) {
        try{
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", "game_catalog"
            );
            Map upload = cloudinary.uploader().upload(file.getBytes(), options);
            return upload.get("secure_url").toString();
        }catch (IOException e){
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    //saves user profile pictures to game_catalog_users folder on cloudinary
    public String uploadUserImage(MultipartFile file) {
        try{
            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", "game_catalog_users"
            );
            Map upload = cloudinary.uploader().upload(file.getBytes(), options);
            return upload.get("secure_url").toString();
        }catch (IOException e){
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}
