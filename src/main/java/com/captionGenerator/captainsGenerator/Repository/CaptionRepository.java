package com.captionGenerator.captainsGenerator.Repository;

import com.captionGenerator.captainsGenerator.Entity.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, Long> {
}