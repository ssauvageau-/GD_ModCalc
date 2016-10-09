package modcalc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Launcher;

/**
 * @author Ceno
 */
public class ModCalc {

    private final int yAdjust = 71;
    private final int xAdjust = 40;
    private String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Grim Dawn\\mods\\";
    private String modDir = "Cornucopia003\\database\\";
    private final String defaultPath = "res/database";
    private ArrayList<String> localFileNameList;

    public ModCalc(){}
    
    public ModCalc(String installDir, String modDir) {
        this.installDir = installDir;
        this.modDir = modDir;
    }
    
    public void init() {
        String table = null;

        localFileNameList = pullLocalFileNames();

        for (String str : FileReaderGD.getFileNames(null, Paths.get(installDir + modDir))) {//attempt to read from mod data
            String[] tmp = str.split("\\\\");
            if (tmp[tmp.length - 1].equals("skills_mastertable.dbr")) {
                try {
                    table = FileReaderGD.read(str);
                } catch (IOException ex) {
                    Logger.getLogger(ModCalc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (table == null) { //need to use internalized data
            for (String str : localFileNameList) {
                String[] tmp = str.split("\\\\");
                if (tmp[tmp.length - 1].equals("skills_mastertable.dbr")) {
                    try {
                        table = FileReaderGD.read(str);
                    } catch (IOException ex) {
                        Logger.getLogger(ModCalc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        ArrayList<Mastery> masteries = new ArrayList<>();
        for(String str : table.split("\n"))
        {
            if(str.contains("skillCtrlPane")&&str.contains("/"))
            {
                String[] tmp = str.split(",");
                int id = Integer.parseInt(tmp[0].replace("skillCtrlPane", ""));
                String cName = "class" + id;
                String cTable = tmp[1];
                Mastery m = new Mastery(id, this, cName, cTable.replace("//","\\"));
                masteries.add(m);
                m.compileSkills(installDir + modDir);
                System.out.println(m);
            }
        }
    }
    
    public String getDir() {
        return installDir + modDir;
    }

    public ArrayList<String> getLocalFileNameList() {
        return localFileNameList;
    }

    private ArrayList<String> pullLocalFileNames() {
        return recursiveDefault(null, defaultPath);
    }

    //get default resource files while running from a JAR or from an IDE
    public ArrayList<String> recursiveDefault(ArrayList<String> output, String path) {
        if (output == null) {
            output = new ArrayList<>();
        }

        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {
            try ( // Run with JAR file
                    JarFile jar = new JarFile(jarFile)) {
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(path + "/")) { //filter according to the path
                        System.out.println(name);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ModCalc.class.getName()).log(Level.SEVERE, null, ex);
            } //gives ALL entries in jar
        } else {
            try { // Run with IDE
                final URL url = Launcher.class.getResource("/" + path) == null ? new URL("file:///" + path) : Launcher.class.getResource("/" + path);
                if (url != null) {
                    try {
                        final File apps = new File(url.toURI());
                        for (File app : apps.listFiles()) {
                            if (app.isDirectory()) {
                                recursiveDefault(output, app.getPath());
                            }
                            output.add(app.getPath());
                            //System.out.println(app.getPath());
                        }
                    } catch (URISyntaxException ex) {
                    }
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(ModCalc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return output;
    }
}
