import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class StudentsJDBS {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/Student";
    static final String USER = "postgres";
    static final String PASS = "postgres";
    private static Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;
    private static BufferedReader bufferedReader = null;


    private String value = "";
    private String SQL = "";

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the student database program! \n" +
                "\nThe following commands are available to work with the database: \n" +
                "show - to display all database records; \n" +
                "insert - to make a new record in the database;" +
                "(student data is entered with a space after insert); \n" +
                "delete - to delete a record from the database; \n" +
                "exit - to exit the program;");

        String[] commandLine = new String[6];
        StudentsJDBS studentsJDBS = new StudentsJDBS();
        studentsJDBS.setConnection();


        while (true) {
            System.out.println("\nPlease enter the command: ");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String command = bufferedReader.readLine();
            commandLine = command.split("\\s");
            switch (commandLine[0]) {
                case "show":
                    studentsJDBS.printStudents();
                    studentsJDBS.printAllStudents();
                    break;
                case "insert":
                    studentsJDBS.insertStudents(commandLine[1], commandLine[2], commandLine[3],
                            commandLine[4], commandLine[5]);
                    break;
                case "delete":
                    studentsJDBS.deleteStudent(commandLine[1]);
                    break;
                case "exit":
                    closeConnection();
                    break;
                default:
                    System.out.println("Invalid command entered!");
                    break;
            }
        }
    }

    public void setConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            if (bufferedReader != null) bufferedReader.close();
            System.out.println("Connection is close");
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printStudents() {
        try {
            System.out.println("");
            SQL = "SELECT column_name FROM information_schema.columns WHERE table_name = 'students';";
            statement = connection.createStatement();
            result = statement.executeQuery(SQL);
            while (result.next()) {
                value = String.format("%-15s|",
                        result.getString("column_name"));
                System.out.print(value);
            }
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printAllStudents() {
        try {
            SQL = "SELECT * FROM Students";
            statement = connection.createStatement();
            result = statement.executeQuery(SQL);

            while (result.next()) {
                value = String.format("%-15d|%-15s|%-15s|%-15s|%-4td:%-4tm:%-5tY|%-15s|",
                        result.getInt("id"), result.getString("first_name"), result.getString("second_name"),
                        result.getString("last_name"), result.getDate("birth_day"), result.getDate("birth_day"),
                        result.getDate("birth_day"), result.getString("student_group"));
                System.out.println(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteStudent(String s) {
        try {

            int id = Integer.parseInt(s);
            SQL = String.format("DELETE FROM Students WHERE id=%d", id);
            statement = connection.createStatement();
            statement.executeUpdate(SQL);
            System.out.println("Removal of student with number : " + s + " was successful");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertStudents(String first_name, String second_name, String last_name, String birth_day, String student_group) {
        try {
            SQL = String.format("insert into students ( first_name, second_name, last_name, birth_day, student_group) VALUES (" +
                    "'" + first_name + "', '" + second_name + "', '" + last_name + "', '" + birth_day + "', '" + student_group + "'" + ");");
            statement = connection.createStatement();
            statement.executeUpdate(SQL);
            System.out.println("adding student was successful");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}