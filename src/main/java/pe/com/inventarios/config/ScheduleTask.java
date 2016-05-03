package pe.com.inventarios.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.MultipartConfigElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pe.com.inventarios.model.Activo;
import pe.com.inventarios.model.Foto;
import pe.com.inventarios.repository.ActivoRepository;
import pe.com.inventarios.repository.FotoRepository;

@Component
public class ScheduleTask {
	
	@Value("${path.estructurasSanitarias}")
	private String pathPhotos;
	@Value("${path.files.photos}")
	protected String pathFilesPhotos;
	
	private int MAX_FILE_UPLOAD=5;
	
	private FotoRepository fotoRepository;
	
	private ActivoRepository activoRepository;
	
	private MultipartConfigElement config;
	
	private List<String> noSave;
	private List<String> duplicateActive;
	
	@Autowired
	public ScheduleTask(ActivoRepository activoRepository, FotoRepository fotoRepository, MultipartConfigElement element) {
		this.activoRepository = activoRepository;
		this.fotoRepository = fotoRepository;
		this.config = element;
		noSave = new ArrayList<>();
		duplicateActive = new ArrayList<>();
	}

	@Scheduled(cron="0 0 8-9 * * MON-FRI")
//	@Scheduled(fixedDelay=900000)
	public void swarmPhotos(){
		File files = new File(pathPhotos);
		listFilesForFolder(files);
		System.out.println("No save by Size = "+noSave.toString());
		System.out.println("Active duplicates "+duplicateActive.toString());		
	}
	
	public void listFilesForFolder(final File folder) {
		String seqPhoto = "";
		Activo activo = null;
		Set<Foto> fotosLocal = null;
		Set<Foto> fotos = null;
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	System.out.println(fileEntry.getName());
	        	seqPhoto = fileEntry.getName();
	        	if(seqPhoto.matches("\\d+")){
	        		try{
	        			activo = activoRepository.findBySeq2015(seqPhoto);	        			
	        		}catch(Exception e){
	        			System.out.println(e.getMessage());
	        			duplicateActive.add(seqPhoto+" - "+e.getMessage());
	        			continue;
	        		}
	        		if(activo == null)
	        			continue;
	        		fotos = fotoRepository.findByActivo(activo.getActivo());
	        		if(fotos.isEmpty()){
	        			fotosLocal = getPhotos(fileEntry, activo.getActivo(), activo.getSeq2015());
	        			if(!fotosLocal.isEmpty())
	        				fotoRepository.save(fotosLocal);
	        		}
	        	}
	        	else{
	        		listFilesForFolder(fileEntry);
	        		System.out.println("---------------------------------------");
	        	}
	        }
	    }
	}
	
	public Set<Foto>getPhotos (final File folder, Long activoID, String secuence){
		Set<Foto> fotos = new HashSet<>();
		Foto foto = null;
		//long activoID = new Long(folder.getName());
		int conta = 1;
		long limit = config.getMaxFileSize();
		System.out.print("ActivoID "+activoID+"[");
		List<String> noSaveBySize = new ArrayList<>();
		for(File file : folder.listFiles()){
			if(file.length() > limit){
				noSaveBySize.add(file.getAbsolutePath());
    			continue;
			}
			if(conta++ > MAX_FILE_UPLOAD)
				break;
			System.out.print(file.getName()+",");
			if(StringUtils.endsWithIgnoreCase(file.getName(), ".db"))
				continue;
			foto = new Foto();
            try {
            	foto.setActivo(activoID);
            	//foto.setFoto(toByteArray(file));
            	foto.setPath(sendWritePic(file, secuence));
            	foto.setSecuence(secuence);
            	fotos.add(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("]");
		noSave.addAll(noSaveBySize);
		return fotos;
	}
	
	public byte[] toByteArray(File file) throws IOException{
		InputStream input = FileUtils.openInputStream(file);
		return IOUtils.toByteArray(input);
	}
	
	private String sendWritePic(File file, String secuence) throws IOException{
		File fileDirectory = new File(pathFilesPhotos+File.separator+secuence);
		if(!fileDirectory.exists())
			fileDirectory.mkdir();
		String filepath = Paths.get(file.getAbsolutePath()).toString();
		File outFile = new File(fileDirectory, file.getName());
		InputStream in = new FileInputStream(new File(filepath));
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outFile));
		stream.write(IOUtils.toByteArray(in));
		stream.close();
		return outFile.getAbsolutePath();
	}
}
