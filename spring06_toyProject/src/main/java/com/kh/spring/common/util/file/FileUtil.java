package com.kh.spring.common.util.file;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.common.code.Config;
import com.kh.spring.common.code.ErrorCode;
import com.kh.spring.common.exception.HandlableException;

public class FileUtil {

	public FileDTO fileUpload(MultipartFile file) {
		FileDTO fileDTO = null;
		try {
			String uploadPath = createUploadPath(createSubPath());
			fileDTO = createFileDTO(file);
			File dest = new File(uploadPath + fileDTO.getRenameFileName());
			file.transferTo(dest);
		} catch (IllegalStateException | IOException e) {
			throw new HandlableException(ErrorCode.FAILED_FILE_UPLOAD_ERROR);
		}
		return fileDTO;
	}
	
	private String createSubPath() {
		//2. 파일 업로드 날짜 기준으로 저장될 파일 경록 생성
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DAY_OF_MONTH);
		
		return year + "/" + month + "/" + date + "/";
	}
	
	private String createUploadPath(String subPath) {
		String uploadPath = Config.UPLOAD_PATH.DESC.toString() + subPath;
		
		File dir = new File(uploadPath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		return uploadPath;
	}
	
	private FileDTO createFileDTO(MultipartFile filePart) {
		String renameFileName = UUID.randomUUID().toString();
		String originFileName = filePart.getOriginalFilename();
		
		if(originFileName.contains(".")) {
			renameFileName = renameFileName + originFileName.substring(originFileName.lastIndexOf("."));
		}
		String savePath = createSubPath();
		
		FileDTO fileDTO = new FileDTO();
		fileDTO.setOriginFileName(originFileName);
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);
		
		return fileDTO;
	}
}
