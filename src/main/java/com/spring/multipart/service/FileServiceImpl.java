package com.spring.multipart.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.multipart.dto.FileDto;
import com.spring.multipart.model.FileOperations;
import com.spring.multipart.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileRepository repository;

	private Path directory;

	private Path getPath(String filename) {
		String path = "D:\\multipart-files";
		String dir = path + "\\" + filename;
		directory = Paths.get(dir).toAbsolutePath().normalize();
		return directory;
	}

	@Override
	public List<FileOperations> uploadFile(MultipartFile[] multipartFiles) {
		if (multipartFiles != null) {
			List<FileOperations> fileList = new ArrayList<>();
			for (MultipartFile file : multipartFiles) {
				directory = getPath(file.getOriginalFilename());
				Path path2 = directory.resolve(file.getOriginalFilename());
				try {
					Files.createDirectories(directory);
					Files.copy(file.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileOperations fileOperations = new FileOperations();
				fileOperations.setFileName(file.getOriginalFilename());
				fileOperations.setFileType(file.getContentType());
				fileOperations.setFilrUrl(path2.toString());
				FileOperations saveFile = repository.save(fileOperations);
				fileList.add(saveFile);
			}
			return fileList;
		}
		return Collections.emptyList();
	}

	@Override
	public FileDto downloadFileById(int id) {
		FileDto fileDto = new FileDto();
		FileOperations fileOperations = repository.findByFileId(id);
		directory = Paths.get(fileOperations.getFilrUrl()).toAbsolutePath().normalize();
		Resource resource = null;
		try {
			resource = new UrlResource(directory.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		fileDto.setFileType(fileOperations.getFileType());
		fileDto.setResource(resource);
		return fileDto;
	}

	@Override
	public String deleteById(int id) {
		FileOperations file = repository.findByFileId(id);
		this.directory = getPath(file.getFileName());
		try {
			FileUtils.forceDelete(new File(this.directory.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		repository.deleteById(id);
		return "Success";
	}

}
