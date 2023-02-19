package com.example.demo.CapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.CapEntity.CapAdmin;

@Repository
public interface CapAdminRepository extends JpaRepository<CapAdmin, Long>
{
	public CapAdmin findByEmail(String email);
}
