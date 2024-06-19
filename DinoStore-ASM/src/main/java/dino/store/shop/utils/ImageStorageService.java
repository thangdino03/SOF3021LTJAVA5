package dino.store.shop.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
	private final String UPLOAD_DIR = "uploads";

    public String saveImage(MultipartFile imageFile) throws IOException {
        // Tạo thư mục upload nếu chưa tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Tạo tên ngẫu nhiên cho tệp
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        // Lưu trữ tệp vào thư mục upload
        Path filePath = Paths.get(UPLOAD_DIR + File.separator + fileName);
        Files.write(filePath, imageFile.getBytes());

        // Trả về đường dẫn của tệp đã lưu
        return filePath.toString();
    }
}
