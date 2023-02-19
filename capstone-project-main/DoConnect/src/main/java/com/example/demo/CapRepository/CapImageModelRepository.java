package com.example.demo.CapRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.CapEntity.CapImage;

@Repository
public interface CapImageModelRepository extends JpaRepository<CapImage, Long>
{
	public Optional<CapImage> findByName(String name);
}
