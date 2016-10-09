package modcalc;

public class Property {
    protected String name;
    protected String[] values;
    protected int size;
    
    public Property(String n, String... vals)
    {
        this.name = n;
        this.size = vals == null ? -1 : vals.length;
        this.values = vals;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" = ");
        for(String str : values)
            sb.append(str).append(", ");
        
        return sb.toString();
    }
}
