package dalclassbagger;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.domain.Metadata;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.writer.BagWriter;

public class Baglady {

	Path input;
	Path output;
	Path metaoutput;
	Bag bag;
	StandardSupportedAlgorithms alg = StandardSupportedAlgorithms.MD5;
	Boolean success=true;
	String delimeter ="";
	Metadata metadata;
	
	//constructors
	public Baglady() {
		
		//determining OS's file path delimiter
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			delimeter = "\\";
		else 
			delimeter = "/";	
		
		metaoutput = null;
		
		this.bag = new Bag();
		metadata=new Metadata();
	}
	
	public Baglady(Path in, Path out,Path metaoutput,StandardSupportedAlgorithms alg)
	{
		this.input=in;
		this.output=out;
		this.alg=alg;
		this.metaoutput=metaoutput;
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			delimeter = "\\";
		else
			delimeter = "/";
		this.bag = new Bag();
		metadata=new Metadata();

	}
	
	//getters and setters
	public Path getInput() {
		return input;
	}

	public void setInput(Path input) {
		this.input = input;
	}

	public  Path getOutput() {
		return output;
	}

	public void setOutput(Path output) {
		this.output = output;
	}

	
	public Bag getBag() {
		return bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public StandardSupportedAlgorithms getAlg() {
		return alg;
	}

	public void setAlg(StandardSupportedAlgorithms alg) {
		this.alg = alg;
	}
	
	public Path getMetaoutput() {
		return metaoutput;
	}

	public void setMetaoutput(Path metaoutput) {
		this.metaoutput = metaoutput;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	//method to clean up folders, this API can only make bags in their current Location
	//this restores the folder to it's previous state
	//will also take care of extra files from zipping the folder
	public void cleanFolder(Boolean zip) {
		copyFolder();
		try {
			Files.walk(Paths.get(input.toString()+"\\data"))
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
			Files.delete(Paths.get(input.toString()+delimeter+"bag-info.txt"));
			Files.delete(Paths.get(input.toString()+delimeter+"bagit.txt"));
			Files.delete(Paths.get(input.toString()+delimeter+"manifest-"+alg+".txt"));
			Files.delete(Paths.get(input.toString()+delimeter+"tagmanifest-"+alg+".txt"));
			if (zip)
				Files.walk(output)
			      .sorted(Comparator.reverseOrder())
			      .map(Path::toFile)
			      .forEach(File::delete);
				
			
		} 
		catch (IOException e) {e.printStackTrace();}
	}

	//copys folders to the desired output location
	//as the API only makes bag in place, this will put them in the desired output
	public void copyFolder(){
		Path src = Paths.get(input.toString()+"\\data");
	     try{
	    	 Files.walk(Paths.get(input.toString()+"\\data"))
	         .forEach( s ->{
	        	 try
	             {   
	        		 Path d = input.resolve( src.relativize(s) );
	                 if( Files.isDirectory( s ) )
	                 {   
	                	 if( !Files.exists( d ) )
	                         Files.createDirectory( d );
	                     return;
	                 }
	                 Files.copy( s, d );// use flag to override existing
	             }
	        	 catch( Exception e )
	                 { e.printStackTrace(); }
	         });
	     }
	     catch( Exception ex )
	         {   ex.printStackTrace(); }
}

	//zips the bag if requested
	public void pack() throws IOException {
	    Path p= null;
	    Path out = Paths.get(output.toString()+".zip");
		try {p = Files.createFile(out);}
		catch (IOException e) {p=out;}
	    
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Path pp = input;
	        Files.walk(pp)
	          .filter(path -> !Files.isDirectory(path))
	          .forEach(path -> {
	              ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
	              try {
	                  zs.putNextEntry(zipEntry);
	                  Files.copy(path, zs);
	                  zs.closeEntry();
	                  }
	              catch (IOException e) {System.err.println(e);}
	          					});
	    }
	}
	
	//just a wrapper for the api functions
	public void makeBag() throws NoSuchAlgorithmException, IOException
	{
		if(metadata.isEmpty())
			bag=BagCreator.bagInPlace(input, Arrays.asList(alg), false);
		else
			bag=BagCreator.bagInPlace(input, Arrays.asList(alg), false, metadata);

	}
	
	public void writeBag() throws NoSuchAlgorithmException, IOException
	{
		BagWriter.write(bag, output);
	}
	
	//make our own metadata file 
	public void makeMetaData()throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		//get all files in the bag as an array
		Path[] payload = Files.walk(bag.getRootDir()).filter(Files::isRegularFile).toArray(Path[]::new);
		String text ="";
		
		//make the file
		try {metaoutput.toFile().createNewFile();}
		catch (IOException e1) {e1.printStackTrace();}
		
		//get bag metadata (version, etc)
		List x = bag.getMetadata().getAll();
		
		//add data to string
		for(Object item : x) {
			 text+=item.toString()+"\r\n";
		}
		 
		for (Path item : payload) {
			text+=item.toString()+"\t"+sdf.format(item.toFile().lastModified())+"\t"+item.toFile().length()+"B \r\n";
		}
		
		//print string to file
		byte data[]=text.getBytes();
		try {Files.write(metaoutput, data);}
		catch (IOException e) {e.printStackTrace();}
}
	
	//what happens when start is pushed, creates bag and cleans up after it.
	public void doeverything(boolean zip, Map<String, Component> customFields)
	{
		try {
			makeBagInfo(customFields);
			makeBag();
			writeBag();
			if (zip)
				pack();
			if (metaoutput != null)
				makeMetaData();
			cleanFolder(zip);
		} 
		catch (NoSuchAlgorithmException e) {e.printStackTrace(); this.success=false;} 
		catch (IOException e) {e.printStackTrace(); this.success=false;}
	}
	
	public void doeverything(boolean zip)
	{
		try {
			makeBag();
			writeBag();
			if (zip)
				pack();
			if (metaoutput != null)
				makeMetaData();
			cleanFolder(zip);
		} 
		catch (NoSuchAlgorithmException e) {e.printStackTrace(); this.success=false;} 
		catch (IOException e) {e.printStackTrace(); this.success=false;}
	}

	private void makeBagInfo(Map<String, Component> customFields) {
		
		for (Entry<String, Component> entry : customFields.entrySet()) 
		{
			this.metadata.add(entry.getKey(), getFilled(entry.getValue()));
		}
	}
	
	private String getFilled(Component value) {
		RequiredComboBox box =new RequiredComboBox();
		RequiredTextField field= new RequiredTextField();
		if (value.getClass().equals(box.getClass())) 
		{
			return ((RequiredComboBox) value).getSelectedItem().toString();
		}
		else if (value.getClass().equals(field.getClass()))
		{
			return ((RequiredTextField) value).getText().toString();
		} 
		return "";
	}

}
