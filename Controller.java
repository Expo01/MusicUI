package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import sample.model.Album;
import sample.model.Artist;
import sample.model.Datasource;










public class Controller {
    //controller not created until fxml loaded?

    @FXML
    private TableView artistTable;

    @FXML
    private ProgressBar progressBar;

    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask(); // Task object created
        artistTable.itemsProperty().bind(task.valueProperty()); // Binding result of task
        // 'arists observable list' to tableviews item property?
        progressBar.progressProperty().bind((task.progressProperty()));
        progressBar.setVisible(true);
        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));

        new Thread(task).start(); // without this, the UI will populate but won't actually list the artists
    }

    @FXML
    public void listAlbumsForArtist() {
        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if(artist == null) {
            System.out.println("NO ARTIST SELECTED");
            return;
        }
        Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
            @Override
            protected ObservableList<Album> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().queryAlbumsForArtistId(artist.getId()));
            }
        };
        artistTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

    }

    @FXML
    public void updateArtist() {
//        final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        final Artist artist = (Artist) artistTable.getItems().get(2); // get record

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().updateArtistName(artist.getId(), "AC/DC");
            }
        };

        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                artist.setName("AC/DC");
                artistTable.refresh(); // this method in response to a bug related to javafx
            }
        });

        new Thread(task).start();
    }

}

class GetAllArtistsTask extends Task {

    @Override
    public ObservableList<Artist> call()  {
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryArtists(Datasource.ORDER_BY_ASC));
    }
}