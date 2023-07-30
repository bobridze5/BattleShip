import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * Основной класс игры, в котором происходит отрисовка и маркировка поля, создание кораблей.
 */
public class Select extends Rectangle {
    public static final int Size = 40;
    public static final int Cells = 10;
    private static final int Grid = Size * Cells;
    private int MouseX, MouseY;
    private final Marker[][] markers = new Marker[Cells][Cells];
    private final int[] Pattern = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private final Ship[] ships = new Ship[Pattern.length];
    private Random random;
    private final char[] chars = {'А', 'Б', 'В', 'Г', 'Д', 'E', 'Ж', 'З', 'И', 'К'};
    private boolean isComputer;
    private final AI computer = new AI(0);
    private ArrayList<Marker> mark;

    public Select(Position position, int width, int height) {
        super(position, width, height);
    }

    /**
     * Метод paint отрисовывает все элементы игры.
     * @param g
     */
    public void paint(Graphics g) {
        DrawField(g);
        DrawMarker(g);
    }

    /**
     * Метод DrawField отрисовывает игровое поле.
     *
     * @param g
     */
    private void DrawField(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        if (!isComputer) g.drawString("Поле противника", position.x + Size * 2, position.y - 2 * Size + Size / 2);
        else g.drawString("Ваше поле", position.x + Size * 3, position.y - 2 * Size + Size / 2);
        for (int i = 0; i < Cells + 1; i++) {
            if (i < Cells) {
                g.drawString(String.valueOf(chars[i]), position.x + i * Size + Size / 2 - Size / 5, position.y - Size / 3);
                g.drawString(String.valueOf(i + 1), position.x - Size / 2 - Size / 3, position.y + Size * i + Size / 2 + Size / 4);
            }
            g.drawLine(position.x, position.y + i * Size, position.x + Grid, position.y + i * Size);
            g.drawLine(position.x + i * Size, position.y, position.x + i * Size, position.y + Grid);
        }
    }

    /**
     * Метод marked() создаёт клетки и присваивает значение isClicked = false.
     */
    public void marked() {
        for (int i = 0; i < Cells; i++) {
            for (int j = 0; j < Cells; j++) {
                markers[i][j] = new Marker(new Position(position.x + i * Size, position.y + j * Size), Size, Size);
                markers[i][j].setClicked(false);
            }
        }
    }

    /**
     * Метод DrawMarker отрисовывает клеточки в зависимости от периода игры,
     * то есть, впервый раз вызывается метод paintNotVisibleField(g), который отрисовывает все клетки "серым",
     * затем отрисовка происходит, если необходимо, всех клеток (попадания, промахи, серые клетки).
     *
     * @param g
     */
    private void DrawMarker(Graphics g) {
        for (int i = 0; i < Cells; i++) {
            for (int j = 0; j < Cells; j++) {
                markers[i][j].paint(g);
            }
        }
    }

    /**
     * Метод DrawCell отрисовывает выстрелы игрока / компьютера.
     * Компьютер получает случайные индексы и устанавливает их в переменные MouseX, MouseY
     * Затем происходит отрисовка с помощью метода shot
     *
     * @param g
     */
    public void DrawCell(Graphics g) {
        try {
            if (isComputer) {
                setIndexOnGrid(computer.makeRandomChoice());
                Thread.sleep(1000);
            }
            markers[MouseX][MouseY].shot(g);
            markers[MouseX][MouseY].setClicked(true);
            paintComment(g);
        } catch (Exception ignored) {

        }
    }

    /**
     * Метод isHit проверяет, было ли поподание по кораблю.
     *
     * @return true / false
     */
    public boolean isHit() {
        return markers[MouseX][MouseY].getIsWasShip();
    }

    /**
     * Метод isClicked проверяет было ли нажатие на данную клетку.
     *
     * @return true / false
     */
    public boolean isClicked() {
        return markers[MouseX][MouseY].isClicked();
    }

    /**
     * Метод getIndexOnGrid получает индексы на сетке и присваивает переменным MouseX / MouseY (x - координата / y - координата).
     *
     * @param other позиция взятая при нажатии на сетку.
     */
    public void getIndexOnGrid(Position other) {
        MouseX = (other.x - position.x) / Size;
        MouseY = (other.y - position.y) / Size;
    }

    /**
     * Метод setIndexOnGrid устанавливает координаты точки.
     * @param other
     */
    public void setIndexOnGrid(Position other) {
        MouseX = other.x;
        MouseY = other.y;
    }

    /**
     * Метод makeShips размещает случайным образом корабли на сетке.
     */
    public void makeShips() {
        random = new Random();
        boolean orientation;
        for (int i = 0; i < Pattern.length; i++) {
            // В цикле do while случайным образом определяются индексы на сетке, ориентация корабля, таким образом,
            // чтобы другие корабли не задевали друг друга или не располагасть друг на друге
            do {
                Position temp = new Position(random.nextInt(position.x, position.x + Grid),
                        random.nextInt(position.y, position.y + Grid));
                getIndexOnGrid(temp);
                orientation = random.nextBoolean();
            } while (isOnField(orientation, Pattern[i]) || isTouch(Pattern[i], orientation));

            //Создание и заполнение маркеров под корабль с учётом ориентации корабля
            mark = new ArrayList<>(Pattern[i]);
            if ((!orientation) && (MouseX + Pattern[i] < Cells)) {
                for (int j = 0; j < Pattern[i]; j++) {
                    markers[MouseX + j][MouseY].setAtShip(true);
                    mark.add(markers[MouseX + j][MouseY]);
                }
                createShip(i,  Pattern[i], false, mark);
            } else if (MouseY + Pattern[i] < Cells) {
                for (int j = 0; j < Pattern[i]; j++) {
                    markers[MouseX][MouseY + j].setAtShip(true);
                    mark.add(markers[MouseX][MouseY + j]);
                }
                createShip(i,  Pattern[i], true, mark);
            }

        }
    }

    /**
     * Метод createShip отвечает за создание корабля.
     *
     * @param index       индекс.
     * @param segments    Кол-во секций у корабля.
     * @param orientation В каком положении корабль (Вертикально / Горизонтально)
     */
    private void createShip(int index, int segments, boolean orientation, ArrayList<Marker> markers) {
        ships[index] = new Ship(segments, orientation, markers);
    }

    /**
     * Метод paintShips устанавливает видимость на клетки
     *
     * @param isVisible - отвечает за видимость кораблей.
     */
    public void paintShips(boolean isVisible) {
        for (Ship ship : ships) {
            ship.setVisibleForShip(isVisible);
        }
    }

    /**
     * Метод isTouch - проверяет можно ли разместить корабль в ячейки с индексами MouseX и MouseY.
     * @param segments - Кол-во секций корабля (# 4-x палубный, 3-x...).
     * @param orient   - Ориентация корабля (Горизонтальная / Вертикальная).
     * @return false - Если можно поставить корабль / true - если нельзя.
     */
    private boolean isTouch(int segments, boolean orient) {
        if (segments > 1) {
            for (int i = 0; i < segments; i++) {
                if (!orient) {
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            if ((MouseX + i + x >= 0)
                                    && (MouseX + i + x < Cells)
                                    && (MouseY + y >= 0)
                                    && (MouseY + y < Cells)
                                    && (markers[MouseX + i + x][MouseY + y].isShip())) {
                                return true;
                            }
                            //System.out.printf("\t(%d;%d)\t\n",MouseX + i + x,MouseY + i + y);
                        }
                    }
                } else {
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            if ((MouseX + x >= 0)
                                    && (MouseX + x < Cells)
                                    && (MouseY + i + y >= 0)
                                    && (MouseY + i + y < Cells)
                                    && (markers[MouseX + x][MouseY + i + y].isShip())) {
                                return true;
                            }
                            //System.out.printf("\t(%d;%d)\t\n",MouseX + i + x,MouseY + i + y);
                        }
                    }
                }
            }
        } else {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    if ((MouseX + x >= 0)
                            && (MouseX + x < Cells)
                            && (MouseY + y >= 0)
                            && (MouseY + y < Cells)
                            && (markers[MouseX + x][MouseY + y].isShip())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Метод isOnField - проверяет вылазит ли корабль с учётом секций за границы сетки.
     *
     * @return false - если корабль не вылазит, и true - иначе.
     */
    private boolean isOnField(boolean orient, int segments) {
        return ((!orient) && (MouseX + segments >= Cells)) || (MouseY + segments >= Cells);
    }

    /**
     * Метод isGameOver - проверят все ли корабли уничтожены.
     *
     * @return false - если корабли ещё есть, true - если все корабли уничтожены.
     */
    public boolean isGameOver() {
        for (int i = 0; i < Cells; i++) {
            for (int j = 0; j < Cells; j++) {
                if (markers[i][j].isShip()) return false;
            }
        }
        return true;
    }

    /**
     * Метод isShotOrDestroyed возвращает одно из 3-х состояний (мимо, попал, уничтожил).
     * @return integer (0 - 2)
     */
    private int isShotOrDestroyed() {

        //System.out.printf("isTouch = %b, isMissed = %b\n",isTouch(1,false),markers[MouseX][MouseY].isMissed());
        if (isTouch(1, false) && (!markers[MouseX][MouseY].isMissed())) {
            return 0;
        } else if (!isTouch(1, false) && (!markers[MouseX][MouseY].isMissed())) {
            return 1;
        }
        return 2;
    }

    /**
     * Метод paintComment отприсовывает надпись в зависимости от состояния.
     * @param g
     */
    private void paintComment(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 23));
        switch (isShotOrDestroyed()) {
            case 0 -> g.drawString("Попал!", position.x + Size * 4, position.y + Size * 11);
            case 1 -> g.drawString("Уничтожен!", position.x + Size * 4, position.y + Size * 11);
            case 2 -> g.drawString("Мимо!", position.x + Size * 4, position.y + Size * 11);
        }
    }

    /**
     * Устанавливает поле компьютера
     * @param isComputer (true / false)
     */
    public void setComputer(boolean isComputer) {
        this.isComputer = isComputer;
    }

    /**
     * Класс AI - компьютер, который стреляет наугад.
     */
    private class AI {
        // Сложность так и не была реализована
        private final int difficult;
        private final Random random;

        public AI(int difficult) {
            random = new Random();
            this.difficult = difficult;
        }

        public Position makeRandomChoice() {
            int x, y;

            do {
                x = random.nextInt(0, Cells);
                y = random.nextInt(0, Cells);
            } while (markers[x][y].isClicked());
            return new Position(x, y);
        }
    }
}
