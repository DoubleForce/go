package FileWorker;

import java.io.*;
import java.util.Scanner;



public class FileWorker {

    public static int[][] transferBoard(String fileName) throws IOException{
        File file = new File(fileName);
        int[][] result = new int[20][20];
        for(int j = 0; j<20; j++){for(int i = 0; i<20; i++){result[i][j]=0;}}
        //Этот спец. объект для построения строки

        exists(fileName);

        try {
            //Объект для чтения файла в буфер
            Scanner sc = new Scanner(new FileReader(file.getAbsoluteFile()));
            try {
                //В цикле построчно считываем файл
                for(int i = 19; i >= 1; --i)
                    for(int j = 1; j <= 19; ++j){
                        result[j][i] = sc.nextInt();
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
                //Записываем текст у файл
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
}
