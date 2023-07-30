import java.awt.*;

public class Marker extends Rectangle {

    private final Color hit = Color.red;
    private final Color miss = Color.white;
    private final Color color = Color.gray;
    private final Color ship = Color.GREEN;
    private boolean atShip;
    private boolean clicked;
    private boolean isMissed;
    private boolean isWasShip;
    private boolean isVisible;

    public Marker(Position position, int width, int height) {
        super(position, width, height);
    }

    public void paint(Graphics g) {
        g.setColor(getColorComponent());
        g.fillRect(position.x + 1, position.y + 1, width - 2, height - 2);
    }

    /**
     * Метод getColorComponent устанавливает цвет "пера" в зависимости от состояния ячейки
     * @return Color
     */
    private Color getColorComponent(){
        if(clicked){
            if(isMissed) return miss;
            else if(isWasShip) return hit;
        } else if(isVisible){
            return ship;
        }
        return color;
    }


    public void setVisible(boolean visible){
       this.isVisible = visible;
    }

    /**
     * Метод shot проверяет на возможность нажатия на ячейку и меняет состояния ячейки в зависимости от попадания.
     * @param g
     */
    public void shot(Graphics g) {
        if (!clicked) {
            if (atShip) {
                g.setColor(hit);
                setAtShip(false);
                setIsWasShip(true);
            }
            else {
                g.setColor(miss);
                isMissed = true;
            }
            g.fillRect(position.x + 1, position.y + 1, width - 2, height - 2);
        }
    }


    public void setAtShip(boolean atShip) {
        this.atShip = atShip;
    }

    public boolean isShip() {
        return atShip;
    }

    public boolean isClicked(){
        return clicked;
    }

    public void setClicked(boolean param) {
        clicked = param;
    }
    public boolean isMissed(){
        return isMissed;
    }

    public void setIsWasShip(boolean param){
        isWasShip = param;
    }

    public boolean getIsWasShip(){
        return isWasShip;
    }

}
