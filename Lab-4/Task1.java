import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Group group = new Group();

        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            Map<String, Student> map = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 7) continue;

                String[] Parts = parts[0].split(" ");
                if (Parts.length < 3) continue;

                String lastName = Parts[0];
                String firstName = Parts[1];
                String middleName = Parts[2];
                int course = Integer.parseInt(parts[1]);
                String gr = parts[2];
                int session = Integer.parseInt(parts[3]);
                String subject = parts[4];
                String type = parts[5];
                String result = parts[6];

                String key = lastName + firstName + middleName;

                Student student = map.get(key);
                if (student == null) {
                    student = new Student(lastName, firstName, middleName, course, gr);
                    map.put(key, student);
                }

                if (type.equals("экзамен")) {
                    student.getBook().addRecord(session, subject, type, Integer.parseInt(result));
                } else {
                    student.getBook().addRecord(session, subject, type, result);
                }
            }

            for (Student student : map.values()) {
                group.addStudent(student);
            }

        } catch (Exception e) {
            System.out.println("Ошибка открытия файла input.txt");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("output.txt"))) {
            for (Student s : group.getGoodStudents()) {
                for (RecordBook.Session r : s.getBook().getRecords()) {
                    if (r.getType().equals("экзамен") && r.getMark() != null) {
                        pw.printf("%s; %d курс; %s; Сессия %d; %s; %d\n",
                                s.getName(), s.getCourse(), s.getGroup(),
                                r.getNumber(), r.getSubject(), r.getMark());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла output.txt");
        }
    }
}
class Student {
    private String lastName;
    private String firstName;
    private String middleName;
    private int course;
    private String group;
    private RecordBook Book;

    public Student(String lastName, String firstName, String middleName, int course, String group) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.course = course;
        this.group = group;
        this.Book = new RecordBook();
    }

    public String getName() {return lastName + " " + firstName + " " + middleName;}

    public String getGroup() {return group;}

    public int getCourse() {return course;}

    public RecordBook getBook() {return Book;}

    public double AverageMark() {
        List<RecordBook.Session> records = Book.getRecords();
        int sum = 0;
        int count = 0;

        for (RecordBook.Session record : records) {
            if (record.getType().equals("экзамен") && record.getMark() != null) {
                sum += record.getMark();
                count++;
            }
        }

        if (count > 0) {
            return (double) sum / count;
        } else {
            return 0.0;
        }
    }

    public boolean GoodStudent() {
        List<RecordBook.Session> records = Book.getRecords();
        for (RecordBook.Session record : records) {
            if (record.getType().equals("экзамен")) {
                if (record.getMark() == 0 || record.getMark() < 9) {
                    return false;
                }
            } else if (record.getType().equals("зачет")) {
                if (!record.getResult().equalsIgnoreCase("зачет")) {
                    return false;
                }
            }
        }
        return true;
    }
}

class RecordBook {
    private List<Session> records = new ArrayList<>();

    public void addRecord(int session, String subject, String type, String result) {
        records.add(new Session(session, subject, type, result));
    }

    public void addRecord(int session, String subject, String type, int mark) {
        records.add(new Session(session, subject, type, mark));
    }

    public List<Session> getRecords() {
        return records;
    }

    class Session {
        private int number;
        private String subject;
        private String type; // экзамен / зачет
        private Integer mark; // для экзамена
        private String result; // для зачета

        public Session(int number, String subject, String type, int mark) {
            assert type.equals("экзамен") : "Оценка только для экзамена";
            this.number = number;
            this.subject = subject;
            this.type = type;
            if (mark < 0 || mark > 10) {
                throw new IllegalArgumentException("Оценка должна быть от 0 до 10");
            }
            this.mark = mark;
        }

        public Session(int number, String subject, String type, String result) {
            assert type.equals("зачет") : "Результат только для зачета";
            this.number = number;
            this.subject = subject;
            this.type = type;
            if (!result.equals("зачет") && !result.equals("незачет")) {
                throw new IllegalArgumentException("Неверный результат");
            }
            this.result = result;
        }

        public int getNumber() { return number; }
        public String getSubject() { return subject; }
        public String getType() { return type; }
        public Integer getMark() { return mark; }
        public String getResult() { return result; }
    }
}

class Group {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student s) {
        students.add(s);
    }

    public double AverageGroupMark(int number) {
        int sum = 0;
        int count = 0;

        for (Student student : students) {
            for (RecordBook.Session record : student.getBook().getRecords()) {
                if (record.getType().equals("экзамен") && record.getNumber() == number && record.getMark() != null) {
                    sum += record.getMark();
                    count++;
                }
            }
        }

        if (count > 0) {
            return (double) sum / count;
        } else {
            return 0.0;
        }
    }

    public int GoodStudentsCount() {
        int count = 0;
        for (Student student : students) {
            if (student.GoodStudent()) {
                count++;
            }
        }
        return count;
    }

    public long BadStudentsCount() {
        int count = 0;
        for (Student student : students) {
            for (RecordBook.Session record : student.getBook().getRecords()) {
                if (record.getType().equals("экзамен") && record.getMark() != null && record.getMark() == 4) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Student> getGoodStudents() {
        List<Student> goodStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.GoodStudent()) {
                goodStudents.add(student);
            }
        }
        return goodStudents;
    }
}

