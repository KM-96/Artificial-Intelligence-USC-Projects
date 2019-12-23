package model;

public class Constant extends Term implements Cloneable {
    public Constant(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "'" + name + '\'';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
