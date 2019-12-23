package model;

public class Variable extends Term implements Cloneable {
    public Variable(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "'" + name + '\'';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof Variable) {
                return this.getName().equals(((Variable) obj).getName());
            }
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}