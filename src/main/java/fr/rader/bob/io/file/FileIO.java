package fr.rader.bob.io.file;

import java.io.*;

public class FileIO {

    private static final int BUFFER_SIZE = 4096;

    public static InputStream read(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void write(File destination, InputStream inputStream) {
        try {
            FileOutputStream outputStream = new FileOutputStream(destination);

            int length;
            byte[] buffer = new byte[BUFFER_SIZE];
            while((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean createFile(File file) throws IOException {
        if(file.isDirectory()) {
            if(!file.delete()) {
                System.out.println("Could not delete folder (same name as file)");
                return false;
            }
        }

        if(!file.exists()) {
            if(!file.getParentFile().exists()) {
                if(!file.getParentFile().mkdirs()) {
                    System.out.println("Could not create file's directories, they might already exist");
                    return false;
                }
            }

            if(!file.createNewFile()) {
                System.out.println("Could not create file");
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public static boolean createFolder(String file) {
        return createFolder(new File(file));
    }

    public static boolean createFolder(File file) {
        if(file.isFile()) {
            if(!file.delete()) {
                System.out.println("Could not delete file (same name as folder)");
                return false;
            }
        }

        return !file.exists() && file.mkdirs();
    }

    public static void deleteDirectory(String directory) {
        deleteDirectory(new File(directory));
    }

    public static void deleteDirectory(File directory) {
        if(directory.exists() && directory.isDirectory()) {
            for(File file : directory.listFiles()) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}
