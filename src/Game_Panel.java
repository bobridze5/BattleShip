import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Game_Panel extends JPanel implements ActionListener, MouseListener {

    private enum STATE {Placing, Gaming, GameOver}
    public static final int width = 1080;
    public static final int height = 640;
    private STATE state = STATE.Placing;
    private final JButton exit;
    private final Select player;
    private final Select computer;
    private final String win = "Вы победили!";
    private final String lose = "Вы проиграли.";
    private int score = 0;
    private JFrame frame;
    private int max_score = -120;
    private double rate = 1d;
    private int difficult;
    private long timer;
    private long startTime;
    private boolean isTime = true;
    private final int delay = 1000;

    public Game_Panel(int difficult, JFrame frame) {
        this.frame = frame;
        this.difficult = difficult;

        setSize(width, height);

        setLayout(new BorderLayout());

        // Создание кнопки выхода
        exit = new JButton("X");
        exit.setBackground(Color.cyan);
        exit.setFont(new Font("Arial", Font.BOLD, 36));
        exit.setFocusable(false);
        exit.addActionListener(this);

        // Слои под кнопку выхода
        JPanel panel = new JPanel(new GridLayout(1, 1, 5, 0));
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(exit);
        flow.add(panel);
        add(flow);

        player = new Select(new Position(80, 100), 0, 0);
        computer = new Select(new Position(player.position.x + 500, 100), 0, 0);

        player.marked();
        computer.marked();

        player.makeShips();
        computer.makeShips();

        computer.setComputer(true);
        player.setComputer(false);

        player.paintShips(true);
        computer.paintShips(false);

        state = STATE.Gaming;
        setVisible(true);
        // Добавление слушателя событий для мышки
        addMouseListener(this);

    }

    /**
     * Метод paint отрисовывает окно и элементы игры
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        player.paint(g);
        computer.paint(g);

    }

    /**
     * Метод actionPerformed описывает, что происходит при нажатии на кнопку.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите выйти?\n Ваш результат будет обнулён.",
                "Вопрос",
                JOptionPane.YES_NO_OPTION
        );

        // Переход в меню
        if (result == JOptionPane.YES_OPTION) {
            frame.remove(this);
            frame.repaint();
            frame.revalidate();

            frame.add(new Menu(frame));
            frame.repaint();
            frame.revalidate();
        }

    }

    /**
     * Метод CompTurn описывает ходы компьютера.
     *
     * @param g
     */
    private void CompTurn(Graphics g) {
        rate = 1d;
        computer.DrawCell(g);
        while (computer.isHit()) {
            // Таймер с запланированной задачей перерисовывать все элементы с задержкой delay
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            }, delay);
            computer.DrawCell(g);
        }
    }



    /**
     * Метод mouseClicked обрабатывает нажатия кнопок мыши на окно.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Получение x,y координат при нажатии на окно.
        player.getIndexOnGrid(new Position(e.getPoint().x, e.getPoint().y));

        // Таймер
        if (isTime) {
            startTime = System.currentTimeMillis();
            isTime = false;
        }

        // Описание ходов игрока/компьютера
        if (state.equals(STATE.Gaming)) {

            if (!player.isClicked()) {

                player.DrawCell(getGraphics());

                if (player.isHit()) {
                    score += (300 * rate);
                    rate += 0.5d;
                } else CompTurn(getGraphics());
                // Таймер с запланированной задачей перерисовывать все элементы с задержкой delay
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        repaint();
                    }
                }, delay);
            }

            // Конец игры
            if (player.isGameOver()) {
                state = STATE.GameOver;

                timer = (System.currentTimeMillis() - startTime) / 1000;
                String timeInString;
                if (timer % 60 < 10) {
                    timeInString = timer / 60 + ":0" + timer % 60;
                } else timeInString = timer / 60 + ":" + timer % 60;
                JOptionPane.showMessageDialog(
                        this,
                        win + "\n"
                                + "Количество очков: " + score
                                + "\nВремя: "
                                + timeInString,
                        "Поздравляем!",
                        JOptionPane.INFORMATION_MESSAGE
                );

                reWriteText(new User("Вы", timeInString, score));

            } else if (computer.isGameOver()) {
                state = STATE.GameOver;

                JOptionPane.showMessageDialog(this, lose, "Увы!", JOptionPane.INFORMATION_MESSAGE);
            }

            // Переход в меню
            if (state.equals(STATE.GameOver)) {
                frame.remove(this);
                frame.repaint();
                frame.revalidate();

                frame.add(new Menu(frame));
                frame.repaint();
                frame.revalidate();
            }
        }

    }

    /**
     * Метод reWriteText перезаписывает файл leaderboard.txt.
     *
     * @param user данные пользователя (имя, кол-во очков, время).
     */
    private void reWriteText(User user) {
        File file = new File("\\temp\\leaderboard.txt");
        File temp = new File("\\temp\\temp.txt");
        String current;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

            while ((current = reader.readLine()) != null) {
                if (current.startsWith("Вы")) {
                    String[] arr = current.split(" ");
                    max_score = Integer.parseInt(arr[1]);
                    if (score >= max_score) {
                        writer.write(user.getName());
                        writer.write(" ");
                        writer.write(String.valueOf(user.getScore()));
                        writer.write(" ");
                        writer.write(user.getTime());
                        writer.newLine();
                    } else writer.write(current + System.getProperty("line.separator"));
                } else {
                    writer.write(current + System.getProperty("line.separator"));
                }
            }
            reader.close();
            writer.close();
            boolean g = file.delete();
            boolean f = temp.renameTo(file);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Не используется
    @Override
    public void mousePressed(MouseEvent e) {
    }

    // Не используется
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // Не используется
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    // Не используется
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
