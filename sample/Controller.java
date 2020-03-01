package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Random;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;


public class Controller  implements Initializable
{
    @FXML
    JFXListView ListView;
    @FXML
    JFXTextField MaximumNumber;
    @FXML
    JFXTextField MinimumNumber;
    @FXML
    Label randomNumbersLabel;
    @FXML
    JFXButton RunButton;
    @FXML
    JFXButton LoadDataButton;
    @FXML
    JFXComboBox Numbers;


    final String hostname= "midterm.cudmgzc9w5uz.us-east-1.rds.amazonaws.com";
    final String dbName= "testdb";
    final String port= "3306";
    final String userName= "anthonymvu";
    final String password= "430606vu";
    final String AWS_URL= "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
    @Override
    public void initialize(URL location, ResourceBundle resource)

    {
            try{
                Connection conn = DriverManager.getConnection(AWS_URL);
                Statement stmt = conn.createStatement();

                     stmt.execute("CREATE TABLE Random Numbers (" +
                            "generateRandomNumbers CHAR(30) )");

                    System.out.println("TABLE CREATED");

                    stmt.close();
                    conn.close();
                }
                catch (Exception ex)
                {
                    var msg = ex.getMessage();
                    System.out.println("TABLE ALREADY EXISTS, TABLE NOT CREATED");
                }

            RunButton.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent)
                {

                    try {
                        Connection conn = DriverManager.getConnection(AWS_URL);
                        Statement stmt = conn.createStatement();

                        int Min = Integer.parseInt(MinimumNumber.getText());
                        int Max = Integer.parseInt(MaximumNumber.getText());
                        {
                            System.out.println("Enter at least one Minimum and one Maximum number");
                        }

                        Random numbers = new Random();
                        int generateRandomNumbers = numbers.nextInt((Max - Min) + 1) + Min;
                        String sql = "INSERT INTO RANDOM NUMBERS" +
                                "(' " + generateRandomNumbers + " ')";

                        randomNumbersLabel.setText(String.valueOf(generateRandomNumbers));

                        stmt.close();
                        conn.close();


                    } catch (Exception ex)
                    {
                        var msg = ex.getMessage();
                        System.out.println("NUMBERS GENERATED DOES NOT MEET REQUIREMENTS THEREFORE IT IS INVALID. PLEASE TRY AGAIN");
                    }
                }
});

         LoadDataButton.setOnAction( new EventHandler<ActionEvent>()
         {
        @Override
        public void handle(ActionEvent actionEvent) {
        try {
            Connection conn = DriverManager.getConnection(AWS_URL);
            Statement stmt = conn.createStatement();
            String query = "SELECT Generate FROM Random Numbers";
            queryResult(stmt, query);

            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            var msg = ex.getMessage();
            System.out.println(msg);
        }
    }
});
}
    private void queryResult(Statement statement, String sqlQuery) throws SQLException

    {
        ResultSet data = statement.executeQuery(sqlQuery);
        ObservableList<Numbers> RandomlyGeneratedNumbersDB = FXCollections.observableArrayList();
        while (data.next())
        {
            Numbers num = new Numbers();
            num.generateRandomNumbers = data.getString("Generate Random Numbers");

            RandomlyGeneratedNumbersDB.add(num);
        }

        ListView.setItems(RandomlyGeneratedNumbersDB);
        Numbers.setItems(RandomlyGeneratedNumbersDB);
    }
}
