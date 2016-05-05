import pubprocess.*;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

public class BookPub extends Application {
    private TextField pathField = new TextField("Select a text file");;
    private ArrayList<String> replaceList = new ArrayList<>(Arrays.asList("◇", "□", "■","◆", "▽", "○"));

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane();
        Tab mainTab = new Tab();
        Tab confTab = new Tab();

        mainTab.setText("Convert");
        mainTab.setClosable(false);
        mainTab.setContent(mainPane(stage));

        confTab.setText("Config");
        confTab.setClosable(false);
        confTab.setContent(configPane());

        tabs.getTabs().addAll(mainTab, confTab);

        Scene scene = new Scene(tabs, 600, 400);

        stage.setTitle("Book Pub");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane mainPane(Stage stage) {
        BorderPane pane = new BorderPane();
        pane.setTop(srcBox(stage));
        pane.setBottom(createConvertButton());
        return pane;
    }

    private HBox srcBox(Stage stage) {
        HBox srcBox = new HBox(8);
        Label selectLabel = new Label("Source File:");;
        Button selectFileButton = new Button("Select");
        selectFileButton.setOnAction((ActionEvent) -> {
            pathField.setText(selectTextFile(stage));
        });

        srcBox.setPadding(new Insets(12));
        srcBox.getChildren().add(selectLabel);
        srcBox.getChildren().add(pathField);
        srcBox.getChildren().add(selectFileButton);
        srcBox.setHgrow(pathField, Priority.ALWAYS);
        srcBox.setAlignment(Pos.CENTER);

        return srcBox;
    }

    private String selectTextFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a file");
        /* set extension filter */
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        String ret;
        if (file != null) {
            ret = file.getPath();
        } else {
            ret = "Select a text file";
        }
        return ret;
    }

    private HBox createConvertButton() {
        HBox box = new HBox(8);
        Button cvtButton = new Button("Convert");

        cvtButton.setOnAction((ActionEvent) -> {
            PubProcess pub = new PubProcess(pathField.getText());
            pub.convertFile();
        });

        box.setPadding(new Insets(12));
        box.getChildren().addAll(cvtButton);

        return box;
    }

    private BorderPane configPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12, 0, 12, 12));

        updateReplaceList(pane);
        pane.setRight(createEditButton(pane));
        pane.setBottom(createSetButton());

        return pane;
    }

    private VBox createEditButton(BorderPane configPane) {
        VBox box = new VBox(8);
        Button delButton = new Button("Delete");
        Button clrButton = new Button("All Delete");

        delButton.setOnAction((ActionEvent) -> {
            // TODO: Delete an item
            //int idx = listView.getSelectionModel().getSelectedIndex();
        });

        clrButton.setOnAction((ActionEvent) -> {
            replaceList.clear();
            updateReplaceList(configPane);
        });

        box.setPadding(new Insets(12));
        box.getChildren().addAll(delButton, clrButton);
        return box;
    }

    private void updateReplaceList(BorderPane configPane) {
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(replaceList);
        list.setItems(items);
        list.setMaxHeight(Control.USE_PREF_SIZE);
        list.setPrefWidth(150.0);
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView());

        configPane.setCenter(list);
    }

    private HBox createSetButton() {
        HBox box = new HBox(8);
        Button applyButton = new Button("Apply");
        Button cancelButton = new Button("Cancel");
        Button resetButton = new Button("Reset");

        box.setPadding(new Insets(12));
        box.getChildren().addAll(applyButton, cancelButton, resetButton);

        return box;
    }

    public static void main(String [] args) {
        launch(args);
    }
}

