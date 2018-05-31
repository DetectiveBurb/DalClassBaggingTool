package dalclassbagger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Baglady {

	static Path input;
	static Path output;
	
	public Baglady() {
	}
	
	public Baglady(Path in, Path out)
	{
		this.input=in;
		this.output=out;
	}
	
	public static void cleanFolder() {
		try {
			Files.walk(Paths.get(input.toString()+"\\data"))
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
			Files.delete(Paths.get(input.toString()+"\\bag-info.txt"));
			Files.delete(Paths.get(input.toString()+"\\bagit.txt"));
			Files.delete(Paths.get(input.toString()+"\\manifest-md5.txt"));
			Files.delete(Paths.get(input.toString()+"\\tagmanifest-md5.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//copys folders to the desired output location
	public static void copyFolder( Path src, Path dest ){
	     try{
	    	 Files.walk( src )
	         .forEach( s ->{
	        	 try
	             {   Path d = dest.resolve( src.relativize(s) );
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
	public static void pack(Path sourceDirPath, Path zipFilePath) throws IOException {
	    Path p= null;
		try {p = Files.createFile(zipFilePath);}
		catch (IOException e) {p=zipFilePath;}
	    
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
	        Path pp = sourceDirPath;
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

}
