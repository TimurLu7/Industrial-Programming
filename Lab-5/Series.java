package Series;

import java.util.*;

public abstract class Series implements Comparable<Series>, Iterable<Double> {
    protected double first_el;
    protected double d;
    protected int number_el;

    public Series(double first_el, double d, int number_el){
        assert number_el>0:"Количество элементов должно быть положительным числом";

        this.first_el=first_el;
        this.d=d;
        this.number_el=number_el;
    }

    public Series(String data) {
        fromString(data);
    }

    public abstract double get_El(int i);
    public abstract double get_Sum();

    public double getFirst_el(){
        return first_el;
    }

    public double get_D(){
        return d;
    }

    public int getNumber_el(){
        return number_el;
    }

    @Override
    public int compareTo(Series other){
        return Double.compare(this.first_el,other.first_el);
    }

    @Override
    public Iterator<Double> iterator(){
        return new SeriesIterator();
    }

    private class SeriesIterator implements Iterator<Double>{
        private int currentIndex=0;

        @Override
        public boolean hasNext(){
            return currentIndex<number_el;
        }

        @Override
        public Double next(){
            assert hasNext():"Нет больше элементов в прогрессии";

            return get_El(++currentIndex);
        }
    }

    @Override
    public String toString(){
        return String.format("Series{First Element=%.2f, Difference=%.2f, Number Of Elements=%d}",
                first_el, d, number_el);
    }

    public static class Diff_Comparator implements Comparator<Series>{
        @Override
        public int compare(Series s1,Series s2){
            return Double.compare(s1.get_D(),s2.get_D());
        }
    }

    public static class Number_Comparator implements Comparator<Series>{
        @Override
        public int compare(Series s1,Series s2){
            return Integer.compare(s1.getNumber_el(),s2.getNumber_el());
        }
    }

    public static class Sum_Comparator implements Comparator<Series>{
        @Override
        public int compare(Series s1,Series s2){
            return Double.compare(s1.get_Sum(),s2.get_Sum());
        }
    }

    protected void fromString(String data){
        try{
            String clean=data.replace("Series{", "").replace("}", "");
            String[] parts=clean.split(",");
            assert parts.length == 3:"Неверный формат строки";

            this.first_el= Double.parseDouble(parts[0].split("=")[1].trim());
            this.d= Double.parseDouble(parts[1].split("=")[1].trim());
            this.number_el= Integer.parseInt(parts[2].split("=")[1].trim());

            assert number_el > 0 : "Количество элементов должно быть положительным";
        }catch (Exception e){
            throw new IllegalArgumentException("Ошибка");
        }
    }
}

