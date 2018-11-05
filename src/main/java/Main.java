import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 *创建一个工具用于添加数据项
 *
 */

public class Main extends Application {


    @Override

    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("客户端程序");

        primaryStage.setScene(new Scene(root, 1100, 475));

        primaryStage.show();

        //初始化一些数据

    }

    public static void main(String[] args) {

        launch(args);

    }

}
