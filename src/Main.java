import javax.swing.*;
import java.awt.*;

public class Main extends JFrame{

    private final int width = 1080;
    private final int height = 640;

    public static void main(String[] args) {
        new Main();
    }

    // Создание Jframe в конструкторе Main
    public Main(){
        super("Морской бой");
        // Установка размеров окна
        setSize(width,height);
        // Завершение программы при закрытии окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Установка окна по центру
        setLocationRelativeTo(null);
        // Установка возможности изменения размеров окна
        setResizable(false);
        // Добавление на фрейм панели меню с расположением в центре
        add(new Menu(this),BorderLayout.CENTER);
        // Установка видимости окна
        setVisible(true);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
