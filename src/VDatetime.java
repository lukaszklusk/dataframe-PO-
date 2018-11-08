import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class VDatetime extends Value {
    public Date val;
    public VDatetime(Date v){
        val = v;
    }

    @Override
    public Value add(Value v) {
        return null;
    }

    @Override
    public Value sub(Value v) {
        return null;
    }

    @Override
    public Value mul(Value v) {
        return null;
    }

    @Override
    public Value div(Value v) {
        return null;
    }

    @Override
    public Value pow(Value v) {
        return null;
    }

    @Override
    public boolean gt(Value v) {
        if(v instanceof VDatetime){
            return val.after(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public boolean gte(Value v) {
        if(v instanceof VDatetime){
            return val.after(((VDatetime) v).val) || val.equals(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public boolean lt(Value v) {
        if(v instanceof VDatetime){
            return val.before(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public boolean lte(Value v) {
        if(v instanceof VDatetime){
            return val.before(((VDatetime) v).val) || val.equals(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public boolean eq(Value v) {
        if(v instanceof VDatetime){
            return val.equals(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public boolean neq(Value v) {
        if(v instanceof VDatetime){
            return  !val.equals(((VDatetime) v).val);
        }else return false;
    }

    @Override
    public int hashCode() {
        int r = val.hashCode()*10 + 5;
        return r;
    }

    @Override
    public boolean equals(Object other) {
        return this.hashCode()==other.hashCode();
    }

    @Override
    public VDatetime create(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date f = null;
        try {
            f = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        VDatetime vr = new VDatetime(f);
        return vr;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ret = sdf.format(val);
        return ret;
    }
}
