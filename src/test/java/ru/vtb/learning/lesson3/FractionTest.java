package ru.vtb.learning.lesson3;

// Копия Fraction с добавлением тестового поля calculatorCount и его getCalculatorCount().
public class FractionTest implements Fractionable{
    private int num;
    private int denum;
    private int calculatorCount = 0;

    public FractionTest(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(lifetime = 1500)
    public double doubleValue() {
        System.out.println("invoke double value");
        calculatorCount++;
        return (double) num/denum;
    }

    public int getCalculatorCount() {
        return calculatorCount;
    }
}
