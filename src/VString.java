public class VString extends Value implements Cloneable{
    public String val;
    public VString(String v){
        val = v;
    }
    public VString(){
        val = "";
    }
    @Override
    public String toString() {
        return val;
    }

    @Override
    public Value add(Value v) throws IncompatibleTypes {
        throw new IncompatibleTypes();
    }

    @Override
    public Value sub(Value v) throws IncompatibleTypes {
        throw new IncompatibleTypes();
    }

    @Override
    public Value mul(Value v) throws IncompatibleTypes {
        throw new IncompatibleTypes();
    }

    @Override
    public Value div(Value v) throws IncompatibleTypes {
        throw new IncompatibleTypes();
    }

    @Override
    public Value pow(Value v) throws IncompatibleTypes {
        throw new IncompatibleTypes();
    }

    @Override
    public boolean eq(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val == ((VString) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean gte(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val.length() >= ((VString) v).val.length();
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean lte(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val.length() <= ((VString) v).val.length();
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean gt(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val.length() > ((VString) v).val.length();
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean lt(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val.length() < ((VString) v).val.length();
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean neq(Value v) throws IncompatibleTypes {
        if(v instanceof VString){
            return val != ((VString) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean equals(Object other) {
        return other.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        int code = val.hashCode() * 10 + 4;
        return code;
    }

    @Override
    public VString create(String s) {
        return new VString(s);
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Object cln = new VString(val);
        return cln;
    }
}
