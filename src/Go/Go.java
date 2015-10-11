package Go;

import FileWorker.FileWorker;
import Window.Window;

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
public class Go {
    //Пусть к папке данного проета
   // public static String PATH = "C://Users/Avdonin/IdeaProjects/iGo"; // "C:\\Users\\Avdonin\\IdeaProjects\\iGo"

    private static String fileNameofBoard = "src/board.txt"; //PATH+"\\src\\board.txt"; // C://users/avdonin/ideaprojects/go/src/board.txt
    private static String fileNameofHelp =  "src/help.txt"; //PATH+"\\src\\help.txt";

    final static int WHITE = 2;
    final static int BLACK = 1;
    final static int EMPTY = 0;
    final static int BOARD_SIZE = 19;

    public static int[][] board = new int[20][20];


    public static void main(String[] args) throws IOException {
        board = FileWorker.transferBoard(fileNameofBoard);
        commandLine();
    }

    public static void commandLine() throws IOException{ //Командная строка
        Scanner sc = new Scanner(System.in);
        String command = "";

        while (!command.equals("q")){
            System.out.print("> ");
            command = sc.nextLine();

            if (command.equals("print"))
                print();

            else if (command.equals("window")){
                Window window = new Window();
            }
            else if (command.equals("help")){
                System.out.print(FileWorker.read(fileNameofHelp));
            }
            Pattern p = Pattern.compile("move");
            Matcher m = p.matcher(command);
            if (m.find()){
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

    public static void move(int color,int x,int y){
        //Проверка корректности хода есть ли там даме и условие ко-борьбы
        //Снятие камней противоположного цвета у которых нет дамэ
        //Изменение доски
        if (board[x][y] != EMPTY) {
            System.out.println("This point is occupied!");
        } else {
            board[x][y] = color;//
            System.out.println(x+" "+y);
        }

    }

    public static void print() {
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            for (int j = 1; j <= BOARD_SIZE; ++j) {
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void DFS() {
        //Если группа камней не иммет даме, то удаляем.
    }
}
