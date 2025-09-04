package ca.coltip.data.dto;

public class CourseDTO {
    private int id;
    private String title;
    private double price;
    private String photo;
    private String description;
    private CourseTypeDTO courseType;
    private LanguageDTO language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseTypeDTO getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseTypeDTO courseType) {
        this.courseType = courseType;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO languageCode) {
        this.language = languageCode;
    }
}
