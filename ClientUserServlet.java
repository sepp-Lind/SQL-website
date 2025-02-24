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


public class ClientUserServlet extends HttpServlet {
    private Connection connection;
    String message = "";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sqlStatement = request.getParameter("sqlQuery");

        if (sqlStatement == null || sqlStatement.trim().isEmpty()) {
            message = "SQL query is empty.";
        } else {
            try {
                getDBConnection();
                Statement statement = connection.createStatement();

                boolean isQuery = statement.execute(sqlStatement);

                if (isQuery) {
                    ResultSet resultSet = statement.getResultSet();
                    // Convert the result set to an HTML table
                    String resultTable = getHtmlRows(resultSet);
                    // Set the table to be displayed in the JSP
                    request.setAttribute("resultTable", resultTable);
                } else {
                    int rowsAffected = statement.getUpdateCount();
                    if(rowsAffected > 0){
                        message = "The statement executed successfully.<br>" + rowsAffected + " row(s) affected.<br>";
                    }else{
                        message = "The statement executed successfully, 0 rows affected.";
                    }
                }
                statement.close();
            } catch (SQLException e) {
                message = "SQL error: " + e.getMessage();
            }
        }

        // Set message in the request for the JSP to display
        request.setAttribute("resultMessage", message);

        // Forward the request and response to the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("clientHome.jsp");
        dispatcher.forward(request, response);
    }

    private void getDBConnection() {
        Properties properties = new Properties();
        FileInputStream FIN = null;
        MysqlDataSource dataSource = null;

        try {
            FIN = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 11.0/webapps/Project4/WEB-INF/classes/client.properties");
            properties.load(FIN);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            connection = dataSource.getConnection();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            message = "Database connection error: " + e.getMessage();
        }
    }

    public static synchronized String getHtmlRows(ResultSet resultSet) throws SQLException {
        StringBuilder htmlRows = new StringBuilder("<table border='1'>");

        // Get the ResultSet's metadata to extract column names
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Add table header row
        htmlRows.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlRows.append("<th>").append(metaData.getColumnName(i)).append("</th>");
        }
        htmlRows.append("</tr>");

        // Add table rows
        int rowCounter = 0;
        while (resultSet.next()) {
            // Apply zebra striping effect (alternating row colors)
            if (rowCounter % 2 == 0) {
                htmlRows.append("<tr style='background-color:#f2f2f2;'>");  // First color
            } else {
                htmlRows.append("<tr style='background-color:#ffffff;'>");  // Second color
            }

            // Add each column data in the row
            for (int i = 1; i <= columnCount; i++) {
                String columnData = resultSet.getString(i);
                htmlRows.append("<td>").append(columnData != null ? columnData : "NULL").append("</td>");
            }
            htmlRows.append("</tr>");
            rowCounter++;
        }

        // Close the table
        htmlRows.append("</table>");

        // If no rows found, display a message
        if (rowCounter == 0) {
            htmlRows.append("<p>No data found for your query.</p>");
        }

        // Return the HTML table as a string
        return htmlRows.toString();
    }
}
