
public class VInteger extends Value {
    public int val;
    public VInteger(int v){
        val = v;
    }
    @Override
    public String toString() {
        return Integer.toString(val);
    }

    @Override
    public Value add(Value v) {
        if(v instanceof VInteger){
            val = val + ((VInteger) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value sub(Value v) {
        if(v instanceof VInteger){
            val = val - ((VInteger) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value mul(Value v) {
        if(v instanceof VInteger){
            val = val * ((VInteger) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value div(Value v) {
        if(v instanceof VInteger){
            val = val / ((VInteger) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value pow(Value v) {
        if(v instanceof VInteger){
            val = DataFrame.powr(val,((VInteger) v).val);
            return this;
        }else return null;
    }

    @Override
    public boolean eq(Value v){
        if(v instanceof VInteger){
            return val == ((VInteger) v).val;
        }else return false;
    }

    @Override
    public boolean gte(Value v) {
        if(v instanceof VInteger){
            return val >= ((VInteger) v).val;
        }else return false;
    }

    @Override
    public boolean lte(Value v) {
        if(v instanceof VInteger){
            return val <= ((VInteger) v).val;
        }else return false;
    }

    @Override
    public boolean gt(Value v) {
        if(v instanceof VInteger){
            return val > ((VInteger) v).val;
        }else return false;
    }

    @Override
    public boolean lt(Value v) {
        if(v instanceof VInteger){
            return val < ((VInteger) v).val;
        }else return false;
    }

    @Override
    public boolean neq(Value v) {
        if(v instanceof VInteger){
            return val != ((VInteger) v).val;
        }else return true;
    }

    @Override
    public boolean equals(Object other) {
        return other.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        int code = val*10 + 1;
        return code;
    }

    @Override
    public VInteger create(String s) {
        int vn = Integer.parseInt(s);
        return new VInteger(vn);
    }
}
