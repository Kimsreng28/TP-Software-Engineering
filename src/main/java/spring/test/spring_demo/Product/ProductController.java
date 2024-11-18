package spring.test.spring_demo.Product;

import java.io.File;
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
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String getProduct(Model model) {
        List<ProductEntity> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("newProduct", new ProductEntity());
        return "Products/Product";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if(product.isPresent()){
            model.addAttribute("product", product.get());
            return "Products/EditProduct";
        }else {
            model.addAttribute("newProduct", new ProductEntity());
        }
        return "Products/Product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/TP05/";
    }
    
    
    @PostMapping("/saveProduct")
    public String postProduct(@ModelAttribute("newProduct") ProductEntity newProduct, 
                            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        productRepository.save(newProduct);

        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.home") + "/uploads/product-images/" + newProduct.getId();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, image.getBytes());

                newProduct.setImage(fileName);
                productRepository.save(newProduct); 

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/TP05/";
    }



    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, 
                                @ModelAttribute("product") ProductEntity updatedProduct, 
                                @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<ProductEntity> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isPresent()) {
            ProductEntity existingProduct = existingProductOpt.get();

            // Handle image upload
            if (!image.isEmpty()) {
                try {
                    String uploadDir = "/uploads";  
                    String fileName = image.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.write(filePath, image.getBytes());  
                    
                    existingProduct.setImage("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            }

            // Update other fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCode(updatedProduct.getCode());
            existingProduct.setCost(updatedProduct.getCost());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCountry(updatedProduct.getCountry());
            existingProduct.setDescription(updatedProduct.getDescription());

            // Save updated product
            productRepository.save(existingProduct);
        }

        return "redirect:/TP05/"; 
    }
}
