package io.github.vhoyon.vramework.objects;

import java.io.*;
import java.nio.file.Files;

import io.github.vhoyon.vramework.interfaces.Auditable;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.res.FrameworkResources;

public class AuditableFile implements Auditable {
	
	private File auditableFile;
	
	private boolean hasSentWarning;
	
	public AuditableFile(String fileName, String folder){
		this(folder + (folder.endsWith(File.separator) ? "" : File.separator)
				+ fileName);
	}
	
	public AuditableFile(String filePath){
		this.auditableFile = new File(filePath);
		this.hasSentWarning = false;
	}
	
	public File getFile(){
		return this.auditableFile;
	}
	
	@Override
	public void audit(String auditText, boolean hasAppendedDate,
			boolean shouldPrependAudit){
		
		File auditFile = getFile();
		
		try{
			
			PrintWriter writer = handleAuditHeader(auditFile);
			
			writer.println(auditText);
			
			writer.close();
			
		}
		catch(IllegalAccessError e){
			sendErrorMessage(
					"The specified audit file is not owned by the bot.",
					auditFile);
		}
		catch(IOException e){
			
			if(!auditFile.exists()){
				sendErrorMessage(
						"The audit file does not exist and could not be created. Running the bot in admin mode might fix this.",
						auditFile);
			}
			else if(auditFile.isDirectory()){
				sendErrorMessage(
						"The location given for the audit is a directory. Please go change the location in your code to point to a file.",
						auditFile);
			}
			else if(!Files.isWritable(auditFile.toPath())){
				sendErrorMessage(
						"The file is not writable. Running the bot in admin mode might fix this.",
						auditFile);
			}
			else{
				sendErrorMessage(
						"An unspecified error happened. Please try to run the bot again!",
						auditFile);
			}
			
		}
		
	}
	
	protected PrintWriter handleAuditHeader(File auditFile)
			throws IllegalAccessError, IOException{
		
		boolean shouldWriteHeader = false;
		
		// If file doesn't exist, create it with a header.
		if(!auditFile.exists()){
			shouldWriteHeader = true;
		}
		else{
			
			BufferedReader reader = new BufferedReader(
					new FileReader(auditFile));
			String header = reader.readLine();
			
			reader.close();
			
			// If file exists but is empty, we should write header to it.
			if(header == null){
				shouldWriteHeader = true;
			}
			else if(!header.equals(FrameworkResources.AUDIT_HEADER)){
				throw new IllegalAccessError();
			}
			
		}
		
		PrintWriter writer = new PrintWriter(new FileWriter(auditFile, true));
		
		if(shouldWriteHeader){
			writer.println(FrameworkResources.AUDIT_HEADER + "\n");
		}
		
		return writer;
		
	}
	
	protected void sendErrorMessage(String message, File auditFile){
		
		if(!this.hasSentWarning){
			
			this.hasSentWarning = true;
			
			String fileLocation = "File : \"" + auditFile.getAbsolutePath()
					+ "\"";
			String onlyOneWarningString = "This warning will only be shown once!";
			
			Logger.log(message + "\n\t" + fileLocation + "\n\t"
					+ onlyOneWarningString, Logger.LogType.ERROR);
			
		}
		
	}
	
}
