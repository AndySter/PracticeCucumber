package steps;

import io.cucumber.java.After;
import io.cucumber.java.bg.И;

import java.sql.*;
import java.util.List;

public class WorkwithJDBC {
    static Connection connection;
    static ResultSet resultSet;
    static Statement statement;

    @И("подключение к бд по адресу {string}, и параметрами: user {string}, password {string}")
    public void connectDb(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    @И("проверка, что добавляемого товара {string} еще нет в таблице {string}")
    public void checkDb(String name, String table) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM "+ table +" WHERE FOOD_NAME = '"+  name +"'");
        while (resultSet.next()) {
            if(resultSet.getInt("row_count") != 0){
                throw new IllegalStateException("Такой товар уже есть в БД");
            }
        }
    }

    @И("проверка, что добавляемый товар {string} уже есть в таблице {string}")
    public void checkAlreadyDb(String name, String table) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) AS row_count FROM "+ table +" WHERE FOOD_NAME = '"+  name +"'");
        while (resultSet.next()) {
            if(resultSet.getInt("row_count") == 0){
                throw new IllegalStateException("Такого товара еще нет в БД");
            }
        }
    }

    @И("проверка, что добавленный товар появился в таблице:")
     public void checkAddDb(List<String> table) throws SQLException, InterruptedException {
        Thread.sleep(1000);
        resultSet = statement.executeQuery("SELECT COUNT(*) AS count_name FROM "+  table.get(0) +" \n" +
                "WHERE FOOD_NAME = '"+  table.get(1) +"' AND FOOD_TYPE = '"+  table.get(2) +"' AND FOOD_EXOTIC ='"+  table.get(3) +"' " +
                "AND FOOD_ID = (SELECT MAX(FOOD_ID) FROM "+  table.get(0) +")");
        if (resultSet.next()) {
            if(resultSet.getInt("count_name") < 1){
                throw new IllegalStateException("Товар не добавился");
            }
        }
    }

    @И("проверка, что продублированный товар появился в таблице:")
    public void checkAddCopy(List<String> table) throws SQLException, InterruptedException {
        Thread.sleep(1000);
        resultSet = statement.executeQuery("SELECT COUNT(*) AS count_name FROM "+  table.get(0) +" \n" +
                "WHERE FOOD_NAME = '"+  table.get(1) +"' AND FOOD_TYPE = '"+  table.get(2) +"' AND FOOD_EXOTIC ='"+  table.get(3) +"' ");
        if (resultSet.next()) {
            if(resultSet.getInt("count_name") < 2){
                throw new IllegalStateException("Одинаковых товаров нет");
            }
        }
    }

    @И("удаление товара {string} из таблицы {string}")
    public void deleteProduct(String name, String table) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM "+  table +" " +
                "WHERE FOOD_ID = (SELECT MAX(FOOD_ID) FROM "+  table +") AND FOOD_NAME = '"+  name +"'");
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Последняя добавленная строка успешно удалена.");
        } else {
            System.out.println("Таблица пуста или нет строк для удаления.");
        }
    }

    @И("проверка, что добавленного товара нет в таблице:")
    public void checkAfterDb(List<String> table) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) AS count_name FROM "+  table.get(0) +" \n" +
                "WHERE FOOD_NAME = '"+  table.get(1) +"' AND FOOD_TYPE = '"+  table.get(2) +"' AND FOOD_EXOTIC ='"+  table.get(3) +"' " +
                "AND FOOD_ID = (SELECT MAX(FOOD_ID) FROM "+  table.get(0) +")");
        if (resultSet.next()) {
            if(resultSet.getInt("count_name") != 0){
                throw new IllegalStateException("Товар не был удален");
            }
        }
    }

    @И("проверка, что продублированного товара нет в таблице:")
    public void checkAfterDbCopy(List<String> table) throws SQLException {
        resultSet = statement.executeQuery("SELECT COUNT(*) AS count_name FROM "+  table.get(0) +" \n" +
                "WHERE FOOD_NAME = '"+  table.get(1) +"' AND FOOD_TYPE = '"+  table.get(2) +"' AND FOOD_EXOTIC ='"+  table.get(3) +"' ");
        if (resultSet.next()) {
            if(resultSet.getInt("count_name") > 1){
                throw new IllegalStateException("Товар не был удален");
            }
        }
    }

   @И("отключение от базы данных")
    public void exitDb() throws SQLException {
        connection.close();
    }

}
