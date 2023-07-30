import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LeaderBoard extends JPanel implements ActionListener {

    private JTable table;
    private JLabel label;
    private JFrame frame;
    private JButton button;
    private DefaultTableModel model;
    private TableColumnModel tableColumnModel;
    private ArrayList<User> users;
    private final int heightRow = 30;
    private Object[][] testArray;
    private final Object[] columnsHeader = new String[]{"№", "Пользователь", "Очки", "Время"};

    // В конструкторе происходит создание надписи, кнопки и таблицы, а также чтение, сортировка и добавление данных в таблицу.
    public LeaderBoard(JFrame frame) {
        this.frame = frame;

        // Работа с файлом
        users = new ArrayList<>();
        readData();
        sorting();
        addInformationOnTable();

        // Работа с надписью "Таблица лидеров"
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(920, 520));

        label = new JLabel("Таблица лидеров", null, SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(920, 60));
        label.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 26));
        label.setOpaque(true);
        label.setBackground(new Color(61, 245, 181));
        label.setBorder(BorderFactory.createBevelBorder(1, Color.black, Color.gray));
        panel.add(label, BorderLayout.NORTH);


        // Работа с таблицей
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnsHeader);

        for (Object[] objects : testArray) {
            model.addRow(objects);
        }

        table = new JTable(model);
        // Изменение шрифта "шапки" таблицы
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 16));

        // Выравнивание строк в ячейках по центру
        JTableUtilities.setCellsAlignment(table, SwingConstants.CENTER);

        tableColumnModel = table.getColumnModel();

        // Изменение возможности изменять размер каждой колонки
        for (int i = 0; i < columnsHeader.length; i++) {
            tableColumnModel.getColumn(i).setResizable(false);
        }
        // Возможность выделять колонки
        tableColumnModel.setColumnSelectionAllowed(false);

        tableColumnModel.getColumn(0).setMaxWidth(40);

        table.setPreferredSize(new Dimension(920, users.size() * heightRow));
        table.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 18));
        table.setRowHeight(heightRow);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);

        table.setDefaultEditor(Object.class, null);

        // Добавлние таблицы в ползунок
        JScrollPane pane = new JScrollPane(table);
        panel.add(pane, BorderLayout.CENTER);
        add(panel);

        // Добавление кнопки выхода
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        button = new JButton("Выход");
        button.setPreferredSize(new Dimension(120, 30));
        button.setBackground(Color.cyan);
        button.setFocusable(false);
        button.addActionListener(this);
        jPanel.add(button, BorderLayout.EAST);
        panel.add(jPanel, BorderLayout.PAGE_END);

    }

    // Слушатель событий для кнопки выхода
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.remove(this);
        frame.repaint();
        frame.revalidate();

        frame.add(new Menu(frame));
        frame.repaint();
        frame.revalidate();

    }

    /**
     * Метод readData читает файл и добавляет в коллекцию новых пользователей.
     */
    private void readData() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("\\temp\\leaderboard.txt"))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] parts = s.split(" ");
                users.add(new User(parts[0], parts[2], Integer.parseInt(parts[1])));
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Метод sorting отвечает за сортировку объектов по очкам.
     */
    private void sorting(){
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore() - o1.getScore();
            }
        });
    }

    private void addInformationOnTable() {
        testArray = new Object[users.size()][4];

        for (int i = 0; i < testArray.length; i++) {
            testArray[i][0] = i + 1;
            testArray[i][1] = users.get(i).getName();
            testArray[i][2] = users.get(i).getScore();
            testArray[i][3] = users.get(i).getTime();
        }

    }


    // В конструторе было описано
    private static class JTableUtilities {
        public static void setCellsAlignment(JTable table, int alignment) {
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(alignment);


            TableModel tableModel = table.getModel();

            for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
                table.getColumnModel().getColumn(columnIndex).setCellRenderer(render);
            }
        }
    }

}
