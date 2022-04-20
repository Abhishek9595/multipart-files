package com.spring.multipart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.multipart.dto.FileDto;
import com.spring.multipart.model.FileOperations;
import com.spring.multipart.response.ResponseMessage;
import com.spring.multipart.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class FileResource {

	@Autowired
	private FileService service;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> upload(@RequestParam("files") MultipartFile[] multipartFiles) {
		List<FileOperations> uploadFile = service.uploadFile(multipartFiles);
		if (uploadFile != null) {
			log.info("File Uploaded Successfully{}" + uploadFile);
			return new ResponseEntity<>(new ResponseMessage("success", "Files Uploaded Successfully", uploadFile),
					HttpStatus.OK);
		}
		log.error("File Not Uploaded");
		return new ResponseEntity<>(new ResponseMessage("failed", "File Not Uploaded"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> download(@PathVariable int id) {
		FileDto downloadFile = service.downloadFileById(id);
		if (downloadFile != null) {
			log.info("File Downloaded{}" + downloadFile.getResource());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(downloadFile.getFileType()))
					.body(downloadFile.getResource());
		}
		log.error("File Not Downloaded");
		return new ResponseEntity<>(new ResponseMessage("failed", "File Not Downloaded"), HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable int id) {
		String delete = service.deleteById(id);
		if (delete.equalsIgnoreCase("success")) {
			log.info("File Deleted Successfully");
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("success", "File Deleted Successfully"),
					HttpStatus.OK);
		} else {
			log.error("File Not Deleted");
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("failed", "File Not Deleted"),
					HttpStatus.NOT_FOUND);
		}

	}

}
