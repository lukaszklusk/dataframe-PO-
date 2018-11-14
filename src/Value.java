public abstract class Value {
    public abstract String toString();
    public abstract Value add(Value v) throws IncompatibleTypes;
    public abstract Value sub(Value v) throws IncompatibleTypes;
    public abstract Value mul(Value v) throws IncompatibleTypes;
    public abstract Value div(Value v) throws IncompatibleTypes, DivByZero;
    public abstract Value pow(Value v) throws IncompatibleTypes;
    public abstract boolean eq(Value v) throws IncompatibleTypes;
    public abstract boolean gte(Value v) throws IncompatibleTypes;
    public abstract boolean lte(Value v) throws IncompatibleTypes;
    public abstract boolean gt(Value v) throws IncompatibleTypes;
    public abstract boolean lt(Value v) throws IncompatibleTypes;
    public abstract boolean neq(Value v) throws IncompatibleTypes;
    public abstract boolean equals(Object other);
    public abstract int hashCode();
    public abstract <T> T create(String s);
}
