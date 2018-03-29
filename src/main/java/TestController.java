import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class TestController {

    @FXML
    private FlowPane firstLineHbox;


    @FXML
    void initialize() throws Exception {

        ObservableList<CalendarListEntry> calendars = FXCollections.observableArrayList(Controller
                .getCalendarListEntries()
                .stream()
                .filter(i -> i.getSummary().startsWith("ауд.2"))
                .collect(Collectors.toList()));

        for (CalendarListEntry i:
            calendars ) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDate.now().atStartOfDay();

            try {

                Events events = Controller.getCalendarService().events().list(i.getId()).setTimeMin(
                        new DateTime(now.format(dtf)+ "T08:00:00+02:00")
                ).setTimeMax(new DateTime(now.format(dtf)+ "T21:00:00+02:00")
                ).execute();
                ObservableList<Event> listEntries = FXCollections.observableArrayList(events.getItems());
                if (listEntries.size()!=0) {
                    TableView<Event> tableView = new TableView(listEntries);
                    tableView.resize(400, 300);

                    TableColumn<Event, String> columnSummary = new TableColumn(i.getSummary());
                    columnSummary.setMinWidth(300);
                    TableColumn<Event, String> eventStringTableColumn = new TableColumn("Початок");
                    eventStringTableColumn.setMinWidth(200);
                    columnSummary.setCellValueFactory(j -> {
                        StringBuffer s = new StringBuffer(j.getValue().getSummary());
                        s.replace(s.indexOf("ауд."),s.indexOf("ауд.")+8, " ");
                        return  new SimpleStringProperty(s.toString());
                    });
                    eventStringTableColumn.setCellValueFactory(k -> {
                        String timeString = k.getValue().getStart().getDateTime().toStringRfc3339();
                        return new SimpleStringProperty(timeString.split("T")[1].substring(0, 8)
                        );

                    });
                    eventStringTableColumn.setSortType(TableColumn.SortType.ASCENDING);
                    tableView.getColumns().add(columnSummary);
                    tableView.getColumns().add(eventStringTableColumn);
                    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    firstLineHbox.getChildren().add(tableView);
                } else {
                   break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}