package ninja.zilles.lecture2;

/**
 * Created by zilles on 4/4/17.
 */

public class ObjectExample {
    private String name;
    private int number;
    private double anotherNumber;

    public ObjectExample() { // needed by firebase to create objects
    }

    public ObjectExample(String name, int number, double anotherNumber) {
        this.name = name;
        this.number = number;
        this.anotherNumber = anotherNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getAnotherNumber() {
        return anotherNumber;
    }

    public void setAnotherNumber(double anotherNumber) {
        this.anotherNumber = anotherNumber;
    }
}
