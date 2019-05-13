package ar.edu.itba.paw.model;

public class UserRole {

    private final long id;
    private String role;
    private int level;

    public UserRole(long id, String role, int level) {
        this.id = id;
        this.role = role;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
