//import pubprocess.*;
import pubprocess.PubProcess;
import pubprocess.ReplacedItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.KeyEvent;

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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;

public class BookPub extends Application {
    private TextField pathField = new TextField("Select a text file");;
    private ListView<String> replaceListView = new ListView<>();

    @Override
    public void start(Stage stage) {
        /* init config parameter */
        ReplacedItem replacedItem = new ReplacedItem();

        TabPane tabs = new TabPane();
        Tab mainTab = new Tab();
        Tab confTab = new Tab();

        mainTab.setText("Convert");
        mainTab.setClosable(false);
        mainTab.setContent(mainPane(stage, replacedItem));

        confTab.setText("Config");
        confTab.setClosable(false);
        confTab.setContent(configPane(replacedItem));

        tabs.getTabs().addAll(mainTab, confTab);

        Scene scene = new Scene(tabs, 600, 400);

        stage.setTitle("Book Pub");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane mainPane(Stage stage, ReplacedItem replacedItem) {
        BorderPane pane = new BorderPane();
        pane.setTop(srcBox(stage));
        pane.setBottom(createConvertButton(replacedItem));
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

    private HBox createConvertButton(ReplacedItem replacedItem) {
        HBox box = new HBox(8);
        Button cvtButton = new Button("Convert");

        cvtButton.setOnAction((ActionEvent) -> {
            PubProcess pub = new PubProcess(pathField.getText());
            pub.convertFile(replacedItem);
        });

        box.setPadding(new Insets(12));
        box.getChildren().addAll(cvtButton);

        return box;
    }

    private BorderPane configPane(ReplacedItem replacedItem) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(12, 0, 12, 12));

        replaceListView.setItems(replacedItem.getItems());
        replaceListView.setMaxHeight(Control.USE_PREF_SIZE);
        replaceListView.setPrefWidth(150.0);

        pane.setCenter(replaceListView);
        pane.setRight(createEditButton(replacedItem));
        pane.setBottom(createSetButton());

        return pane;
    }

    private VBox createEditButton(ReplacedItem replacedItem) {
        VBox box = new VBox(8);
        Button addButton = new Button("Add");
        Button delButton = new Button("Delete");
        Button clrButton = new Button("All Delete");

        addButton.setOnAction((ActionEvent) -> {
            addNewReplaceWord(replacedItem);
        });

        delButton.setOnAction((ActionEvent) -> {
            int idx = replaceListView.getSelectionModel().getSelectedIndex();
            replacedItem.removeItem(idx);
        });

        clrButton.setOnAction((ActionEvent) -> {
            replacedItem.clearAllItems();
        });

        box.setPadding(new Insets(12));
        box.getChildren().addAll(addButton, delButton, clrButton);
        return box;
    }

    private void addNewReplaceWord(ReplacedItem replacedItem) {
        BorderPane pane = new BorderPane();
        TextField field = new TextField();
        HBox box = new HBox(8);
        Button addButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        box.setPadding(new Insets(12));
        box.getChildren().addAll(addButton, cancelButton);
        pane.setCenter(field);
        pane.setBottom(box);

        Stage stage = new Stage();
        Scene scene = new Scene(pane, 320, 120);
        stage.setScene(scene);
        stage.show();

        field.setOnKeyPressed((keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (replacedItem.addItem(field.getText())) {
                    stage.close();
                }
            }
        });

        addButton.setOnAction((ActionEvent) -> {
            if (replacedItem.addItem(field.getText())) {
                stage.close();
            }
        });

        cancelButton.setOnAction((ActionEvent) -> {
            stage.close();
        });
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

