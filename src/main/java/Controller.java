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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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


    /**
     * reading list of calendars from gAcount creating a list with names of calendars
     * choice from that calendars change the calendars where events was use
     *
     * @execute() method
     */
    private static Map<String, String> calendarIdMap;

    public static List<CalendarListEntry> getCalendarListEntries() throws Exception {
        List<CalendarListEntry> listEntries;
        String pageToken = null;
        do {
            CalendarList calendarList = getCalendarService().calendarList().list().setPageToken(pageToken).execute();
            listEntries = calendarList.getItems();
            calendarIdMap = listEntries.stream().collect(Collectors.toMap(i -> i.getSummary(), i -> i.getId()));
            pageToken = calendarList.getNextPageToken();
        }
        while (pageToken != null);
        return listEntries;
    }

    public static Event chooseLesson (int i, Event event, LocalDate date) throws Exception{
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTime startDateTime;
        DateTime endDateTime;
        switch (i){
            case 1: {
                startDateTime = new DateTime(date.format(formatterDay)+"T09:00:00+02:00");
                endDateTime = new DateTime(date.format(formatterDay)+"T10:35:00+02:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev")) ; return event;
            }
            case 2: {
                startDateTime = new DateTime(date.format(formatterDay)+"T10:50:00+02:00");
                endDateTime = new DateTime(date.format(formatterDay)+"T12:25:00+02:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev")) ; return event;
            }
            case 3: {
                startDateTime = new DateTime(date.format(formatterDay)+"T12:40:00+02:00");
                endDateTime = new DateTime(date.format(formatterDay)+"T14:15:00+02:00");
                event.setStart(new EventDateTime().setDateTime(startDateTime).setTimeZone("Europe/Kiev"));
                event.setEnd(new EventDateTime().setDateTime(endDateTime).setTimeZone("Europe/Kiev")) ; return event;
            }
            default: {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                chooseLesson(Integer.parseInt(reader.readLine()),event, date);
            }

        }
        return null;
    }

    private ObservableList<String> calendarListStrings;

    @FXML
    private ChoiceBox<String> groupChoiceBox;

    @FXML
    private TextField groupTextField;

    @FXML
    private Button groupButton;

    @FXML
    private ComboBox<String> teachersComboBox;

    @FXML
    private TextField teachersTextField;

    @FXML
    private Button teacherButton;

    @FXML
    private ComboBox<String> auditoryComboBox;

    @FXML
    private TextField auditoryTextField;

    @FXML
    private Button AuditoryButton;

    @FXML
    private ComboBox<String> lessonsComboBox;

    @FXML
    private TextField lessonTextField;

    @FXML
    private Button lessonButton;

    @FXML
    private TextField numberOfLessonTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> pairPicker;

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
    private WebView googleCalendarWeb;

    private ToggleGroup toggleGroup;
    private String lessonType;
    @FXML
    void initialize() throws Exception {


        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
                getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }

        /** Creating a Web Page with kafedra calendar
         */
        WebEngine engine = googleCalendarWeb.getEngine();
        engine.load("https://calendar.google.com/calendar/embed?src=vitikaf22%40gmail.com&ctz=Europe%2FKiev");


        List<CalendarListEntry> item = getCalendarListEntries();
        calendarListStrings = FXCollections.observableArrayList(item.stream().map(i -> i.getSummary()).collect(Collectors.toList()));
        auditoryComboBox.setItems(FXCollections.observableArrayList(calendarListStrings.stream().filter(i -> i.startsWith("ауд.2")).collect(Collectors.toList())));
        groupChoiceBox.setItems(FXCollections.observableArrayList(calendarListStrings.stream().filter(i -> i.startsWith("2")).collect(Collectors.toList())));
        teachersComboBox.setItems(calendarListStrings);
        lessonsComboBox.setItems(calendarListStrings);
        TextFields.bindAutoCompletion(auditoryTextField, calendarListStrings);
        calendarIdMap.values().stream().forEach(System.out::print);

    }

    public static void createGoogleCalendar(String name) throws Exception {
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName("applicationName").build();

        // Create a new calendar
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(name);
        calendar.setTimeZone("Europe/Kiev");

// Insert the new calendar
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
//Update the list of entries;
        getCalendarListEntries();


    }
    @FXML
    void createAuditoryCalendar(ActionEvent event) throws Exception {
        if (!(auditoryComboBox.getItems().contains(auditoryTextField.getText()))) {
            createGoogleCalendar(auditoryTextField.getText());
            auditoryComboBox.getItems().add(auditoryTextField.getText());
        } else {

        }

    }

    @FXML
    void createGroupCalendar(ActionEvent event) throws Exception {
        if (!(groupChoiceBox.getItems().contains(groupTextField.getText()))) {
            createGoogleCalendar(groupTextField.getText());
            groupChoiceBox.getItems().add(groupTextField.getText());
        } else {

        }
    }

    @FXML
    void createLessonCalendar(ActionEvent event) throws Exception {
        if (!(lessonsComboBox.getItems().contains(lessonTextField.getText()))) {
            createGoogleCalendar(lessonTextField.getText());
            lessonsComboBox.getItems().add(lessonTextField.getText());
        } else {

        }

    }

    @FXML
    void createTeacherCalendar(ActionEvent event) throws Exception {
        if (!(teachersComboBox.getItems().contains(teachersTextField.getText()))) {
            createGoogleCalendar(teachersTextField.getText());
            teachersComboBox.getItems().add(teachersTextField.getText());
        } else {
            teachersTextField.setText(teachersComboBox.getValue());
        }
    }


    @FXML
    void getLessonType(ActionEvent event) {

    }

    public void sendToCalendars (Event event)throws Exception {

       String[] cal =  event.getSummary().split(" ");

        for (String s: cal) {
            String calendarId = new String(calendarIdMap.get(s));
            event = getCalendarService().events().insert(calendarId, event).execute();
            System.out.println(event.getHtmlLink());
        }


    }
    @FXML
    void executeEvent(ActionEvent e) throws Exception{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
try {

    Event event = new Event()
            .setSummary(groupTextField.getText() + " " + auditoryTextField.getText() + " " + teachersTextField.getText() + " " + lessonTextField.getText() + " " + numberOfLessonTextField.getText() + " " + lessonType);
    event = chooseLesson(1,event, datePicker.getValue());

    EventReminder[] reminderOverrides = new EventReminder[]{
            new EventReminder().setMethod("email").setMinutes(24 * 60),
            new EventReminder().setMethod("popup").setMinutes(10),
    };
    Event.Reminders reminders = new Event.Reminders()
            .setUseDefault(false)
            .setOverrides(Arrays.asList(reminderOverrides));
    event.setReminders(reminders);

    sendToCalendars(event);
}catch (Exception e){
    e.printStackTrace();
}
}
    });
    }
    }

