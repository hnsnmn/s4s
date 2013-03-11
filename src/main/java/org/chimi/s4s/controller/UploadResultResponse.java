package org.chimi.s4s.controller;

import org.chimi.s4s.fileservice.UploadResult;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * 업로드 결과 정보를 뷰에 전달하기 위해 사용된다.
 * 
 * @author Choi Beom Kyun
 */
@JsonPropertyOrder(value = { "success", "code", "fileName", "fileId",
		"metadataId", "mimeType", "image", "length", "fileUrl", "imageUrl" })
public class UploadResultResponse {

	public static UploadResultResponse createSuccessResult(UploadResult result,
			String fileUrl, String imageUrl) {
		return new UploadResultResponse(UploadResultCode.SUCCESS, result,
				fileUrl, imageUrl);
	}

	public static UploadResultResponse createNoFileResult() {
		return new UploadResultResponse(UploadResultCode.NOFILE, null, null,
				null);
	}

	public static UploadResultResponse createErrorResult() {
		return new UploadResultResponse(UploadResultCode.ERROR, null, null,
				null);
	}

	private UploadResultCode code;
	private UploadResult uploadResult;
	private String fileUrl;
	private String imageUrl;

	public UploadResultResponse(UploadResultCode success,
			UploadResult uploadResult, String fileUrl, String imageUrl) {
		this.code = success;
		this.uploadResult = uploadResult;
		this.fileUrl = fileUrl;
		this.imageUrl = imageUrl;
	}

	public boolean isSuccess() {
		return code == UploadResultCode.SUCCESS;
	}

	public UploadResultCode getCode() {
		return code;
	}

	public String getFileId() {
		return uploadResult == null ? null : uploadResult.getFileId();
	}

	public String getFileName() {
		return uploadResult == null ? null : uploadResult.getFileName();
	}

	public long getLength() {
		return uploadResult == null ? 0 : uploadResult.getLength();
	}

	public String getMetadataId() {
		return uploadResult == null ? null : uploadResult.getMetadataId();
	}

	public String getMimeType() {
		return uploadResult == null ? null : uploadResult.getMimeType();
	}

	public boolean isImage() {
		return uploadResult == null ? false : uploadResult.isImage();
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public static enum UploadResultCode {
		SUCCESS, NOFILE, ERROR
	}

}
