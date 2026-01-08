/* Лу Тимур 5 группа
7.	Из текста удалить все символы, кроме пробелов, не являющиеся буквами.
 Между последовательностями подряд идущих букв оставить хотя бы один пробел.
 */
import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    public static void main(String[] args) {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try {
            System.out.print("Введите текст: ");
            String line = br.readLine();
            String res = Result(line);
            System.out.println("Результат: " + res);
        } catch (IOException e) {
            System.out.println("Ошибка чтения с клавиатуры");
        }
    }
    public static String  Result(String text){
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<text.length();i++){
            char c = text.charAt(i);
            if(Character.isLetter(c)){
                buf.append(c);
            }
            else if(Character.isWhitespace(c)){
                buf.append(' ');
            }
        }
        String str = buf.toString();
        StringTokenizer tok = new StringTokenizer(str, " ");
        StringBuffer resBuf = new StringBuffer();
        while(tok.hasMoreTokens()){
            String word = tok.nextToken();
            resBuf.append(word);
            if(tok.hasMoreTokens()){
                resBuf.append(' ');
            }
        }
        return resBuf.toString();
    }
}