package sample;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NotebookDatabase {

    /* Класс БД */
    // Это клиент который обеспечит подключение к БД
    private MongoClient mongoClient;

    // Этот класс дает возможность аутентифицироваться в MongoDB
    private DB db;

    // Тут мы будем хранить состояние подключения к БД
    private boolean authenticate;

    // Класс, который обеспечит возможность работать с коллекциями MongoDB
    private DBCollection collection;

    public NotebookDatabase(Properties prop) {

        try {
            // Создаем подключение
            mongoClient = new MongoClient(prop.getProperty("host"), Integer.valueOf(prop.getProperty("port")));

            // Выбираем БД для дальнейшей работы
            db = mongoClient.getDB(prop.getProperty("dbname"));

            // Входим под созданным логином и паролем
            authenticate = db.authenticate(prop.getProperty("login"), prop.getProperty("password").toCharArray());

            // Выбираем коллекцию/таблицу для дальнейшей работы
            collection = db.getCollection(prop.getProperty("collection"));

        } catch (UnknownHostException e) {
            // Если возникли проблемы при подключении сообщаем об этом
            System.err.println("Don't connect!");
        }
    }

    // Метод для закрытия клиента
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    // Добавление заметки
    public void add(Note note) {
        BasicDBObject document = new BasicDBObject();

        // указываем поле с объекта User
        // это поле будет записываться в коллекцию/таблицу
        document.put("noteText", note.getNoteText());

        // записываем данные в коллекцию/таблицу
        collection.insert(document);
    }

    // Получение заметки из коллекции
    public Note getByNotes(String noteText) {
        BasicDBObject query = new BasicDBObject();

        // задаем поле и значение поля по которому будем искать
        query.put("noteText", noteText);

        // осуществляем поиск
        DBObject result = collection.findOne(query);

        // Заполняем сущность полученными данными с коллекции
        Note note = new Note();
        note.setNoteText(String.valueOf(result.get("noteText")));
        note.setId(String.valueOf(result.get("_id")));

        // возвращаем полученную заметку
        return note;
    }

    // Получение списка заметок из коллекции
    public List<Note> getListByNotes(String noteText) {

//        BasicDBObject query = new BasicDBObject();

        // задаем поле и значение поля по которому будем искать совпадения
        BasicDBObject query = new BasicDBObject("noteText", new BasicDBObject("$regex", noteText));

        // задаем поле и значение поля по которому будем искать
//        query.put("noteText", noteText);

        // осуществляем поиск и записываем все найденные документы в курсор
        DBCursor cursor = collection.find(query);

        // получаем лист объектов
        List<DBObject> result = cursor.toArray();

        // Заполняем сущность полученными данными с коллекции
        List<Note> notes = new ArrayList<Note>();

        for (DBObject item : result) {
            Note iterationNote = new Note();    // промежуточная записка
            iterationNote.setNoteText(String.valueOf(item.get("noteText")));
            iterationNote.setId(String.valueOf(item.get("_id")));
            notes.add(iterationNote);
        }

        // возвращаем полученный лист заметок
        return notes;
    }
        /*
        // Обновление данных заметки
        // noteText - это старая заметка
        // newNoteText - это новая заметка, на которую мы хотим поменять
        public void updateByNoteText(String noteText, String newNoteText) {
            BasicDBObject newData = new BasicDBObject();

            // задаем новый логин
            newData.put("noteText", newNoteText);

            // указываем обновляемое поле и текущее его значение
            BasicDBObject searchQuery = new BasicDBObject().append("noteText", noteText);

            // обновляем запись
            table.update(searchQuery, newData);
        }
        */

        /*
        // Удаление заметки
        public void deleteByNoteText(String noteText){
            BasicDBObject query = new BasicDBObject();

            // указываем какую запись будем удалять с коллекции
            // задав поле и его текущее значение
            query.put("noteText", noteText);

            // удаляем запись с коллекции/таблицы
            table.remove(query);
        }
         */
}
