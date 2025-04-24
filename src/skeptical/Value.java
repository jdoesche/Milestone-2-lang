package skeptical;

public class Value {
    public enum Type {
        NUMBER,
        STRING,
        CONSTANT
    }

    private final Type type;
    private final Object value;

    public Value(double number) {
        this.type = Type.NUMBER;
        this.value = number;
    }

    public Value(String str) {
        this.type = Type.STRING;
        this.value = str;
    }

    public Value(Value other, boolean isConstant) {
        this.type = isConstant ? Type.CONSTANT : other.type;
        this.value = other.value;
    }

    public Type getType() {
        return type;
    }

    public double asNumber() {
        if (type == Type.NUMBER || type == Type.CONSTANT) return ((Number) value).doubleValue();
        throw new RuntimeException("Type error: not a number");
    }

    public String asString() {
        if (type == Type.STRING || type == Type.CONSTANT) return String.valueOf(value);
        if (type == Type.NUMBER) return Double.toString((double) value);
        throw new RuntimeException("Type error: cannot convert to string");
    }

    public boolean isConstant() {
        return type == Type.CONSTANT;
    }

    @Override
    public String toString() {
        return asString();
    }

    // Equality comparison, used for == and !=
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Value other)) return false;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
