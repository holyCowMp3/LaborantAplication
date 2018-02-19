

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
public class Controller {

    @FXML
    private ChoiceBox<?> groupChoiceBox;

    @FXML
    private ComboBox<?> teachersComboBox;

    @FXML
    private ComboBox<?> auditoryComboBox;

    @FXML
    private ComboBox<?> lessonsComboBox;

    @FXML
    private TextField numberOfLessonTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button executeEvent;

    @FXML
    private TableColumn<?, ?> teacherColumn;

    @FXML
    private TableColumn<?, ?> mondayColumn;

    @FXML
    private TableColumn<?, ?> firstMon;

    @FXML
    private TableColumn<?, ?> secondMon;

    @FXML
    private TableColumn<?, ?> tuesdayColumn;

    @FXML
    private TableColumn<?, ?> wednesdayColumn;

    @FXML
    private TableColumn<?, ?> thursdayColumn;

    @FXML
    private TableColumn<?, ?> fridayColumn;

    @FXML
    private TableColumn<?, ?> saturdayColumn;

    @FXML
    private TableColumn<?, ?> groupLesson;

    @FXML
    void getLessonType(ActionEvent event) {
        System.out.println(((RadioButton) event.getSource()).getText());
    }

}
