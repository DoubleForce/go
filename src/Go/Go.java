package Go;

import FileWorker.FileWorker;
import Window.Window;
import Window.LoginWindow;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Обрати внимание на комментарий к PATH. Я не знаю путь к твоему файлу, но когда он появится (ты его сейчас исправишь),
    я буду оставлять его в комментиях перед отправкой, как оставлен сейчас путь к папке моего проекта, чтобы когда ты
    обновлял проект, тебе было нужно просто скопировать путь из комментария. Таким образом, если ты первый раз это
    читаешь, то не нужно трогать комментарий, в противном случае перед тем как отправить версию тебе нужно будет
    убедиться, что в комментарии мой путь. Такой взаимный обмен.

    Запустив програамму введи help
 */

//Методы с большой буквы?
    //Не давай писать всё с меленькой буквы, кроме аббривиатур. В случае, если в названии метода два слово, только второе
    //писать с большой буквы.

//Разобраться с public, private.
//Бегать по массив только с помощью (x, y) или (x1, y1).
//Не писать 20, есть константа Board_size
//Integer нельзя передать по ссылке?
//При неправильном вводе хода программа прерывается.


public class Go {
    //Пусть к папке данного проета
    // public static String PATH = "C://Users/Avdonin/IdeaProjects/iGo"; // "C:\\Users\\Avdonin\\IdeaProjects\\iGo"


    public static String fileNameofBoard = "src/board";
    public static String fileNameofClearBoard = "src/cleanBoard";
    public static String fileNameofHelp = "src/help";

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
                boolean isBlackMove = true;
                while(true){
                    System.out.println(FileWorker.read(fileNameofBoard));
                    if (isBlackMove) {
                        System.out.print("Black> ");
                        command = sc.nextLine();
                        if (command.equals("stop")||command.equals("q"))
                            break;
                        move(1, FileWorker.parseX(command), FileWorker.parseY(command));

                    }
                    else {
                        System.out.print("White> ");
                        command = sc.nextLine();
                        if (command.equals("stop")||command.equals("q"))
                            break;

                        move(2, FileWorker.parseX(command), FileWorker.parseY(command));
                    }
                    isBlackMove = !isBlackMove;
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

    public static void move(int color, int x, int y) throws IOException{
        //Проверка корректности хода есть ли там даме и условие ко-борьбы
        //Снятие камней противоположного цвета у которых нет дамэ
        //Изменение доски
        if (board[x][y] != EMPTY) {
            System.out.println("This point is occupied!");
        } else {
            board[x][y] = color;
        }

        int label = 3;
        //Нужно что - нибудь сделать с этим: cnt = 3?

        int[][] boardBuffer = new int[20][20];
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                boardBuffer[i][j] = board[i][j];
            }
        }


        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                if (boardBuffer[i][j] == (color % 2) + 1) {
                    MyInteger dameCount = new MyInteger();
                    dameCount.value = 0;
                    DFS(i, j, label, (color % 2) + 1, dameCount, boardBuffer);
                    if (dameCount.value == 0) {
                        DeleteStones(boardBuffer, label);
                    }
                    ++label;
                }
            }
        }
        FileWorker.writeMove(color,x,y); //Меняет файл доски после сделанного хода и всех проверок
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
        //Если группа камней не иммет даме, то удаляем.
    }

    private static void DeleteStones(int[][] boardBuffer, int label) throws IOException{
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                if (boardBuffer[i][j] == label) {
                    board[i][j] = EMPTY;
                    FileWorker.writeMove(EMPTY,i,j);
                }
            }
        }
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