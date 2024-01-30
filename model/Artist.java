package sample.model;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Artist {

    private SimpleIntegerProperty id; // changed from int and string to these classes. something to do with data binding?
    private SimpleStringProperty name;
    // Property is a value that represents the state of an object and can be retrieved and/or set
    // a Property, unlike a POJO, is observable. can call methods on the object like adding a Listener to inform you
    // whenever the value changes. Can also use a bind method.


    public Artist() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}

// mapping name field in artist class to name field in table to complete data binding?
