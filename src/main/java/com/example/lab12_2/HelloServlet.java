package com.example.lab12_2;

import java.io.*;
import java.sql.*;
import java.util.Collection;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static java.lang.System.out;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    private Connection connection;
    Statement statement;
    public void init() {
        message = "Hello World!";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/agencydb","root","");
            String sql = "CREATE TABLE IF NOT EXISTS cars (id INT AUTO_INCREMENT PRIMARY KEY,manufacturer" +
                    " VARCHAR(255) NOT NULL,model VARCHAR(255) NOT NULL,engine_volume FLOAT" +
                    ",manufacture_year INT,color VARCHAR(50),car_type ENUM('Седан', 'Хетчбек', 'Універсал'))";
            statement = connection.createStatement();
            statement.executeUpdate(sql);

            //String sqlFilling = "INSERT INTO cars (manufacturer, model, engine_volume, manufacture_year, color, " +
            //        "car_type)VALUES('Honda', 'Civic', 1.8, 2019, 'Червоний', 'Седан')," +
            //        "('Volkswagen', 'Golf', 1.4, 2018, 'Синій', 'Хетчбек')," +
            //        "('Ford', 'Focus', 2.0, 2021, 'Білий', 'Хетчбек')";
            //statement.executeUpdate(sqlFilling);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String sql = "SELECT * FROM cars";
        String sql2 = "SELECT manufacturer FROM cars";
        String sql3 = "SELECT manufacturer, COUNT(*) AS car_count FROM cars GROUP BY manufacturer";
        String sql4 = "SELECT manufacturer, COUNT(*) AS car_count FROM cars GROUP BY manufacturer " +
                "ORDER BY car_count DESC LIMIT 1";
        String sql5 = "SELECT manufacturer, COUNT(*) AS car_count FROM cars GROUP BY manufacturer " +
                "ORDER BY car_count ASC LIMIT 1";

        int year = 2021;

        String sql6 = "SELECT * FROM cars WHERE manufacture_year = " + year;

        int startYear = 2018;
        int endYear = 2024;

        String sql7 = "SELECT * FROM cars WHERE manufacture_year BETWEEN " + startYear + " AND " + endYear;

        String manufacturer2 = "Volkswagen";
        String sql8 = "SELECT * FROM cars WHERE manufacturer = '" + manufacturer2 + "'";


        String color2 = request.getParameter("color");
        String sql9 = "SELECT * FROM cars WHERE color = '" + color2 + "'";

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Всі виробники</h2>");
            while(resultSet.next()){
                String firstname = resultSet.getString("manufacturer");
                String lastname = resultSet.getString("model");
                String engine_volume = resultSet.getString("engine_volume");
                String manufacture_year = resultSet.getString("manufacture_year");
                String color = resultSet.getString("color");
                out.println("<p>" + firstname + " " + lastname + " " + engine_volume + " " + manufacture_year + " "
                        + color + "</p>");

            }

            ResultSet resultSet2 = statement.executeQuery(sql2);
            out.println("<h2>Всі виробники автомобілів:</h2>");
            while (resultSet2.next()) {
                String manufacturer = resultSet2.getString("manufacturer");
                out.println("<p>" + manufacturer + "</p>");
            }

            ResultSet resultSet3 = statement.executeQuery(sql3);
            out.println("<h2>Кількість автомобілів за кожним виробником:</h2>");

            while (resultSet3.next()) {
                String manufacturer = resultSet3.getString("manufacturer");
                int carCount = resultSet3.getInt("car_count");
                out.println("<p>" + manufacturer + ": " + carCount + " автомобілів</p>");
            }

            ResultSet resultSet4 = statement.executeQuery(sql4);
            out.println("<h2>Виробник з найбільшою кількістю автомобілів:</h2>");

            if (resultSet4.next()) {
                String manufacturer = resultSet4.getString("manufacturer");
                int carCount = resultSet4.getInt("car_count");
                out.println("<p>" + manufacturer + " має найбільшу кількість автомобілів: " + carCount +
                        " автомобілів</p>");
            } else {
                out.println("<p>Немає даних про автомобілі.</p>");
            }


            ResultSet resultSet5 = statement.executeQuery(sql5);
            out.println("<h2>Виробник з найменшою кількістю автомобілів:</h2>");

            if (resultSet5.next()) {
                String manufacturer = resultSet5.getString("manufacturer");
                int carCount = resultSet5.getInt("car_count");
                out.println("<p>" + manufacturer + " має найменшу кількість автомобілів: " + carCount +
                        " автомобілів</p>");
            } else {
                out.println("<p>Немає даних про автомобілі.</p>");
            }


            ResultSet resultSet6 = statement.executeQuery(sql6);
            out.println("<h2>Автомобілі, випущені у " + year + " році:</h2>");

            if (!resultSet6.isBeforeFirst()) {
                out.println("<p>Немає даних про автомобілі, випущені у " + year + " році.</p>");
            } else {
                while (resultSet6.next()) {
                    String manufacturer = resultSet6.getString("manufacturer");
                    String model = resultSet6.getString("model");
                    String engineVolume = resultSet6.getString("engine_volume");
                    String color = resultSet6.getString("color");
                    out.println("<p>" + manufacturer + " " + model + ", Об'єм двигуна: " + engineVolume +
                            ", Колір: " + color + "</p>");
                }
            }


            ResultSet resultSet7 = statement.executeQuery(sql7);
            out.println("<h2>Автомобілі, випущені у роках " + startYear + " - " + endYear + ":</h2>");
            if (!resultSet7.isBeforeFirst()) {
                out.println("<p>Немає даних про автомобілі у вказаному діапазоні років.</p>");
            } else {
                while (resultSet7.next()) {
                    String manufacturer = resultSet7.getString("manufacturer");
                    String model = resultSet7.getString("model");
                    String engineVolume = resultSet7.getString("engine_volume");
                    String color = resultSet7.getString("color");
                    out.println("<p>" + manufacturer + " " + model + ", Об'єм двигуна: " + engineVolume +
                            ", Колір: " + color + "</p>");
                }
            }


            ResultSet resultSet8 = statement.executeQuery(sql8);
            out.println("<h2>Автомобілі виробника " + manufacturer2 + ":</h2>");

            if (!resultSet8.isBeforeFirst()) {
                out.println("<p>Немає даних про автомобілі цього виробника.</p>");
            } else {
                while (resultSet8.next()) {
                    String model = resultSet8.getString("model");
                    String engineVolume = resultSet8.getString("engine_volume");
                    String manufactureYear = resultSet8.getString("manufacture_year");
                    String color = resultSet8.getString("color");
                    out.println("<p>" + model + ", Об'єм двигуна: " + engineVolume + ", Рік випуску: " +
                            manufactureYear + ", Колір: " + color + "</p>");
                }
            }


            ResultSet resultSet9 = statement.executeQuery(sql9);
            out.println("<h1>Задавайте значення у параметрах приклад: " +
                    "http://localhost:8081/Lab12_2_war_exploded/hello-servlet?color=Синій</h1>");
            out.println("<h2>Автомобілі кольору " + color2 + ":</h2>");

            if (!resultSet9.isBeforeFirst()) {
                out.println("<p>Немає автомобілів цього кольору.</p>");
            } else {
                while (resultSet9.next()) {
                    String manufacturer = resultSet9.getString("manufacturer");
                    String model = resultSet9.getString("model");
                    String engineVolume = resultSet9.getString("engine_volume");
                    String manufactureYear = resultSet9.getString("manufacture_year");
                    out.println("<p>" + manufacturer + " " + model + ", Об'єм двигуна: " + engineVolume + ", " +
                            "Рік випуску: " + manufactureYear + "</p>");
                }
            }


            out.println("</body></html>");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
    }
}