package com.ecommerce.ArtShop.Service;


import com.ecommerce.ArtShop.DTO.PaintingDTO.NewPaintingResponse;
import com.ecommerce.ArtShop.Mapper.PaintingMapper;
import com.ecommerce.ArtShop.DTO.PaintingDTO.PaintingRequest;
import com.ecommerce.ArtShop.Specification.PaintingSpec;
import com.ecommerce.ArtShop.Model.ImageModel;
import com.ecommerce.ArtShop.Model.Painting;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.PaintingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final PaintingMapper paintingMapper;
    private final FileStorageService fileStorageService;
    private final ImageService imageService;



    public Long save(PaintingRequest request, MultipartFile file, Authentication connectedUser) throws IOException {
        User user = (User) connectedUser.getPrincipal();
        Painting painting = paintingMapper.toPainting(request);
        painting.setUser(user);
        paintingRepository.save(painting);
        ImageModel imageModel = imageService.uploadImages(file, painting);
        imageModel.setPath(fileStorageService.saveFile(file, user.getId()));
        painting.setFinalImage(imageModel);
        paintingRepository.save(painting);
        return painting.getId();

    }

    public NewPaintingResponse findById(Long paintingId) {
        return paintingRepository.findById(paintingId)
                .map(paintingMapper::toNewPaintingResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID " + paintingId));
    }

    public PageResponse<NewPaintingResponse> findAllPaintings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Painting> paintings = paintingRepository.findAll(pageable);
        List<NewPaintingResponse> paintingResponses = paintings.stream()
                .map(paintingMapper::toNewPaintingResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(paintingResponses,
                paintings.getNumber(),
                paintings.getSize(),
                paintings.getTotalElements(),
                paintings.getTotalPages(),
                paintings.isFirst(),
                paintings.isLast()
        );
    }

    public PageResponse<NewPaintingResponse> findLike(int page, int size, String text) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Painting> paintings = paintingRepository.findAll(PaintingSpec.containsText(text), pageable);
        List<NewPaintingResponse> paintingResponses = paintings.stream()
                .map(paintingMapper::toNewPaintingResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(paintingResponses,
                paintings.getNumber(),
                paintings.getSize(),
                paintings.getTotalElements(),
                paintings.getTotalPages(),
                paintings.isFirst(),
                paintings.isLast()
        );
    }

    public PageResponse<NewPaintingResponse> findByUser(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Painting> paintings = paintingRepository.findAll(PaintingSpec.containsUser(name), pageable);
        List<NewPaintingResponse> paintingResponses = paintings.stream()
                .map(paintingMapper::toNewPaintingResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(paintingResponses,
                paintings.getNumber(),
                paintings.getSize(),
                paintings.getTotalElements(),
                paintings.getTotalPages(),
                paintings.isFirst(),
                paintings.isLast()
        );
    }


    public List<NewPaintingResponse> findAllPaintings() {
        List<Painting> paintings = paintingRepository.findAll();
        List<NewPaintingResponse> paintingResponses = paintings.stream()
                .map(paintingMapper::toNewPaintingResponse)
                .collect(Collectors.toList());
        return paintingResponses;
    }
    @Transactional
    public void delete(Long id, Authentication connectedUser) {
        User currentUser = (User) connectedUser.getPrincipal();
        Optional<Painting> paintingOpt = paintingRepository.findById(id);

        if (paintingOpt.isPresent()) {
            Painting painting = paintingOpt.get();
            if (painting.getUser().getId()==(currentUser.getId())) {
                paintingRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("You do not have permission to delete this painting.");
            }
        } else {
            throw new IllegalArgumentException("Painting not found.");
        }
    }


}
