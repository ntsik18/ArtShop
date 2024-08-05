package com.ecommerce.ArtShop.Controller;


import com.ecommerce.ArtShop.DTO.PaintingDTO.NewPaintingResponse;
import com.ecommerce.ArtShop.DTO.PaintingDTO.PaintingRequest;
import com.ecommerce.ArtShop.Service.ImageService;
import com.ecommerce.ArtShop.Service.PageResponse;
import com.ecommerce.ArtShop.Service.PaintingService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/paintings")
public class PaintingController {
    private final PaintingService paintingService;
    private final ImageService imageService;


    @GetMapping("/search/paintings")
    public ResponseEntity<PageResponse<NewPaintingResponse>> findByCriteria(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "userName", required = false) String name

    ) {
        if (text != null) {
            return ResponseEntity.ok(paintingService.findLike(page, size, text));
        }
        if (name != null) {
            return ResponseEntity.ok(paintingService.findByUser(page, size, text));
        } else
            return ResponseEntity.ok(paintingService.findAllPaintings(page, size));

    }

    @GetMapping("/search/all")
    public ResponseEntity<PageResponse<NewPaintingResponse>> findAllPaintings(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(paintingService.findAllPaintings(page, size));
    }

    @GetMapping("{painting-id}")
    public ResponseEntity<NewPaintingResponse> findById(
            @PathVariable("painting-id") Long paintingId) {
        return ResponseEntity.ok(paintingService.findById(paintingId));
    }


    @PostMapping(value = {"/new"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> addNew(@RequestPart("painting")
                                       PaintingRequest request,
                                       @RequestPart("image") MultipartFile file,
                                       Authentication connectedUser) throws IOException {
        return ResponseEntity.ok(paintingService.save(request, file, connectedUser));
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> deleteById (@PathVariable Long id, Authentication connectedUser) {
        paintingService.delete(id, connectedUser);
        return ResponseEntity.ok().build();
    }

}
