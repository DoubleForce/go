package FileWorker;

import Go.Go;

import java.io.*;
import java.util.Scanner;



public class FileWorker {

    public static int[][] transferBoard(String fileName) throws IOException{
        File file = new File(fileName);
        int[][] result = new int[20][20];
        for(int j = 0; j<20; j++){for(int i = 0; i<20; i++){result[i][j]=0;}}

        exists(fileName);

        try {
            //Объект для чтения файла в буфер
            Scanner sc = new Scanner(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String line = sc.nextLine();
                for(int i = Go.BOARD_SIZE; i >= 1; --i) {
                    line = sc.nextLine();
                    for (int j = 1; j <= Go.BOARD_SIZE; ++j) {
                        if (line.substring(3+2*(j-1),4+2*(j-1)).equals("·"))
                            result[j][i] = 0;
                        else if (line.substring(3+2*(j-1),4+2*(j-1)).equals("•"))
                            result[j][i] = 1;
                        else if (line.substring(3+2*(j-1),4+2*(j-1)).equals("o"))
                            result[j][i] = 2;
                    }
                }
            } finally {
                //Также не забываем закрыть файл
                sc.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //Возвращаем полученный текст с файла
       return result;
    }

    public static void writeMove(int color,int x, int y) throws IOException{
        File file = new File(Go.fileNameofBoard);
        StringBuilder sb = new StringBuilder();
        exists(Go.fileNameofBoard);
        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                int i = Go.BOARD_SIZE+1;
                while ((s = in.readLine()) != null) {
                    if (i == y){
                        if (color == Go.EMPTY)
                            s = s.substring(0,3+2*(x-1)) + '·' + s.substring(4+2*(x-1));
                        if (color == Go.BLACK)
                            s = s.substring(0,3+2*(x-1)) + '•' + s.substring(4+2*(x-1));
                        else if (color == Go.WHITE)
                            s = s.substring(0,3+2*(x-1)) + 'o' + s.substring(4+2*(x-1));
                    }
                    sb.append(s);
                    sb.append("\n");
                    i--;
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        write(Go.fileNameofBoard,sb.toString());
    }

    public static void clearBoard() throws IOException{
        write(Go.fileNameofBoard,read(Go.fileNameofClearBoard).toString());
    }

    public static int getUserInfo(String fileName,String login, String password) {
        File file = new File(fileName);

        int result = 1;

        /*
            0 -- Введён правильно логин и пароль
            1 -- Такого пользователя не существует
            2 -- Пароль введён неправильно
         */

        String[] database = new String[100];

        String line = "";

        try {
            //Объект для чтения файла в буфер
            Scanner sc = new Scanner(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                line = sc.nextLine();
                while(!(line.equals("end"))) {
                    if (login.equals(line.substring(0, line.indexOf(" ")))) {
                        if (password.equals(line.substring(line.indexOf(" ") + 1))) {
                            result = 0;
                            break;
                        }
                        else {
                            result = 2;
                            break;
                        }
                    }
                    line = sc.nextLine();
                }
            } finally {
                //Также не забываем закрыть файл
                sc.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static String read(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        //Этот спец. объект для построения строки
        StringBuilder sb = new StringBuilder();

        exists(fileName);

        try {
            //Объект для чтения файла в буфер
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                //Также не забываем закрыть файл
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //Возвращаем полученный текст с файла
        return sb.toString();
    }

    public static void write(String fileName, String text) {
        //Определяем файл
        File file = new File(fileName);

        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст в файл
                out.print(text);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) throw new FileNotFoundException(file.getName());
    }

    public static void delete(String nameFile) throws FileNotFoundException {
        exists(nameFile);
        new File(nameFile).delete();
    }

    public static void update(String nameFile, String newText) throws FileNotFoundException {
        exists(nameFile);
        StringBuilder sb = new StringBuilder();
        String oldFile = read(nameFile);
        sb.append(oldFile);
        sb.append(newText);
        write(nameFile, sb.toString());
    }

    public static int parseX(String Coordinates) throws IOException{
        char ch = Coordinates.charAt(0);
        if (ch < 'I') {
            return ch - 'A' + 1;
        } else {
            return ch - 'A';
        }
    }
    public static int parseY(String Coordinates) throws IOException{
        return Integer.parseInt(Coordinates.substring(1));
    }
}
