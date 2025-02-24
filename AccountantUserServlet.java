import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccountantUserServlet extends HttpServlet {
    private Connection connection;
    private String message = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        String resultTable = "";
        try {
            getDBConnection();
                Statement statement = connection.createStatement();
                    ResultSet resultSet;
                    switch (command) {
                        case "1":
                            resultSet = statement.executeQuery("{call Get_The_Sum_Of_All_Parts_Weights()}");
                            resultTable = getHtmlRows(resultSet);
                            break;
                        case "2":
                            resultSet = statement.executeQuery("{call Get_The_Maximum_Status_Of_All_Suppliers()}");
                            resultTable = getHtmlRows(resultSet);
                            break;
                        case "3":
                            resultSet = statement.executeQuery("{call Get_The_Total_Number_Of_Shipments()}");
                            resultTable = getHtmlRows(resultSet);
                            break;
                        case "4":
                            resultSet = statement.executeQuery("{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}");
                            resultTable = getHtmlRows(resultSet);
                            break;
                        case "5":
                            resultSet = statement.executeQuery("{call List_The_Name_And_Status_Of_All_Suppliers()}");
                            resultTable = getHtmlRows(resultSet);
                            break;
                        default:
                            resultTable = "<p>Invalid command.</p>";
                            break;
                    }

        } catch (SQLException e) {
            message = "SQL error: " + e.getMessage();
        }

        request.setAttribute("resultMessage", message);
        request.setAttribute("resultTable", resultTable);
        RequestDispatcher dispatcher = request.getRequestDispatcher("accountantHome.jsp");
        dispatcher.forward(request, response);
    }

    private void getDBConnection() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 11.0/webapps/Project4/WEB-INF/classes/theaccountant.properties")) {
            properties.load(fis);
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            message = "Database connection error: " + e.getMessage();
        }
    }

    public static synchronized String getHtmlRows(ResultSet resultSet) throws SQLException {
        StringBuilder htmlRows = new StringBuilder("<table border='1'>");
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Table Header
        htmlRows.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlRows.append("<th>").append(metaData.getColumnName(i)).append("</th>");
        }
        htmlRows.append("</tr>");

        // Table Rows
        int rowCounter = 0;
        while (resultSet.next()) {
            String rowColor = (rowCounter % 2 == 0) ? "#f2f2f2" : "#ffffff";
            htmlRows.append("<tr style='background-color:").append(rowColor).append(";'>");
            for (int i = 1; i <= columnCount; i++) {
                String columnData = resultSet.getString(i);
                htmlRows.append("<td style='color:black;'>").append(columnData != null ? columnData : "NULL").append("</td>");
            }
            htmlRows.append("</tr>");
            rowCounter++;
        }
        htmlRows.append("</table>");

        if (rowCounter == 0) {
            htmlRows.append("<p>No data found for your query.</p>");
        }

        return htmlRows.toString();
    }
}
