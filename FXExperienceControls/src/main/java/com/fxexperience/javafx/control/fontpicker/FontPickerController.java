package com.fxexperience.javafx.control.fontpicker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.fxexperience.javafx.scene.control.colorpicker.ColorPickerControl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontPickerController extends AnchorPane {

    private final ObservableList<String> fixedSizes;
    
    private final NumberFormat numberFormat;

    @FXML private ComboBox<String> familyComboBox;
    @FXML private ChoiceBox<String> styleChoiceBox;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private ToggleGroup fontRadioGroup;
    @FXML private RadioButton fixedFontRadio;
    @FXML private RadioButton scalableFontRadio;
    @FXML private TextArea sampleFontText;

    private ObjectProperty<Font> font;

    public Font getFont() {
        return font.get();
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        this.font.set(font);
    }

    public FontPickerController() {
    	this.numberFormat = DecimalFormat.getInstance(Locale.getDefault());
    	this.fixedSizes = FXCollections.observableArrayList(getFixedFontSizes());
    	this.font =  new SimpleObjectProperty<>(Font.getDefault());
    	
        initialize();
    }

    /**
     * Private
     */
    private void initialize() {

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ColorPickerControl.class.getResource("/fxml/FXMLFontPicker.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ColorPickerControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(5);

        // Family font combo-box
        familyComboBox.getItems().setAll(Font.getFamilies());
        familyComboBox.getSelectionModel().select(0);

        familyComboBox.valueProperty().addListener(observable -> changeFont());

        familyComboBox.setCellFactory((ListView<String> listView) -> {
            final ListCell<String> cell = new ListCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setFont(new Font(item, 12));
                    }
                }
            };
            cell.setPrefWidth(200);
            return cell;
        });

        // Style font combo-box
        styleChoiceBox.setItems(FXCollections.observableArrayList(
                "Bold", "Bold Italic", "Italic", "Regular"));
        styleChoiceBox.getSelectionModel().select(3);
        styleChoiceBox.valueProperty().addListener(observable -> changeFont());

        // Size font combo-box
        sizeComboBox.getItems().setAll(fixedSizes);
        sizeComboBox.getSelectionModel().select(4);
        sizeComboBox.valueProperty().addListener(observable -> 
        changeFont()
        );
    }

    private void changeFont() {
        try {
            double size = numberFormat.parse(sizeComboBox.getValue()).doubleValue();

            FontWeight weight = styleChoiceBox.getSelectionModel().isSelected(0) ||
                    styleChoiceBox.getSelectionModel().isSelected(1)
                    ? FontWeight.BOLD : FontWeight.NORMAL;
            FontPosture posture = styleChoiceBox.getSelectionModel().isSelected(1) ||
                    styleChoiceBox.getSelectionModel().isSelected(2)
                    ? FontPosture.ITALIC : FontPosture.REGULAR;
            String family = familyComboBox.getValue();
            font.setValue(Font.font(family, weight, posture, size));
            sampleFontText.setFont(font.get());
        } catch (java.text.ParseException ex) {
            Logger.getLogger(FontPickerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onFixedFontSelected(ActionEvent event) {
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);
        sizeComboBox.getItems().setAll(fixedSizes);
    }

    @FXML
    private void onScalableFontSelected(ActionEvent event) {

    }

    private List<String> getFixedFontSizes() {
        double[] predefinedFontSizes
                = { 9.0, 10.0, 11.0, 12.0,
                   13.0, 14.0, 18.0, 24.0};
        
        
        return DoubleStream.of(predefinedFontSizes)
		        			.mapToObj(numberFormat::format)
		        			.collect(Collectors.toList());					   
        
    }

    private static List<String> getScalableFontSizes() {
        String[] predefinedFontSizes
                = {"0.083333", "0.166667", "0.25", "0.333333", "0.416667", "0.5",
                   "0.583333", "0.666667", "0.75", "0.833333", "0.916667", "1.0" };
        return Arrays.asList(predefinedFontSizes);
    }
}
