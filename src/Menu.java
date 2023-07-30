import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Menu extends JPanel implements ActionListener {

    private final JButton play;
    private final JButton leaderboards;
    private final JButton exit;
    private final String[] difficult = {"Легкая", "Нормальная", "Сложная"};
    private final JFrame frame;
    private LeaderBoard leaderBoard;

    // Изначальные файлы для записи
    private final Object[][] data = {
            {"Никита", 109200, "2:33"},
            {"Артём", 49500, "1:20"},
            {"Михаил", 16800, "5:30"},
            {"Влад", 7800, "3:12"},
            {"Дмитрий", 66000, "1:44"},
            {"Арсений", 85800, "1:40"},
            {"Богдан", 109200, "2:40"},
            {"Алексей", 244800, "3:43"},
            {"Илья", 399000, "4:20"},
            {"Евгений", 111200, "2:11"},
            {"Михаил", 136500, "0:54"},
            {"Владимир", 49500, "2:33"},
            {"Александр", 36000, "1:47"},
            {"Глеб", 10500, "1:07"},
            {"Вадим", 16800, "0:58"},
            {"Сергей", 6000, "1:02"},
            {"Вы", 0, "0:00"},

    };


    public Menu(JFrame frame) {
        writeText();
        this.frame = frame;
        // Установка слоя с центральным расположением с вертикальными отступами 90 пикселей
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 90));
        // Создание панели со слоем "таблицы": 3 строки, 1 столбец, вертикальные отступы 30 пикселей
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 30));
        // Установка предпочитаемог размера панели
        panel.setPreferredSize(new Dimension(460, 360));

        Dimension dimension = new Dimension(120, 90);
        play = addButton("Играть", panel, dimension);
        leaderboards = addButton("Таблица лидеров", panel, dimension);
        exit = addButton("Выход", panel, dimension);

        leaderBoard = new LeaderBoard(frame);

        add(panel);
        setVisible(true);
    }

    /**
     * Метод addButton создаёт и добавляет кнопки в контейнер.
     * @param name текст.
     * @param container место, где кнопки будут располагаться.
     * @param dimension размеры кнопок.
     * @return кнопка.
     */
    private JButton addButton(String name, Container container, Dimension dimension) {
        JButton button = new JButton(name);
        button.setPreferredSize(dimension);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 24));
        button.setBackground(Color.cyan);
        container.add(button);
        button.addActionListener(this);
        return button;
    }

    /**
     * Метод writeText записывает данные в текстовый файл.
     * Если текстовый файл уже создан, то запись производиться не будет.
     */
    private void writeText() {
        try {
            File file = new File("\\temp\\leaderboard.txt");
            if (file.createNewFile()) {
                PrintWriter printWriter = new PrintWriter(file);
                for (Object[] objects : this.data) {
                    printWriter.print(objects[0]);
                    printWriter.print(" ");
                    printWriter.print(objects[1]);
                    printWriter.print(" ");
                    printWriter.println(objects[2]);
                }
                printWriter.close();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    // Слушатель событий для кнопок
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if (btn.equals(play)) {
            // Всплывающее окно с выбором сложности, по умолчанию стоит "лёгкая"
            int difficult = JOptionPane.showOptionDialog(
                    null,
                    "Выберете сложность:",
                    "Морской бой",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    this.difficult,
                    this.difficult[0]
            );
            // Данные строки служат для осуществления перехода на другую панель (в данном случае - игровая панель)
            frame.remove(this);
            frame.repaint();
            frame.revalidate();

            frame.add(new Game_Panel(difficult, frame));
            frame.repaint();
            frame.revalidate();

        } else if (btn.equals(leaderboards)) {
            // Переход в таблицу лидеров
            frame.remove(this);
            frame.repaint();;
            frame.add(leaderBoard);
            frame.revalidate();

        } else if (btn.equals(exit)) {
            System.exit(0);
        }
    }

}
