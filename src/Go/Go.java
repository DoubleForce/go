package Go;

import FileWorker.FileWorker;
import Window.Window;
import Window.LoginWindow;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Методы с большой буквы?
    //Не давай писать всё с меленькой буквы, кроме аббривиатур. В случае, если в названии метода два слово, только второе
    //писать с большой буквы.

//Разобраться с public, private.
//Бегать по массив только с помощью (x, y) или (x1, y1).
//Не писать 20, есть константа Board_size
//Integer нельзя передать по ссылке?
//При неправильном вводе хода программа прерывается.
//Нерацильонально по времени ходы записывать в файл?
//Если в move вводить бред, то прога вылетит
//Формат move и ходов в start различается
//Функция подсчёта очков
//Функция, которая проверяет, можно ли ходить (чтобы даме не обнулялись)
//Сделать pass
//В move есть FileWorker.writeMove - этот writeMove сделан не рационально (+ он есть в DeleteStones).
//+ в доске может съесться

public class Go {
    //Пусть к папке данного проета
    // public static String PATH = "C://Users/Avdonin/IdeaProjects/iGo"; // "C:\\Users\\Avdonin\\IdeaProjects\\iGo"


    public static String fileNameofBoard = "src/board";
    public static String fileNameofClearBoard = "src/cleanBoard";
    public static String fileNameofHelp = "src/help";
    public static Move lastMove = new Move();
    public static int WHITE = 2;
    public static int BLACK = 1;
    public static int EMPTY = 0;
    public static int BOARD_SIZE = 19;

    public static int[][] board = new int[20][20];


    public static void main(String[] args) throws IOException {
        board = FileWorker.transferBoard(fileNameofBoard);
        commandLine();
    }


    public static void commandLine() throws IOException { //Командная строка
        Scanner sc = new Scanner(System.in);
        String command = "";

        while (!command.equals("q")) {
            System.out.print("> ");
            command = sc.nextLine();

            if (command.equals("print"))
                System.out.println(FileWorker.read(fileNameofBoard));

            else if (command.equals("board")) {
                print();
            }

            else if (command.equals("window")) {
                Window window = new Window();
            }

            else if (command.equals("login")) {
                LoginWindow loginwindow = new LoginWindow();
                loginwindow.setVisible(true);
            }

            else if (command.equals("help")) {
                System.out.print(FileWorker.read(fileNameofHelp));
            }

            else if (command.equals("clear")) {
                FileWorker.clearBoard();
                for (int i = 1; i<=BOARD_SIZE;i++)
                    for(int j = 1; j<=BOARD_SIZE;j++)
                        board[i][j] = 0;
                System.out.println("The board is clean");
            }

            else if (command.equals("start")) {
                int colorMove = BLACK;
                while(true) {
                    System.out.println(FileWorker.read(fileNameofBoard));

                    if (colorMove == BLACK) {
                        System.out.print("Black> ");
                    }
                    else {
                        System.out.print("White> ");
                    }

                    command = sc.nextLine();
                    if (command.equals("stop") || command.equals("q")) {
                        break;
                    }



                    colorMove = move(colorMove, FileWorker.parseX(command), FileWorker.parseY(command));
                }
            }

            Pattern p = Pattern.compile("move");
            Matcher m = p.matcher(command);
            if (m.find()) {
                command = command.substring(4);
                Scanner read = new Scanner(command);
                int color = read.nextInt();
                int x = read.nextInt();
                int y = read.nextInt();
                System.out.println();
                move(color, x, y);
                //Думаю, что со цветом можно получше здесь сделать
            }
        }
    }

    static class MyInteger {
        int value;
    }

    static class Move {
        int x;
        int y;
        boolean maybeKo;

        Move() {
            maybeKo = false;
        }
    }

    public static int move(int color, int x, int y) throws IOException{
        //Проверка корректности хода есть ли там даме и условие ко-борьбы
        //Снятие камней противоположного цвета у которых нет дамэ
        //Изменение доски
        if (board[x][y] != EMPTY) {
            System.out.println("This point is occupied!");
            return color;
        }

        board[x][y] = color;

        int label = 3;
        //Нужно что - нибудь сделать с этим: cnt = 3?

        int[][] boardBuffer = new int[20][20];
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                boardBuffer[i][j] = board[i][j];
            }
        }


        int countDeletedStones = 0;
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                if (boardBuffer[i][j] == (color % 2) + 1) {
                    MyInteger dameCount = new MyInteger();
                    dameCount.value = 0;
                    DFS(i, j, label, (color % 2) + 1, dameCount, boardBuffer);
                    if (dameCount.value == 0) {
                       countDeletedStones += DeleteStones(boardBuffer, label);
                    }
                    ++label;
                }
            }
        }



        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                boardBuffer[i][j] = board[i][j];
            }
        }

        if (countDeletedStones == 0) {
            MyInteger dameCount = new MyInteger();
            dameCount.value = 0;
            DFS(x, y, 3, color, dameCount, boardBuffer);
            if (dameCount.value == 0) {
                System.out.println("Dame count is 0!");
                board[x][y] = EMPTY;
                return color;
            }
        }


        /*board[x][y] = color;


        lastMove.x = x;
        lastMove.y = y;
        if (countDeletedStones == 1) {
            //Сделать в стиле ООП
            lastMove.maybeKo = true;
        } else {
            lastMove.maybeKo = false;
        }
*/

        FileWorker.writeMove(color,x,y); //Меняет файл доски после сделанного хода и всех проверок

        return 3 - color;
    }

    private static void DFS(int x, int y, int label, int color, MyInteger dameCount, int[][] boardBuffer) {
        boardBuffer[x][y] = label;
        final int[] dx = {1, 0, -1, 0};
        final int[] dy = {0, -1, 0, 1};
        for (int k = 0; k <= 3; ++k) {
            int x1 = x + dx[k];
            int y1 = y + dy[k];
            if (1 <= x1 && x1 <= 19 && 1 <= y1 && y1 <= 19) {
                if (boardBuffer[x1][y1] == color) {
                    DFS(x1, y1, label, color, dameCount, boardBuffer);
                } else if (boardBuffer[x1][y1] == EMPTY) {
                    dameCount.value = dameCount.value + 1;
                }
            }
        }
        //Если группа камней не имеет даме, то удаляем.
    }

    private static int DeleteStones(int[][] boardBuffer, int label) throws IOException{
        int ans = 0;
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                if (boardBuffer[i][j] == label) {
                    ++ans;
                    board[i][j] = EMPTY;
                    FileWorker.writeMove(EMPTY,i,j);
                }
            }
        }

        return ans;
    }

    public static void print() {
        for (int i = BOARD_SIZE; i >= 1; --i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                System.out.print(board[j][i] + " ");
            }
            System.out.println();
        }
    }
}