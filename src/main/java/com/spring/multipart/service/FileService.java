package com.spring.multipart.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.multipart.dto.FileDto;
import com.spring.multipart.model.FileOperations;

public interface FileService {

	public List<FileOperations> uploadFile(MultipartFile[] multipartFiles);

	public FileDto downloadFileById(int id);

	public String deleteById(int id);

}
