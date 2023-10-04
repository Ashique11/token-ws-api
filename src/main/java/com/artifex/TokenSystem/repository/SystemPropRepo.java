package com.artifex.TokenSystem.repository;

import com.artifex.TokenSystem.entity.AudioProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemPropRepo extends JpaRepository<AudioProperties, Integer> {
}
