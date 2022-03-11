/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.model.Account;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author RnaBo
 */
public class MailClientFx extends Application {

    private static Stage mainStage;
    private static Account activeAccount;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Account getActiveAccount() {
        return activeAccount;
    }

    public static void setActiveAccount(Account ac) {
        activeAccount = ac;
    }
    
    

    @Override
    public void start(Stage primaryStage) throws IOException {

        mainStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("views/LoginWindow.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }
    

}
