package modcalc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ceno
 */
public class Mastery {

    private final int ID;
    private String name;
    private String classTable;
    private ArrayList<Skill> skills;
    private final ModCalc calc;

    public Mastery(int id, ModCalc mc) {
        this.ID = id;
        this.calc = mc;
        skills = new ArrayList<>();
    }

    public Mastery(int id, ModCalc mc, String str) {
        this(id, mc);
        this.name = str;
        skills = new ArrayList<>();
    }

    public Mastery(int id, ModCalc mc, String name, String table) {
        this(id, mc, name);
        this.classTable = table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<METADATA -- MASTERY -- NAME=").append(name).append(" />\n\n");
        skills.stream().forEach((s) -> {
            sb.append(s.toString());
        });
        return sb.toString().trim();
    }

    public void addSkill(Skill s) {
        skills.add(s);
    }

    public Skill getSkillForName(String str) {
        for (Skill s : skills) {
            if (s.getName().equals(str)) {
                return s;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setClassTable(String str) {
        this.classTable = str;
    }

    public int compileSkills(String dbrPath) {
        int cnt = 0;
        if (classTable == null) {
            return -1;
        }
        try {
            String[] tableLines = null;
            try {
                tableLines = FileReaderGD.read(dbrPath + classTable).split("\n");
            } catch (Exception e) {
            }
            if (tableLines == null) { //need to use internalized data
                for (String str : this.calc.getLocalFileNameList()) {
                    String tmp = str.replace(str.substring(0, str.indexOf("res\\database")), "").replace("res\\database", "").replace("\\", "/");
                    if (tmp.equals("/" + classTable)) {
                        tableLines = FileReaderGD.read(str).split("\n");
                        break;
                    }
                }

                if (tableLines == null) {
                    return -1;
                }
            }
            String[] skillList = null;

            for (String tabSearch : tableLines) {
                if (tabSearch.contains("tabSkillButtons,")) {
                    skillList = tabSearch.replace("tabSkillButtons,", "").replace(",", "").split(";");
                    break;
                }
            }

            if (skillList != null) {
                for (String skillList1 : skillList) {
                    String[] ui = null;
                    try {
                        ui = FileReaderGD.read(dbrPath + skillList1).split("\n");
                    } catch (Exception e) {
                    }
                    boolean usedInternal = false;
                    String internalPath = "";
                    if (ui == null) { //need to use internalized data
                        for (String str : this.calc.getLocalFileNameList()) {
                            String tmp = str.replace(str.substring(0, str.indexOf("res\\database")), "").replace("res\\database", "").replace("\\", "/");
                            if (tmp.equals("/" + skillList1)) {
                                ui = FileReaderGD.read(str).split("\n");
                                usedInternal = true;
                                internalPath = str;
                                break;
                            }
                        }
                        if (ui == null) {
                            return -1;
                        }
                    }
                    String sName = null;
                    int x = 0, y = 0;
                    boolean circular = false;
                    for (String str : ui) {
                        if (str.contains("skillName")) {
                            if(usedInternal)
                                sName = internalPath.substring(0, internalPath.indexOf("res\\database")) + str.replace("skillName,", "").replace(",", "");
                            else
                                sName = dbrPath + str.replace("skillName,", "").replace(",", "");
                        } else if (str.contains("isCircular")) {
                            circular = Integer.parseInt(str.replace("isCircular", "").replace(",", "")) > 0;
                        } else if (str.contains("bitmapPositionX")) {
                            x = Integer.parseInt(str.replace("bitmapPositionX", "").replace(",", ""));
                        } else if (str.contains("bitmapPositionY")) {
                            y = Integer.parseInt(str.replace("bitmapPositionY", "").replace(",", ""));
                        }
                    }
                    Skill tmp = new Skill(x, y, circular, this.calc, "Skill" + cnt, sName.replace("\\","/"));
                    tmp.compressData();
                    this.addSkill(tmp);
                    cnt++;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Mastery.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cnt;
    }
}
