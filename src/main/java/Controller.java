import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Controller {
    private static final String APPLICATION_NAME =
            "Google Calendar API";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                Controller.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
    getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public static Map<String, String> getCalendarIdMap() {
        return calendarIdMap;
    }

    /**
     * Reading list of calendars from gAcount creating a list with {@link CalendarListEntry}
     * Create a map with the summary as keys, and id as values;
     * Use @calendarIdMap to save result;
     * Change the calendars where events was use @execute method
     */
    private static Map<String, String> calendarIdMap;


    public static List<CalendarListEntry> getCalendarListEntries() throws Exception {
        List<CalendarListEntry> listEntries;
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            listEntries = calendarList.getItems();
            calendarIdMap = listEntries.stream().collect(Collectors.toMap(i -> i.getSummary(), i -> i.getId()));
            pageToken = calendarList.getNextPageToken();
        }
        while (pageToken != null);

        return listEntries;
    }


    /**
     * Method choose the time of lesson (i), and set the start and end time for events, with pair.
     * Using the @{@link DateTimeFormatter formatterDay} to format selected date from @{@link LocalDate datePicker};
     *
     * @param date  date from DatePicker
     * @param event event which modify;
     * @param i     number of pair;
     * @return event with start and end times;
     */

    private static Event chooseLesson(String i, Event event, LocalDate date) {
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTime startDateTime;
        DateTime endDateTime;
        switch (i) {
            case "1": {
                startDateTime = new DateTime(date.format(formatterDay) + "T09:00:00+03:00");
                endDateTime = new DateTime(date.format(formatterDay) + "T10:35:00+03:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));
                return event;
            }
            case "2": {
                startDateTime = new DateTime(date.format(formatterDay) + "T10:50:00+03:00");
                endDateTime = new DateTime(date.format(formatterDay) + "T12:25:00+03:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));
                return event;
            }
            case "3": {
                startDateTime = new DateTime(date.format(formatterDay) + "T12:40:00+03:00");
                endDateTime = new DateTime(date.format(formatterDay) + "T14:15:00+03:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));
                return event;
            }
            case "4": {
                startDateTime = new DateTime(date.format(formatterDay) + "T15:45:00+03:00");
                endDateTime = new DateTime(date.format(formatterDay) + "T17:20:00+03:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));
                return event;
            }
            case "Сампо": {
                startDateTime = new DateTime(date.format(formatterDay) + "T15:45:00+03:00");
                endDateTime = new DateTime(date.format(formatterDay) + "T18:50:00+03:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev"));
                return event;
            }
            default: {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Невірні данні!");
                alert.setHeaderText("Перевірте правильність вводу");
                alert.setContentText("Будьте обережні наступного разу");
                alert.showAndWait();
            }
        }
        return null;
    }
    private  Scene scene;

    {
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("testView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  Stage stage = new Stage();
    /**
     * @ObservableList<String> list with summary of each calendar;
     */
    private ObservableList<String> calendarListStrings;
    /**
     * @ObservableList<QR> list with generated QR objects;
     */
    private static ObservableList<QR> qrList;
    /**
     * {@link TableView<QR> qrTable} table with QR objects;
     */
    @FXML
    private TableView<QR> qrTable;
    /**
     * Parent AnchorPane as container for the {@link CheckComboBox} using ControlsFX library
     */
    @FXML
    private AnchorPane parentToCombo;

    private CheckComboBox groupChoiceBox;

    @FXML
    private TextField groupTextField;

    @FXML
    private TextField teachersTextField;


    @FXML
    private TextField auditoryTextField;


    @FXML
    private TextField lessonTextField;

    @FXML
    private TextField numberOfLessonTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> pairPicker;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private RadioButton radioButton4;

    @FXML
    private RadioButton radioButton5;

    @FXML
    private RadioButton radioButton6;

    @FXML
    private CheckBox sampoCheckBox;

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
    private TabPane tabPane;

    @FXML
    private WebView googleCalendarWeb;

    private  static  com.google.api.services.calendar.Calendar service;

    static {

        try {
            service = getCalendarService();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String lessonType;

    private static List<String> auditoryList = Collections.EMPTY_LIST;
    private static List<String> teachersList = Collections.EMPTY_LIST;
    private static List<String> lessonList = Collections.EMPTY_LIST;

    @FXML
    void initialize() throws Exception {
    pairPicker.setItems(FXCollections.observableArrayList("1", "2", "3","4"));
        URL page = getClass().getResource("CalendarPages.html");

        googleCalendarWeb.getEngine().load(page.toExternalForm());

        ValidationSupport validationSupport = new ValidationSupport();

        radioButton1.setSelected(true);

        validationSupport.registerValidator(pairPicker, Validator.createEmptyValidator("Потрібно обрати пару"));

        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        // creating list's with the calendars name;
        List<CalendarListEntry> item = getCalendarListEntries();

        calendarListStrings = FXCollections.observableArrayList(item.stream().map(i -> i.getSummary()).collect(Collectors.toList()));
        groupChoiceBox = new CheckComboBox<>(FXCollections.observableArrayList(item.stream()
                .map(i -> i.getSummary())
                .filter(i -> (i.length()==3)&&(Character.isDigit(i.charAt(2))))
                .sorted(Comparator.comparingInt(Integer::parseInt))
                .collect(Collectors.toList())));
        groupChoiceBox.prefWidthProperty().setValue(parentToCombo.getPrefWidth());
        parentToCombo.getChildren().add(groupChoiceBox);

        auditoryList = calendarIdMap.keySet().stream().filter(i -> i.startsWith("ауд.")).collect(Collectors.toList());
        teachersList = calendarIdMap.keySet().stream().filter(i -> i.endsWith(".")).collect(Collectors.toList());
        lessonList = calendarIdMap.keySet().stream().filter(i -> (i.length() < 4) & !Character.isDigit(i.charAt(0))).collect(Collectors.toList());
        // bind autocompletion to textfields;
        TextFields.bindAutoCompletion(auditoryTextField, FXCollections.observableArrayList(auditoryList))
                .setMaxWidth(auditoryTextField.getPrefWidth());
        TextFields.bindAutoCompletion(teachersTextField, FXCollections.observableArrayList(teachersList))
                .setMaxWidth(teachersTextField.getPrefWidth());
        TextFields.bindAutoCompletion(lessonTextField, FXCollections.observableArrayList(lessonList))
                .setMaxWidth(lessonTextField.getPrefWidth());
        validationSupport.registerValidator(auditoryTextField, Validator.createEqualsValidator("Оберіть аудиторію із списку",auditoryList));
        validationSupport.registerValidator(teachersTextField, Validator.createEqualsValidator("Оберіть аудиторію із списку",teachersList));
        validationSupport.registerValidator(lessonTextField, Validator.createEqualsValidator("Оберіть аудиторію із списку",lessonList));
        validationSupport.registerValidator(auditoryTextField, Validator.createEqualsValidator("Оберіть аудиторію із списку",auditoryList));
        /**
         * Part of code which creates a QR tables and realise view part
         * Generate QR's using zxing
         **/

        qrTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        if (qrTable.getColumns().size()==0) {
            qrList = FXCollections.observableArrayList(calendarIdMap.keySet()
                    .stream().map(i -> new QR(i)).collect(Collectors.toList()));
            TableColumn<QR, String> id = new TableColumn<>("ID");
            id.setStyle("-fx-alignment: CENTER;");
            id.setCellValueFactory(i -> i.getValue().getName());
            TableColumn<QR, ImageView> image = new TableColumn<>("QR");
            image.setStyle("-fx-alignment: CENTER;");
            image.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getCode()));
            qrTable.getColumns().addAll(id, image);
            qrTable.setItems(qrList);
        } else {
            qrTable.getColumns().clear();
            qrList = FXCollections.observableArrayList(calendarIdMap.keySet()
                    .stream().map(i -> new QR(i)).collect(Collectors.toList()));
            TableColumn<QR, String> id = new TableColumn<>("ID");
            id.setStyle("-fx-alignment: CENTER;");
            qrList.stream().forEach(i -> System.out.println(i.idCalendarProperty()));
            id.setCellValueFactory(i -> i.getValue().getName());
            TableColumn<QR, ImageView> image = new TableColumn<>("QR");
            image.setStyle("-fx-alignment: CENTER;");
            image.setCellValueFactory(i -> new SimpleObjectProperty<>(i.getValue().getCode()));
            qrTable.getColumns().addAll(id, image);
        }



        /**------------------------------------------------------------------------------------------------------------**/
        Map<Tab, Node> tabContent = new java.util.HashMap<>();
        for (Tab tab : tabPane.getTabs()) {
            tabContent.put(tab, tab.getContent());
        }
        // Initial state:
        // State change manager:
        tabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldTab, newTab) -> {
                            oldTab.setContent(null);
                            Node oldContent = tabContent.get(oldTab);
                            Node newContent = tabContent.get(newTab);

                            newTab.setContent(oldContent);
                            FadeTransition fadingOut = new FadeTransition(Duration.seconds(0.25), oldContent);
                            fadingOut.setFromValue(1);
                            fadingOut.setToValue(0);

                            FadeTransition fadeIn = new FadeTransition(
                                    Duration.seconds(0.25), newContent);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);

                            fadingOut.setOnFinished(event -> {
                                newTab.setContent(newContent);
                            });
                            SequentialTransition crossFade = new SequentialTransition(
                                    fadingOut, fadeIn);
                            crossFade.play();
                        });

        ObservableList<Screen> screens = Screen.getScreens();


       if (screens.size()>=2) {
            if (stage.isShowing()) {
                stage.close();
            }
            stage.setScene(scene);
            stage.setFullScreen(true);
           stage.setX(screens.get(1).getBounds().getMinX() + 100);
           stage.setY(screens.get(1).getBounds().getMinY() + 100);
            stage.setTitle("view");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Method which create calendar with entered summary;
     *
     * @param name - name of the summary of created calendar entry;
     * @throws Exception;
     **/

    public static void createGoogleCalendar(String name) throws Exception {
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName("applicationName").build();

        // AclRule aclRule = service.acl().get(name, "ruleId").execute();

        // Create a new calendar
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(name);
        calendar.setTimeZone("Europe/Kiev");

// Insert the new calendar

        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
//Update the list of entries;
        getCalendarListEntries();
        // service.acl().update(name, aclRule.getId(), aclRule).execute();
        //  getCalendarListEntries();

    }

//    /**
//     * Method's which creates calendars with entered summary;
//     *
//     * @throws Exception;
//     **/
//    @FXML
//    void createAuditoryCalendar(ActionEvent event) throws Exception {
//        if (!(auditoryList.contains(auditoryTextField.getText()))) {
//            createGoogleCalendar(auditoryTextField.getText());
//            auditoryList.add(auditoryTextField.getText());
//        } else {
//
//        }
//
//    }
//
//    @FXML
//    void createGroupCalendar(ActionEvent event) throws Exception {
//        if (!(groupChoiceBox.getItems().contains(groupTextField.getText()))) {
//            createGoogleCalendar(groupTextField.getText());
//            groupChoiceBox.getItems().add(groupTextField.getText());
//        } else {
//
//        }
//    }
//
//    @FXML
//    void createLessonCalendar(ActionEvent event) throws Exception {
//        if (!(lessonList.contains(lessonTextField.getText()))) {
//            createGoogleCalendar(lessonTextField.getText());
//            lessonList.add(lessonTextField.getText());
//        } else {
//
//        }
//
//    }
//
//    @FXML
//    void createTeacherCalendar(ActionEvent event) throws Exception {
//        if (!(teachersList.contains(teachersTextField.getText()))) {
//            createGoogleCalendar(teachersTextField.getText());
//            teachersList.add(teachersTextField.getText());
//        } else {
//
//        }
//    }

    /**
     * Create a toggleGroup for the radioButton's, set the name of selected type to the {@link String lessonType}
     */
    @FXML
    void getLessonType(ActionEvent event) {
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButton1.setToggleGroup(toggleGroup);
        radioButton2.setToggleGroup(toggleGroup);
        radioButton3.setToggleGroup(toggleGroup);
        radioButton4.setToggleGroup(toggleGroup);
        radioButton5.setToggleGroup(toggleGroup);
        radioButton6.setToggleGroup(toggleGroup);

        // listen changes in the toggle group and create new String with selected lesson;
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (toggleGroup.getSelectedToggle() != null) {
                    RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();
                    lessonType = new String(rb.getText());
                }

            }
        });
    }

    /**
     * @param event - event with special summary
     *              In this method event sends to the all calendars, which laborant selected in the our selection window;
     *              {execute() the event to the account}
     */
    public void sendToCalendars(Event event) throws Exception {



//        for (String i : cal){
//            if (!getCalendarListEntries().stream().anyMatch(j -> j.getSummary() ==i)){
//                createGoogleCalendar(i);
//            }
//        }
        String[] cal = event.getSummary().split(" ");
        DateTime now = new DateTime(System.currentTimeMillis());

        Events events = service.events().list(calendarIdMap.get(cal[0]))
                .setTimeMin(event.getStart().getDate())
                .execute();

        List<Event> items = events.getItems();
        if (items.size() == 0) {

            pushingToCalendars(event);

        } else {
            System.out.println("here hi");
            boolean flag = true;
            for (Event s : items) {
                if ((s.getStart().getDateTime().toStringRfc3339() == event.getStart().getDateTime().toStringRfc3339()) && (s.getSummary().contentEquals(event.getSummary()))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Дані пари вже існують!");
                    alert.setHeaderText("Перевірте правильність вводу");
                    alert.setContentText("Будьте обережні наступного разу");
                    alert.showAndWait();
                    flag = false;
                }
            }

            if (flag) {
                pushingToCalendars(event);
            }
            initialize();
        }
    }


    /**
     * Split the summary of calendar before executting to the Calendar;
     * @param event - event which were push;
     *        @return @{@link Event} object
     * */
    public Event pushingToCalendars(Event event) throws IOException {


        event = service.events().insert("vitikaf22@gmail.com", event).execute();
        String[] cal = event.getSummary().split(" ");
        for (String s : cal) {
            System.out.println(s);
            String calendarId = new String(calendarIdMap.get(s));
            event = service.events().insert(calendarId, event).execute();
        }
        return event;
    }

    /**
     * Create event with selected params, groups, teachers, using the data from TextFields
     */
    @FXML
    void executeEvent(ActionEvent e) throws Exception {
        if (!sampoCheckBox.isSelected()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        String groups = String.join(" ", groupChoiceBox.getCheckModel().getCheckedItems());


                        Event event = new Event()
                                .setSummary(groups + " " + auditoryTextField.getText() + " " + teachersTextField.getText() + " " + lessonTextField.getText())
                                .setDescription(numberOfLessonTextField.getText() + "  " + lessonType);
                        event = chooseLesson(pairPicker.getValue(), event, datePicker.getValue());

                        EventReminder[] reminderOverrides = new EventReminder[]{
                                new EventReminder().setMethod("email").setMinutes(24 * 60),
                                new EventReminder().setMethod("popup").setMinutes(15),
                        };
                        Event.Reminders reminders = new Event.Reminders()
                                .setUseDefault(false)
                                .setOverrides(Arrays.asList(reminderOverrides));
                        event.setReminders(reminders);
                        sendToCalendars(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        String groups = String.join(" ", groupChoiceBox.getCheckModel().getCheckedItems());
                        Event event = new Event()
                                .setSummary(groups + " " + auditoryTextField.getText() + "Сампо");
                        event = chooseLesson("Сампо", event, datePicker.getValue());

                        EventReminder[] reminderOverrides = new EventReminder[]{
                                new EventReminder().setMethod("popup").setMinutes(15)
                        };

                        Event.Reminders reminders = new Event.Reminders()
                                .setUseDefault(false)
                                .setOverrides(Arrays.asList(reminderOverrides));
                        event.setReminders(reminders);
                        sendToCalendars(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        });
        }

    }
}

