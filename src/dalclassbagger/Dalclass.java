package dalclassbagger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import gov.loc.repository.bagit.creator.BagCreator;
import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.hash.StandardSupportedAlgorithms;
import gov.loc.repository.bagit.writer.BagWriter;

public class Dalclass {


	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		//setup for bagging
		Path path= Paths.get("C:\\Users\\Student\\Pictures\\lkjubkjb");
		StandardSupportedAlgorithms algorithm = StandardSupportedAlgorithms.MD5;
		boolean includeHiddenFiles = false;
		Bag bag = BagCreator.bagInPlace(path, Arrays.asList(algorithm), includeHiddenFiles);
		
		//moving the bag to where the user specified
		Path output = Paths.get("C:\\Users\\Student\\Desktop\\Bag");
		BagWriter.write(bag, output);
		
		//cleaning up after the API. It creates a bag from the directory specified, at it's location
		copyFolder(Paths.get(path.toString()+"\\data"), path.toAbsolutePath());
		cleanFolder(path);
		
		//zipping bag
		Path zipLocation = Paths.get(output.toAbsolutePath()+".zip");
		pack(output,zipLocation);
	}
	
	
	//Deletes unnecessary files created by API
	private static void cleanFolder(Path path) {
		try {
			Files.walk(Paths.get(path.toString()+"\\data"))
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
			Files.delete(Paths.get(path.toString()+"\\bag-info.txt"));
			Files.delete(Paths.get(path.toString()+"\\bagit.txt"));
			Files.delete(Paths.get(path.toString()+"\\manifest-md5.txt"));
			Files.delete(Paths.get(path.toString()+"\\tagmanifest-md5.txt"));
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
	    Path p = Files.createFile(zipFilePath);
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
