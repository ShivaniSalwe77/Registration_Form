import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;


    public DashboardForm() {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(300,350));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegistredUser = connectToDatabase();

        if(hasRegistredUser) {

            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if(user != null){
                lbAdmin.setText("User : "+user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                  dispose();
            }
        }
     else {
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                lbAdmin.setText("User : " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }

        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

      if(user != null){
        JOptionPane.showMessageDialog(DashboardForm.this,
            "New user : "+ user.name,
              "Succesful Registration ",
              JOptionPane.INFORMATION_MESSAGE);
           }
            }
        });
    }

    private boolean connectToDatabase(){
        boolean hasRegistredUser = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3307/";
        final String DB_URL = "jdbc:mysql://localhost:3307/mystore";
        final String USERNAME = "root";
        final  String PASSWORD = "";

        try {
            //first connect to mysql server and create database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL,USERNAME,PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mystore");
            statement.close();
            conn.close();

            //second connect to database and create table users if not created
            conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS ("
                    +"id INT(10) NOT NULL,"
                    +"name VARCHAR(200) NOT NULL,"
                    +"email VARCHAR(200) NOT NULL,"
                    +"phone VARCHAR(200),"
                    +"address VARCHAR(200),"
                    +"password VARCHAR(200) NOT NULL"
                    +")";

            statement.executeUpdate(sql);

            //check if we have users in table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*)FORM users");

            if(resultSet.next()){
                int numUsers = resultSet.getInt(1);
                if(numUsers >0){
                    hasRegistredUser = true;
                }
            }
            statement.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return  hasRegistredUser;

    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm();
    }
}