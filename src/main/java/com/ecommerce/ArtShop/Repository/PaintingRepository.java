package com.ecommerce.ArtShop.Repository;

import com.ecommerce.ArtShop.Model.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PaintingRepository extends JpaRepository<Painting, Long>, JpaSpecificationExecutor<Painting> {
}
