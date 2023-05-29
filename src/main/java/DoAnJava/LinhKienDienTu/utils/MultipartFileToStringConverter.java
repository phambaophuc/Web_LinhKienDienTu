package DoAnJava.LinhKienDienTu.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MultipartFileToStringConverter implements Converter<MultipartFile, String> {

    @Override
    public String convert(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Xử lý exception nếu cần thiết
        }
        return null;
    }
}
