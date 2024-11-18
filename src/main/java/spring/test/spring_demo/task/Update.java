package spring.test.spring_demo.task;

public class Update {
    private String color;
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String title;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Update(String color, String title, String description) {
        this.color = color;
        this.title = title;
        this.description = description;
        
    }
    
}
