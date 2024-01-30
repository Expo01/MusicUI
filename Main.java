package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.Datasource;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.listArtists();

        primaryStage.setTitle("Music Database");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
//        Datasource.getInstance().open(); // this will open UI but will not inherently open the DB. need to include the driver
        if (!Datasource.getInstance().open()){
            System.out.println("FATAL ERROR: Couldn't connect to DB");
            Platform.exit();
        } // instead of just trying to open the DB,we include the error message and exit is exectuted
        // otherwise the UI would still populate but would not function properly
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close(); // closes the DB
    }

    public static void main(String[] args) {
        launch(args);

    }
}

// copy pasted the sample package from the Music project into sample
// package of MusicUI
// since the Datasource class was directly copied, the path to the DB was also directly copied
// "jdbc:sqlite:/Users/chrisdailey/Documents/CODING/Projects/Music/"
// which means that the database being modified is actually there?

// root level package of MusicUI --> open module settings --> libraries --> + --> java --> find the sqlite jdbc
// file and add. this puts the driver into the class path so not only the UI opens but also connects to the DB

// updating DB more important than UI? DB fails more often than UI with CRUD functions?
// so want to make sure DB updated before trying to change the UI instead of UI first, assuming the DB updated2
// run insert, update or delete using a Task and then updateOnSucceeded method to update UI

/*
PROBLEMS
- model package didn't exist. optimized imports and no more error
-Datasource made package private
- after these two fixes, still no UI populated.

- something is wrong in the overloaded init method, where it is no longer populating the UI
-  Datasource.getInstance().open(); as line ~29 functions as intended. some error occuring after...
- note that when i copied my model package over into sample, the main class from Music project
came with it and I had to delete it. Not sure if this is causing errors



- albums, artists, controller, main the same
 */