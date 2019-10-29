package sample;

public class Note {

    private String id;  // Поле id документа
    private String noteText;    // Поле с записью в документе

    public Note(String noteText) {
        this.noteText = noteText;
    }

    public Note() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id +'\'' +
                ", noteText='" + noteText + '\'' +
                '}';
    }
}
