<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client User Home</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
            padding: 20px;
            text-align: center;
        }
        textarea {
            width: 80%;
            height: 200px;
            resize: both;
            margin: 10px 0;
            border-radius: 5px;
            border: none;
            padding: 10px;
        }
        .button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin: 5px;
        }
        .button:hover {
            background-color: #45a049;
        }
        .spacer {
            margin: 20px 0;
            border-bottom: 1px solid white;
        }
        #result {
            margin-top: 20px;
            padding: 10px;
            background-color: #333;
            border: 1px solid white;
            border-radius: 5px;
        }
        table {
            width: 100%;
            border: 1px solid white;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #333;
        }
        td {
            background-color: #555;
        }
    </style>
</head>
<body>

    <h1>Welcome to the Fall 2024 Project 4 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application using a Tomcat container</h2>
    <div class="spacer"></div>

    <p>You are connected to the Project 4 Enterprise System Database as a client-level user.</p>
    <p>Please enter any SQL Query or update command in the box below</p>

    <!-- Form to submit SQL queries -->
    <form action="ClientUserServlet" method="POST">
        <textarea name="sqlQuery" placeholder="Enter your input here..."></textarea>

        <div>
            <button class="button" type="submit">Execute Command</button>
            <button class="button" type="reset">Reset Form</button>
            <button class="button" type="button" onclick="clearResult()">Clear Results</button> <!-- New button -->
        </div>
    </form>

    <p>All execution results will appear below this line</p>
    <div class="spacer"></div>

    <p><strong>Execution Results:</strong></p>

    <div id="result">
        <%
        // Get the query execution status and result attributes
        String resultMessage = (String) request.getAttribute("resultMessage");
        String resultTable = (String) request.getAttribute("resultTable");

            if (resultTable != null) {
                out.println(resultTable);  // Display the result table
                resultMessage = null;
            } 
            if (resultMessage != null) {
                out.println("<p>" + resultMessage + "</p>");
            } 
            if(resultTable == null && resultMessage == null) {
                out.println("<p>No results or update information available.</p>");
            }
    %>
    </div>

    <script>
        // Function to clear the result area
        function clearResult() {
            document.getElementById("result").innerHTML = '';  // Clear the content of the result div
        }
    </script>

</body>
</html>
