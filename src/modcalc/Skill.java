package modcalc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ceno
 */
public class Skill {

    private final int xPos, yPos;
    private String name;
    private String record;
    private String compressedRaw;
    private final boolean isCircular;
    private final ModCalc calc;
    private int maxUltRank, maxRank, investment;

    public Skill(int x, int y, boolean b, ModCalc mc) {
        this.xPos = x;
        this.yPos = y;
        this.isCircular = b;
        this.calc = mc;
        this.investment = 0;
    }

    public Skill(int x, int y, boolean b, ModCalc mc, String str) {
        this(x, y, b, mc);
        this.setName(str);
    }

    public Skill(int x, int y, boolean b, ModCalc mc, String str, String record) {
        this(x, y, b, mc, str);
        this.record = record;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<METADATA -- SKILL -- X=").append(xPos).append("; Y=").append(yPos)
                .append("; CIRCULAR=").append(isCircular).append("; INVESTMENT=").append(investment).append("; />\n");
        sb.append(compressedRaw).append("\n\n");
        return sb.toString();
    }

    public final void setName(String str) {
        this.name = str;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public String getName() {
        return name;
    }

    public String getRecord() {
        return record;
    }

    public String getCompressedData() {
        return compressedRaw;
    }

    public boolean isIsCircular() {
        return isCircular;
    }

    public int getMaxUltRank() {
        return maxUltRank;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public int getInvestment() {
        return investment;
    }

    public void compressData() {
        String raw = null;
        try { raw = FileReaderGD.read(record); } catch(IOException e){}
        if(raw == null) { //need to use internal data
            String last = record.split("/")[record.split("/").length - 1];
            ArrayList<String> tmp = this.calc.getLocalFileNameList();
            for(String str : tmp)
            {
                if(str.contains(last))
                    try {
                        raw = FileReaderGD.read(str);
                        break;
                } catch (IOException ex) {
                    Logger.getLogger(Skill.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(raw == null) return;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : raw.split("\n"))
            if (str != null) {
                
                String[] line = str.split(",");
                if (line.length < 2 || (line[line.length - 1].equals("0") || line[line.length - 1].equals("0.000000")
                        || line[line.length - 1].equals("") || line[line.length - 1].equals("0.0"))); //do nothing
                else {
                    String tmp_name = line[0];
                    if(tmp_name.contains("buffSkillName") || tmp_name.contains("petSkillName")) {
                        record = this.calc.getDir() + line[1];
                        compressData();
                        return;
                    }
                    if(tmp_name.contains("particleEffectAttachPoint") || tmp_name.contains("fxChanges") 
                            || tmp_name.contains("Anim")) continue;
                    if(tmp_name.contains("skillMaxLevel"))
                        this.maxRank = Integer.parseInt(line[1]);
                    else if(tmp_name.contains("skillUltimateLevel"))
                        this.maxUltRank = Integer.parseInt(line[1]);
                    String[] tmp = new String[line.length - 1];
                    for (int i = 1; i < line.length; i++) {
                        tmp[i - 1] = line[i];
                    }
                    
                    Property p = new Property(tmp_name, tmp);
                    sb.append(p.toString()).append("\n");
                }
            }
        this.compressedRaw = sb.toString();
        this.investment = 0;
    }
}
