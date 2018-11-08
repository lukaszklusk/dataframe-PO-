import java.lang.Math.*;

public class VFloat extends Value {
    public float val;
    public VFloat(float v){ val = v; }

    @Override
    public String toString() {
        return Float.toString(val);
    }

    @Override
    public Value add(Value v) {
        if(v instanceof VFloat){
            val = val+((VFloat) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value sub(Value v) {
        if(v instanceof VFloat){
            val = val-((VFloat) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value div(Value v) {
        if(v instanceof VFloat){
            val = val/((VFloat) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value mul(Value v) {
        if(v instanceof VFloat){
            val = val*((VFloat) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value pow(Value v) {
        if(v instanceof VFloat){
            val = (float)Math.pow(val,((VFloat) v).val);
            return this;
        }else return null;
    }

    @Override
    public boolean eq(Value v) {
        if(v instanceof VFloat){
            return val == ((VFloat) v).val;
        }else return false;
    }

    @Override
    public boolean gte(Value v) {
        if(v instanceof VFloat){
            return val >= ((VFloat) v).val;
        }else return false;
    }

    @Override
    public boolean lte(Value v) {
        if(v instanceof VFloat){
            return val <= ((VFloat) v).val;
        }else return false;
    }

    @Override
    public boolean gt(Value v) {
        if(v instanceof VFloat){
            return val > ((VFloat) v).val;
        }else return false;
    }

    @Override
    public boolean lt(Value v) {
        if(v instanceof VFloat){
            return val < ((VFloat) v).val;
        }else return false;
    }

    @Override
    public boolean neq(Value v) {
        if(v instanceof VFloat){
            return val != ((VFloat) v).val;
        }else return true;
    }

    @Override
    public boolean equals(Object other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        int r = Float.valueOf(val).hashCode() * 10 + 3;
        return r;
    }

    @Override
    public VFloat create(String s) {
        VFloat vr = new VFloat(Float.parseFloat(s));
        return vr;
    }
}