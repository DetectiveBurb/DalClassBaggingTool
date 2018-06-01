package dalclassbagger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.writer.BagWriter;

public class Baglady {

	Path input;
	Path output;
	Path metaoutput;
	Bag bag;
	StandardSupportedAlgorithms alg = StandardSupportedAlgorithms.MD5;
	
	public Baglady() {
	}
	
	public Baglady(Path in, Path out,Path metaoutput,StandardSupportedAlgorithms alg)
	{
		this.input=in;
		this.output=out;
		this.alg=alg;
		this.metaoutput=metaoutput;
	}
	
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

	public void cleanFolder() {
		copyFolder();
		try {
			Files.walk(Paths.get(input.toString()+"\\data"))
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
			Files.delete(Paths.get(input.toString()+"\\bag-info.txt"));
			Files.delete(Paths.get(input.toString()+"\\bagit.txt"));
			Files.delete(Paths.get(input.toString()+"\\manifest-"+alg+".txt"));
			Files.delete(Paths.get(input.toString()+"\\tagmanifest-"+alg+".txt"));
		} 
		catch (IOException e) {e.printStackTrace();}
	}

	//copys folders to the desired output location
	public void copyFolder(){
		Path src = Paths.get(input.toString()+"\\data");
	     try{
	    	 Files.walk(Paths.get(input.toString()+"\\data"))
	         .forEach( s ->{
	        	 try
	             {   Path d = input.resolve( src.relativize(s) );
	                 if( Files.isDirectory( s ) )
	                 {   if( !Files.exists( d ) )
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
	            } catch (IOException e) {
	                System.err.println(e);
	            }
	          });
	    }
	}
	
	public void makeBag() throws NoSuchAlgorithmException, IOException
	{
		bag=BagCreator.bagInPlace(input, Arrays.asList(alg), false);
	}
	
	public void writeBag() throws NoSuchAlgorithmException, IOException
	{
		BagWriter.write(bag, output);
	}
	
	public void makeMetaData()throws IOException
	{
		
		//SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		//String day=format.format(day);
		String text ="";
		  try {metaoutput.toFile().createNewFile();}
		 catch (IOException e1) {e1.printStackTrace();}
		 //String text="Created on "+day+"\r\n";
		 List x = bag.getMetadata().getAll();
		 for(Object item : x) {
			 text+=item.toString()+"\r\n";
		 }
		 
		 for (Object item : bag.getItemsToFetch())
			 text+=item.toString();
		 
		byte data[]=text.getBytes();
		try {Files.write(metaoutput, data);}
		catch (IOException e) {e.printStackTrace();}
}
	
	
	public void doeverything(boolean zip)
	{
		try {
			makeBag();
			writeBag();
			if (zip)
				pack();
			cleanFolder();
			makeMetaData();
		} 
		catch (NoSuchAlgorithmException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}

}
