package spring.test.spring_demo.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/TP05")
public class ProductController {

    // Define the upload directory (inside the static folder)
    private final String locationUpload = "src/main/resources/static/uploads/";

    @Autowired
    private ProductRepository productRepository;

    // Display all products
    @GetMapping("/")
    public String getProduct(Model model) {
        List<ProductEntity> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("newProduct", new ProductEntity());
        return "Products/Product";
    }

    // Edit a product
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "Products/EditProduct";
        } else {
            model.addAttribute("newProduct", new ProductEntity());
        }
        return "Products/Product";
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/TP05/";
    }

    // Save a new product with image
    @PostMapping("/saveProduct")
    public String postProduct(@ModelAttribute("newProduct") ProductEntity newProduct,
                               @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            if (image != null && !image.isEmpty()) {
                // Ensure the upload directory exists
                Path uploadPath = Paths.get(locationUpload);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    System.out.println("Created upload directory: " + uploadPath.toAbsolutePath());
                }

                // Generate a unique file name to avoid conflicts
                String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.write(filePath, image.getBytes());

                // Log the file upload
                System.out.println("Uploaded image: " + uniqueFileName);
                System.out.println("Image saved at: " + filePath.toAbsolutePath());

                // Set the image URL in the product entity
                String imageUrl = "/uploads/" + uniqueFileName;
                newProduct.setImage(imageUrl);
            } else {
                System.out.println("No image uploaded for the product.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred during image upload: " + e.getMessage());
        }

        productRepository.save(newProduct);
        System.out.println("Product saved: " + newProduct);
        return "redirect:/TP05/";
    }

    // Update an existing product with image upload
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("product") ProductEntity updatedProduct,
                                @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<ProductEntity> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isPresent()) {
            ProductEntity existingProduct = existingProductOpt.get();

            try {
                if (image != null && !image.isEmpty()) {
                    // Ensure the upload directory exists
                    Path uploadPath = Paths.get(locationUpload);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // Save the file
                    String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(uniqueFileName);
                    Files.write(filePath, image.getBytes());

                    // Update the image URL
                    existingProduct.setImage("/uploads/" + uniqueFileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Update other fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCode(updatedProduct.getCode());
            existingProduct.setCost(updatedProduct.getCost());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCountry(updatedProduct.getCountry());
            existingProduct.setDescription(updatedProduct.getDescription());

            productRepository.save(existingProduct);
        }

        return "redirect:/TP05/";
    }
}
