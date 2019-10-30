package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Notebook {

    /* Класс работы GUI c DB */
    @FXML
    public TextArea notesArea;

    @FXML
    public TextField fieldSearch;

    @FXML
    public Label helpQuery;

    NotebookDatabase noteDB;
    List<Note> foundNotes;
    int numberNote;

    public Notebook() {
        notesArea = new TextArea();
        fieldSearch = new TextField();

        helpQuery = new Label();

        foundNotes = new ArrayList<Note>();
        numberNote = 0;

        Properties prop = new Properties();
        prop.setProperty("host", "localhost");
        prop.setProperty("port", "27017");
        prop.setProperty("dbname", "rddatabase");
        prop.setProperty("login", "root");
        prop.setProperty("password", "root");
        prop.setProperty("collection", "notes");

        noteDB = new NotebookDatabase(prop);
    }

    // Добавление заметки в БД
    public void addNote(ActionEvent actionEvent) {
        noteDB.add(new Note(notesArea.getText()));
        notesArea.setText("");
        helpQuery.setText("Ваша заметка добавлена успешно!");
    }

    // Поиск заметки в БД
    public void searchNote(ActionEvent actionEvent) {
        try {
            String query = fieldSearch.getText();
            foundNotes = noteDB.getListByNotes(query);
            notesArea.setText(foundNotes.get(numberNote).getNoteText());
            fieldSearch.setText("");
            helpQuery.setText("Найдено похожих заметок: " + foundNotes.size());
        } catch (Exception e) {
            helpQuery.setText("Заметка не найдена");
        }
    }

    // Кнопка назад для просмотра найденных заметок
    public void backSearch(ActionEvent actionEvent) {
        try {
            if (numberNote != 0)
            {
                numberNote --;
                notesArea.setText(foundNotes.get(numberNote).getNoteText());
            }
        } catch (Exception e) {
            notesArea.setText("");
        }
    }

    // Кнопка вперед для просмотра найднных заметок
    public void forwardSearch(ActionEvent actionEvent) {
        try {
            if (numberNote != foundNotes.size() - 1)
            {
                numberNote ++;
                notesArea.setText(foundNotes.get(numberNote).getNoteText());
            }
        } catch (Exception e) {
            notesArea.setText("");
        }
    }

    // Кнопка выхода
    public void exit(ActionEvent actionEvent) {
        noteDB.getMongoClient().close();
        System.exit(0);
    }
}
