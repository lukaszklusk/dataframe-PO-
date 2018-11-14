import java.lang.Math.*;

public class VDouble extends Value implements Cloneable{
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
    public Value add(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            val = val+((VDouble) v).val;
            return this;
        }
        if(v instanceof VInteger){
            val = val+ (double) ((VInteger) v).val;
            return this;
        }
        if(v instanceof VFloat){
            val = val+ ((VFloat) v).val;
            return this;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public Value sub(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            val = val-((VDouble) v).val;
            return this;
        }
        if(v instanceof VInteger){
            val = val- (double) ((VInteger) v).val;
            return this;
        }
        if(v instanceof VFloat){
            val = val- ((VFloat) v).val;
            return this;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public Value div(Value v) throws DivByZero {
        if (v instanceof VInteger) {
            if (((VInteger) v).val == 0) throw new DivByZero();
            val = val / (double) ((VInteger) v).val;
            return this;
        }
        if (v instanceof VDouble) {
            if (((VDouble) v).val == 0) throw new DivByZero();
            val = val / ((VDouble) v).val;
            return this;
        }
        if (v instanceof VFloat) {
            if (((VFloat) v).val == 0) throw new DivByZero();
            val = val / (double) ((VFloat) v).val;
            return this;
        }
        return null;
    }

    @Override
    public Value mul(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            val = val*((VDouble) v).val;
            return this;
        }
        if(v instanceof VInteger){
            val = val* (double) ((VInteger) v).val;
            return this;
        }
        if(v instanceof VFloat){
            val = val* ((VFloat) v).val;
            return this;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public Value pow(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            val = Math.pow(val,((VDouble) v).val);
            return this;
        }
        if(v instanceof VInteger){
            val = Math.pow(val,(double) ((VInteger) v).val);
            return this;
        }
        if(v instanceof VFloat){
            val = Math.pow(val,(double) ((VFloat) v).val);
            return this;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean eq(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val == ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val == (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val == (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean gte(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val >= ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val >= (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val >= (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean lte(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val <= ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val <= (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val <= (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean gt(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val > ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val > (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val > (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean lt(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val < ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val < (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val < (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
    }

    @Override
    public boolean neq(Value v) throws IncompatibleTypes {
        if(v instanceof VDouble){
            return val != ((VDouble) v).val;
        }
        if(v instanceof VInteger){
            return val != (double) ((VInteger) v).val;
        }
        if(v instanceof VFloat){
            return val != (double) ((VFloat) v).val;
        }
        throw new IncompatibleTypes();
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

    @Override
    public Object clone() throws CloneNotSupportedException{
        Object cln = new VDouble(val);
        return cln;
    }
}
