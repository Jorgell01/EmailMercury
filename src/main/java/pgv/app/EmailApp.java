package pgv.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pgv.controllers.LoginController;
import pgv.controllers.EmailController;

public class EmailApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginDialog();
    }

    private void showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EmailApp.class.getResource("/fxml/LoginView.fxml"));
            GridPane loginLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(loginLayout);
            dialogStage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isLoginSuccessful()) {
                showMainView(controller.getEmail(), controller.getPassword());
            } else {
                primaryStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMainView(String email, String password) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EmailApp.class.getResource("/fxml/EmailView.fxml"));
            AnchorPane rootLayout = loader.load();

            EmailController controller = loader.getController();
            controller.setCredentials(email, password);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestor de Correos");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}