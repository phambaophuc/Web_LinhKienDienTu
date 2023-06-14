package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.controller.AdminController;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.repository.IProductRepository;
import DoAnJava.LinhKienDienTu.utils.FileUploadUlti;
import DoAnJava.LinhKienDienTu.utils.S3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại.");
        }
    }

    public List<Product> getProductByName(String productName) {
        List<Product> products = productRepository.findProductByName(productName);
        return products;
    }

    public List<Product> getProductByCategory(String categoryName) {
        List<Product> products = productRepository.findProductByCategory(categoryName);
        return products;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void uploadFileAWS(Product product, MultipartFile multipartFile,
                              MultipartFile[] multipartFiles, String uploadDir, boolean exist) throws IOException {

        if (exist) {
            Product currentProduct = productRepository.findById(product.getProductId()).orElse(null);
            boolean isMainImageUpdated = multipartFile != null && !multipartFile.isEmpty();

            if (isMainImageUpdated) {
                String mainImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                product.setMainImage(mainImageName);
                try {
                    S3Util.uploadFile(mainImageName, multipartFile.getInputStream());
                    logger.info("File " + mainImageName + " has been uploaded successfully!");
                } catch (Exception ex) {
                    logger.error("Error: " + ex.getMessage());
                }
            } else {
                product.setMainImage(currentProduct.getMainImage());
            }

            int count = 0;
            for (MultipartFile extraMultipart : multipartFiles) {
                if (!extraMultipart.isEmpty()) {
                    String extraImageName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
                    if (count == 0) product.setExtraImage1(extraImageName);
                    if (count == 1) product.setExtraImage2(extraImageName);
                    if (count == 2) product.setExtraImage3(extraImageName);

                    try {
                        S3Util.uploadFile(extraImageName, extraMultipart.getInputStream());
                        System.out.println("File " + extraImageName + " has been uploaded successfully!");
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }

                    count++;
                } else {
                    product.setExtraImage1(currentProduct.getExtraImage1());
                    product.setExtraImage2(currentProduct.getExtraImage2());
                    product.setExtraImage3(currentProduct.getExtraImage3());
                    break;
                }
            }
        } else {
            String mainImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(mainImageName);

            FileUploadUlti.saveFile(uploadDir, multipartFile, mainImageName);
            try {
                S3Util.uploadFile(mainImageName, multipartFile.getInputStream());
                logger.info("File " + mainImageName + " has been uploaded successfully!");
            } catch (Exception ex) {
                logger.error("Error: " + ex.getMessage());
            }

            int count = 0;
            for (MultipartFile extraMultipart : multipartFiles) {
                if (!extraMultipart.isEmpty()) {
                    String extraImageName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
                    if (count == 0) product.setExtraImage1(extraImageName);
                    if (count == 1) product.setExtraImage2(extraImageName);
                    if (count == 2) product.setExtraImage3(extraImageName);

                    FileUploadUlti.saveFile(uploadDir, extraMultipart, extraImageName);

                    try {
                        S3Util.uploadFile(extraImageName, extraMultipart.getInputStream());
                        logger.info("File " + extraImageName + " has been uploaded successfully!");
                    } catch (Exception ex) {
                        logger.error("Error: " + ex.getMessage());
                    }
                    count++;
                }
            }
        }
    }
}
