package pe.com.inventarios.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commons {
	
	private static Logger logger = LoggerFactory.getLogger(Commons.class);

	public static void zipFile(final File[] files, final File targetZipFile) throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(targetZipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			byte[] buffer = new byte[128];
			ZipEntry entry = null;
			FileInputStream fis = null;
			int read = 0;
			for (File currentFile : files) {
				if (!currentFile.isDirectory()) {
					logger.info("reading :", currentFile.getName());
					entry = new ZipEntry(currentFile.getName());
					fis = new FileInputStream(currentFile);
					zos.putNextEntry(entry);
					while ((read = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, read);
					}
					zos.closeEntry();
					fis.close();
				}
			}
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found : ", e);
		}
	}
	
	public static void zipFileWithDirectory(final File[] files, final File targetZipFile) throws IOException{
		FileOutputStream fos = new FileOutputStream(targetZipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		zipFileWithDirectory(files, zos);
		zos.close();
		fos.close();
	}
	
	public static void zipFileWithDirectory(final File[] files, ZipOutputStream zos) throws IOException {
		try {
//			FileOutputStream fos = new FileOutputStream(targetZipFile);
//			ZipOutputStream zos = new ZipOutputStream(fos);
			byte[] buffer = new byte[128];
			ZipEntry entry = null;
			FileInputStream fis = null;
			int read = 0;
			for (File currentFile : files) {
				logger.info("reading :", currentFile.getName());
				if (currentFile.isDirectory()) {
					zipFileWithDirectory(currentFile.listFiles(), zos);
					break;
				}else{
					entry = new ZipEntry(currentFile.getPath());
					fis = new FileInputStream(currentFile);
					zos.putNextEntry(entry);
					while ((read = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, read);
					}
//					zos.closeEntry();
					fis.close();					
				}
			}
//			zos.close();
//			fos.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found : ", e);
		}
	}

}
