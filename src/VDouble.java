import java.lang.Math.*;

public class VDouble extends Value {
    public double val;
    public VDouble(double v){ val = v; }
    public VDouble(){
        val = 0;
    }

    @Override
    public String toString() {
        return Double.toString(val);
    }

    @Override
    public Value add(Value v) {
        if(v instanceof VDouble){
            val = val+((VDouble) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value sub(Value v) {
        if(v instanceof VDouble){
            val = val-((VDouble) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value div(Value v) {
        if(v instanceof VDouble){
            val = val/((VDouble) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value mul(Value v) {
        if(v instanceof VDouble){
            val = val*((VDouble) v).val;
            return this;
        }else return null;
    }

    @Override
    public Value pow(Value v) {
        if(v instanceof VDouble){
            val = Math.pow(val,((VDouble) v).val);
            return this;
        }else return null;
    }

    @Override
    public boolean eq(Value v) {
        if(v instanceof VDouble){
            return val == ((VDouble) v).val;
        }else return false;
    }

    @Override
    public boolean gte(Value v) {
        if(v instanceof VDouble){
            return val >= ((VDouble) v).val;
        }else return false;
    }

    @Override
    public boolean lte(Value v) {
        if(v instanceof VDouble){
            return val <= ((VDouble) v).val;
        }else return false;
    }

    @Override
    public boolean gt(Value v) {
        if(v instanceof VDouble){
            return val > ((VDouble) v).val;
        }else return false;
    }

    @Override
    public boolean lt(Value v) {
        if(v instanceof VDouble){
            return val < ((VDouble) v).val;
        }else return false;
    }

    @Override
    public boolean neq(Value v) {
        if(v instanceof VDouble){
            return val != ((VDouble) v).val;
        }else return true;
    }

    @Override
    public boolean equals(Object other) {
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        int r = Double.valueOf(val).hashCode() * 10 + 2;
        return r;
    }

    @Override
    public VDouble create(String s) {
        VDouble r = new VDouble(Double.parseDouble(s));
        return r;
    }
}
