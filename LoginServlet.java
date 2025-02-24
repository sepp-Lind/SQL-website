import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.mysql.cj.jdbc.MysqlDataSource;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet{
    private Connection connection;
    private ResultSet lookupResults;
    private PreparedStatement pStatement;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("input1");
        String password = request.getParameter("input2");

        // SQL query to verify user credentials
        String sql = "SELECT * FROM usercredentials WHERE login_username = ? AND login_password = ?";
        boolean validReq = false;

        try{
            getDBConnection();

            pStatement = connection.prepareStatement(sql);
            pStatement.setString(1, username);
            pStatement.setString(2, password);

            lookupResults = pStatement.executeQuery();
            if(lookupResults.next()){
                validReq = true;
            }else{
                validReq = false;
            }

        } catch(SQLException sqlException){
            JOptionPane.showMessageDialog(null, sqlException, "MAJOR ERROR- ERROR", JOptionPane.ERROR_MESSAGE);
        }

        if(validReq){
            switch (username) {
                case "root":
                    response.sendRedirect("rootHome.jsp");
                    break;
                case "client":
                    response.sendRedirect("clientHome.jsp");
                    break;
                case "theaccountant":
                    response.sendRedirect("accountantHome.jsp");
                    break;
                default:
                    response.sendRedirect("errorpage.html");
                    break;
            }
        }else{
            response.sendRedirect("errorpage.html");
        }

    }

    private void getDBConnection(){

        Properties properties = new Properties();
        FileInputStream FIN = null;
        MysqlDataSource dataSource = null;

        try{
            FIN = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 11.0/webapps/Project4/WEB-INF/classes/systemapp.properties");
            properties.load(FIN);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            connection = dataSource.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
