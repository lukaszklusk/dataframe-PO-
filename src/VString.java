public class VString extends Value {
    public String val;
    public VString(String v){
        val = v;
    }
    @Override
    public String toString() {
        return val;
    }

    @Override
    public Value add(Value v) {
        return this;
    }

    @Override
    public Value sub(Value v) {
        return this;
    }

    @Override
    public Value mul(Value v) {
        return this;
    }

    @Override
    public Value div(Value v) {
        return this;
    }

    @Override
    public Value pow(Value v) {
        return null;
    }

    @Override
    public boolean eq(Value v){
        if(v instanceof VString){
            return val == ((VString) v).val;
        }else return false;
    }

    @Override
    public boolean gte(Value v) {
        if(v instanceof VString){
            return val.length() >= ((VString) v).val.length();
        }else return false;
    }

    @Override
    public boolean lte(Value v) {
        if(v instanceof VString){
            return val.length() <= ((VString) v).val.length();
        }else return false;
    }

    @Override
    public boolean gt(Value v) {
        if(v instanceof VString){
            return val.length() > ((VString) v).val.length();
        }else return false;
    }

    @Override
    public boolean lt(Value v) {
        if(v instanceof VString){
            return val.length() < ((VString) v).val.length();
        }else return false;
    }

    @Override
    public boolean neq(Value v) {
        if(v instanceof VString){
            return val != ((VString) v).val;
        }else return false;
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
}
