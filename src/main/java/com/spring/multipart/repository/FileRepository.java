package com.spring.multipart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.multipart.model.FileOperations;

public interface FileRepository extends JpaRepository<FileOperations, Integer> {

	public FileOperations findByFileId(int id);

}
