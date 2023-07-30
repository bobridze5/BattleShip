public class User {

    private final String name;
    private String time;
    private int score;

    public User(String name, String time, int score){
        this.name = name;
        this.time = time;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(int time){
        this.time = String.valueOf(time / 60);
    }

    public void setScore(int score){
        this.score = score;
    }
}
