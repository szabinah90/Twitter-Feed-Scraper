package users;

public class User {
    private String id;
    private String name;
    private String screen_name;
    private String location;
    private String url;
    private String description;

    public User() {
    }

    public User(String id, String name, String screen_name, String location, String url, String description) {
        this.id = id;
        this.name = name;
        this.screen_name = screen_name;
        this.location = location;
        this.url = url;
        this.description = description;
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

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "\"user\": {" +
                ", \"id\": " + id +
                ", \"name\": " + name +
                ", \"screen_name\": " + screen_name +
                ", \"location\": " + location +
                ", \"url\":" + url +
                ", \"description\": " + description +
                '}';
    }
}
