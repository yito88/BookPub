package pubprocess;

import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReplacedItem {
    private ObservableList<String> replacedItems;

    public ReplacedItem() {
        replacedItems = FXCollections.observableArrayList("◇", "□", "■", "◆", "▽", "○");
    }

    public ObservableList<String> getItems() {
        return replacedItems;
    }

    public boolean addItem(String word) {
        if (replacedItems.contains(word)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Already Set");
            alert.setHeaderText(null);
            alert.setContentText("This word has been already set!");
            alert.showAndWait();
            return false;
        } else {
            replacedItems.add(word);
            return true;
        }
    }

    public void removeItem(int index) {
        replacedItems.remove(index);
    }

    public void clearAllItems() {
        replacedItems.clear();
    }
}
