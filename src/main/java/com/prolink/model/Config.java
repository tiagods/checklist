package com.prolink.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	public boolean readFile(Model model){
		FileInputStream stream;
		Properties props ;
		try{
			props = new Properties();
			stream = new FileInputStream("config");
			props.load(stream);
			model.setFileName(props.getProperty("FileName").trim());
			model.setUpdateType(props.getProperty("UpdateType").trim().split(","));
			//url session
			model.setHttpUrl(props.getProperty("HttpUrl").trim());
			//smb session
			model.setSmbDirectory(props.getProperty("SMBDirectory").trim());
			//ftp session
			model.setFtpHost(props.getProperty("FTPHost").trim());
			model.setFtpPort(props.getProperty("FTPPort"));
			//model.setFtpUser(props.getProperty("FTPUser").trim());
			//model.setFtpPassword(props.getProperty("FTPPassword").trim());
			model.setFtpDirectory(props.getProperty("FTPDirectory").trim());
			//scrips session
			model.setExecName(props.getProperty("ExecName").trim());
			model.setOnOpen(props.getProperty("OnOpen").trim());
			model.setOnClose(props.getProperty("OnClose").trim());
			model.setfT(props.getProperty("FT").trim());
			return true;
		}catch(IOException e){
			return false;
		}
	}
}
