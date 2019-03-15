import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import thrift.WinAPIFunction;

class InsertDialog extends Stage {

    public InsertDialog(Stage owner, Controller holder) {
        super();
        initOwner(owner);
        setTitle("Add record");
        initModality(Modality.WINDOW_MODAL);
        Group root = new Group();
        Scene scene = new Scene(root, 700, 450, Color.WHITE);
        setScene(scene);
        setResizable(false);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(4);
        gridpane.setVgap(10);

        Label name = new Label("Name: ");
        gridpane.add(name, 0, 1);
        final TextField nameFld = new TextField();
        gridpane.add(nameFld, 0, 2);

        Label params = new Label("Params: ");
        gridpane.add(params, 0, 3);
        final TextField paramsFld = new TextField();
        gridpane.add(paramsFld, 0, 4);

        Label returnValues = new Label("Return values: ");
        gridpane.add(returnValues, 0, 5);
        final TextField returnValuesFld = new TextField();
        gridpane.add(returnValuesFld, 0, 6);

        Label description = new Label("Description: ");
        gridpane.add(description, 1, 1);
        final TextArea descriptionArea = new TextArea();
        gridpane.add(descriptionArea, 1, 2, 10, 12);


        Button add = new Button("Ok");
        add.resize(80,30);
        add.setOnAction(actionEvent -> {
            if(nameFld.getText().isEmpty()) {
                holder.showErrorAlert(
                        "Data error",
                        "Field \"Name\" could not be empty");
            } else {
                buildWinAPIFunctionObject(holder, nameFld, paramsFld, returnValuesFld, descriptionArea);
                close();
            }
        });
        gridpane.add(add, 4, 15);

        Button close = new Button("Close");
        close.resize(80, 30);
        close.setOnAction(event -> close());
        gridpane.add(close, 8, 15);

        GridPane.setHalignment(close, HPos.RIGHT);
        root.getChildren().add(gridpane);
    }

    private void buildWinAPIFunctionObject(Controller holder, TextField nameFld, TextField paramsFld, TextField returnValuesFld, TextArea descriptionArea) {
        final WinAPIFunction func = new WinAPIFunction();
        func.setName(nameFld.getText());
        func.setParams(paramsFld.getText());
        func.setReturnValue(returnValuesFld.getText());
        func.setDescription(descriptionArea.getText());
        holder.addRow(func);
    }
}