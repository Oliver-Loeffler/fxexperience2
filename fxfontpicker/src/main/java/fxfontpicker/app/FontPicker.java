package fxfontpicker.app;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontPicker extends AnchorPane {
    
    @FXML private ComboBox<String> familyComboBox;
    @FXML private ChoiceBox<String> styleChoiceBox;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private Slider sl_font_size;
    @FXML private TextArea sampleFontText;

    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(Font.getDefault());

    public Font getFont() {
        return font.get();
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        assert font instanceof Font;
        
        this.font.set(font);
    }
    
    public FontPicker() {
       
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FontPicker.class.getResource("/fxml/FXMLFontPicker.fxml")); //NOI18N
            loader.setController(FontPicker.this);
            loader.setRoot(FontPicker.this);

            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FontPicker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final NumberFormat nf = DecimalFormat.getInstance(Locale.getDefault());
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(5);

        // Family font combo-box
        familyComboBox.getItems().setAll(Font.getFamilies());
        familyComboBox.getSelectionModel().select(0);
        familyComboBox.valueProperty().addListener(observable -> changeFont(nf));
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
        styleChoiceBox.valueProperty().addListener(observable -> changeFont(nf));

        // Size font combo-box
        sizeComboBox.getItems().setAll(FXCollections.observableArrayList(getDefaultFontSizes(nf)));
        sizeComboBox.getSelectionModel().select(4);
        sizeComboBox.valueProperty().addListener(observable -> changeFont(nf));
        sampleFontText.fontProperty().bind(font);

    }

    private double parseDouble(NumberFormat nf, String value) {
    	try {
    		return nf.parse(value).doubleValue();
    	} catch (java.text.ParseException e) {
    		return Double.NaN;
		}
    }
    
    private double getNewFontSize(NumberFormat nf) {
		double size = parseDouble(nf, sizeComboBox.getValue());
        if (!Double.isFinite(size)) {
        	size = font.get().getSize();
        	sizeComboBox.setValue(nf.format(size));
        }
		return size;
	}
    
    private void changeFont(NumberFormat nf) {
        FontWeight weight = styleChoiceBox.getSelectionModel().isSelected(0) ||
                styleChoiceBox.getSelectionModel().isSelected(1)
                ? FontWeight.BOLD : FontWeight.NORMAL;
        FontPosture posture = styleChoiceBox.getSelectionModel().isSelected(1) ||
                styleChoiceBox.getSelectionModel().isSelected(2)
                ? FontPosture.ITALIC : FontPosture.REGULAR;
        String family = familyComboBox.getValue();
        
        double size = getNewFontSize(nf);
        font.setValue(Font.font(family, weight, posture, size));
    	sl_font_size.setValue(size);
    }

	
    
    @FXML 
    private void onSliderUpdate(MouseEvent event) {
        sizeComboBox.setValue(Integer.toString((int) sl_font_size.getValue()));
    }
    
    private List<String> getDefaultFontSizes(NumberFormat numberFormat) {
        double[] predefinedFontSizes
                = { 9.0, 10.0, 11.0, 12.0,
                   13.0, 14.0, 18.0, 24.0, 36, 48, 76};
        
        return DoubleStream.of(predefinedFontSizes)
		        			.mapToObj(numberFormat::format)
		        			.collect(Collectors.toList());
        
    }
}