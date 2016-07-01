package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PATIENT".
 */
public class Patient implements java.io.Serializable {

    private String id;
    private String name;
    private String portraitUri;

    public Patient() {
    }

    public Patient(String id) {
        this.id = id;
    }

    public Patient(String id, String name, String portraitUri) {
        this.id = id;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

}
