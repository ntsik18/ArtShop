package com.ecommerce.ArtShop.Service;


import com.ecommerce.ArtShop.Model.ImageModel;
import com.ecommerce.ArtShop.Model.Painting;
import com.ecommerce.ArtShop.Repository.ImageModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageModelRepo imageModelRepo;


    public ImageModel addNewImage(ImageModel imageModel) {
        return imageModelRepo.save(imageModel);
    }

    public ImageModel uploadImages(MultipartFile file, Painting painting) throws IOException {

        ImageModel imageModel = new ImageModel(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
        imageModel.setPainting(painting);
        addNewImage(imageModel);
        return imageModel;
    }


}
