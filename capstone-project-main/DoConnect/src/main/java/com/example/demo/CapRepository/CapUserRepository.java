package com.example.demo.CapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.CapEntity.CapUser;


@Repository
public interface CapUserRepository extends JpaRepository<CapUser, Long>
{
	public CapUser findByEmail(String email);
}
