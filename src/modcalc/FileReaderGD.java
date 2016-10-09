package modcalc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class FileReaderGD {
    public static String read(String path) throws IOException
    {
        return String.join("\n", Files.readAllLines(Paths.get(path)));
    }
    
    public static List<String> getFileNames(List<String> fileNames, Path dir)
    {
        if(fileNames == null)
            fileNames = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir))
        {
            for (Path path : stream)
            {
                if(path.toFile().isDirectory())
                    getFileNames(fileNames, path);
                else 
                    fileNames.add(path.toAbsolutePath().toString());
            }
        } 
        catch(IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }
}
