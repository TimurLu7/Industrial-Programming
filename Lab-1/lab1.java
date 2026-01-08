/*Лу Тимур 5 группа, вариант 1
Функция представлена в виде своего ряда Тейлора. Вычислить приближённое значение суммы этого бесконечного ряда.
Вычисления заканчивать, когда очередное слагаемое окажется по модулю меньше заданного числа .
Вид этого числа определяется  следующим условием:
  = 10-k, где k – натуральное число.
Сравнить полученный результат со значением, вычисленным через стандартные функции.
Значения x и k ввести с клавиатуры.
Вывод результата осуществить с тремя знаками после десятичной точки.
*/
package lab1;
import java.util.*;

public class lab1 {

	public static void main(String[] args) {
	 Scanner in = new Scanner (System.in);
	 System.out.print("Введите x: ");
	 double x = in.nextDouble();
	 
     System.out.print("Введите натуральное число k: ");
     int k = in.nextInt();
     
     double e = Math.pow(10, -k);
     double a = 1.0;
     double sum = a;
     int n = 1;
     
     while (Math.abs(a) >= e)
     {
    	 a *= x/n;
    	 sum+=a;
    	 n++;
     }
     double b = Math.exp(x);
     
     System.out.printf("Результат по ряду:  %.3f%n",sum);
     System.out.printf("Результат через стандартную функцию:  %.3f%n",b);
     in.close();
	}

}
