<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accountant Home</title>
    <style>
        body {
            background-color: black;
            color: white;
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 20px;
        }
        .toggle-container {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 10px;
            margin: 10px 0;
            padding-left: 40px; /* Indentation for radio buttons */
        }
        .option {
            display: flex;
            align-items: center;
            gap: 10px;
            cursor: pointer;
            font-size: 18px;
        }
        .option input[type="radio"] {
            appearance: none;
            width: 20px;
            height: 20px;
            border: 2px solid white;
            border-radius: 50%;
            background-color: transparent;
            cursor: pointer;
            margin: 0;
            transition: background-color 0.3s, border-color 0.3s;
        }
        .option input[type="radio"]:checked {
            background-color: #4CAF50;
            border-color: #4CAF50;
        }
        .button-container {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-top: 10px;
        }
        .button {
            padding: 10px 20px;
            background-color: #FF5733;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .button:hover {
            background-color: #E74C3C;
        }
        .spacer {
            margin: 20px 0;
            border-bottom: 1px solid white;
        }
        #result table {
            width: 100%;
            border-collapse: collapse;
        }
        #result th, #result td {
            border: 1px solid white;
            padding: 8px;
            text-align: left;
        }
        #result th {
            background-color: #555;
        }
        #result {
            margin-top: 20px;
            padding: 10px;
            background-color: #333;
            border: 1px solid white;
            border-radius: 5px;
        }
    </style>
</head>
<body>

    <h1>Welcome to the Fall 2024 Project 4 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application using a Tomcat container</h2>
    <div class="spacer"></div>

    <p>You are connected to the Project 4 Enterprise System Database as an accountant-level user.</p>
    <p>Please select an SQL Query or update command below:</p>

    <form action="AccountantUserServlet" method="POST">
        <div class="toggle-container">
            <label class="option">
                <input type="radio" name="command" value="1">
                Get the maximum status value of all suppliers (returns a max value)
            </label>
            <label class="option">
                <input type="radio" name="command" value="2">
                Get the total weight of all parts (returns sum)
            </label>
            <label class="option">
                <input type="radio" name="command" value="3">
                Get the total number of shipments (returns the current number of shipments in total)
            </label>
            <label class="option">
                <input type="radio" name="command" value="4">
                Get the name and number of workers of the job with most workers (returns two values)
            </label>
            <label class="option">
                <input type="radio" name="command" value="5">
                List the name and status of every supplier (returns a list of supplier names with status)
            </label>
        </div>

        <div class="button-container">
            <button class="button" type="submit">Execute Selected Cmd</button>
            <button class="button" type="reset">Clear Cmd</button>
        </div>
    </form>

    <div class="spacer"></div>
    <p><strong>Execution Results</strong></p>

    <div id="result">
        <%
            String resultTable = (String) request.getAttribute("resultTable");
            String resultMessage = (String) request.getAttribute("resultMessage");
            if (resultMessage != null) {
                out.println("<p>Message: " + resultMessage + "</p>");
            }
            if (resultTable != null) {
                out.println(resultTable);
            } else {
                out.println("<p>No results available.</p>");
            }
        %>
    </div>

</body>
</html>
