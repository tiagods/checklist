package com.prolink.model;

import java.io.Serializable;

public class Model implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private String[] updateType;
	private String httpUrl;
	private String smbDirectory;
	private String ftpHost;
	private String ftpPort;
	private String ftpUser="prolinkcontabil";
	private String ftpPassword="plk*link815";
	private String ftpDirectory;
	private String execName;
	private String onOpen;
	private String onClose;
	private String fT;
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the updateType
	 */
	public String[] getUpdateType() {
		return updateType;
	}
	/**
	 * @param updateType the updateType to set
	 */
	public void setUpdateType(String[] updateType) {
		this.updateType = updateType;
	}
	/**
	 * @return the httpUrl
	 */
	public String getHttpUrl() {
		return httpUrl;
	}
	/**
	 * @param httpUrl the httpUrl to set
	 */
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	/**
	 * @return the smbDirectory
	 */
	public String getSmbDirectory() {
		return smbDirectory;
	}
	/**
	 * @param smbDirectory the smbDirectory to set
	 */
	public void setSmbDirectory(String smbDirectory) {
		this.smbDirectory = smbDirectory;
	}
	/**
	 * @return the ftpHost
	 */
	public String getFtpHost() {
		return ftpHost;
	}
	/**
	 * @param ftpHost the ftpHost to set
	 */
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}
	/**
	 * @return the ftpPort
	 */
	public String getFtpPort() {
		return ftpPort;
	}
	/**
	 * @param ftpPort the ftpPort to set
	 */
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	/**
	 * @return the ftpUser
	 */
	public String getFtpUser() {
		return ftpUser;
	}
	/**
	 * @param ftpUser the ftpUser to set
	 */
	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	/**
	 * @return the ftpPassword
	 */
	public String getFtpPassword() {
		return ftpPassword;
	}
	/**
	 * @param ftpPassword the ftpPassword to set
	 */
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	/**
	 * @return the ftpDirectory
	 */
	public String getFtpDirectory() {
		return ftpDirectory;
	}
	/**
	 * @param ftpDirectory the ftpDirectory to set
	 */
	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
	/**
	 * @return the executavelName
	 */
	public String getExecName() {
		return execName;
	}
	/**
	 * @param executavelName the executavelName to set
	 */
	public void setExecName(String execName) {
		this.execName = execName;
	}
	/**
	 * @return the onOpen
	 */
	public String getOnOpen() {
		return onOpen;
	}
	/**
	 * @param onOpen the onOpen to set
	 */
	public void setOnOpen(String onOpen) {
		this.onOpen = onOpen;
	}
	/**
	 * @return the onClose
	 */
	public String getOnClose() {
		return onClose;
	}
	/**
	 * @param onClose the onClose to set
	 */
	public void setOnClose(String onClose) {
		this.onClose = onClose;
	}
	/**
	 * @return the fT
	 */
	public String getfT() {
		return fT;
	}
	/**
	 * @param fT the fT to set
	 */
	public void setfT(String fT) {
		this.fT = fT;
	}
	
}
