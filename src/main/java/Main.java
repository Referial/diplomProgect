import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    static BooleanSearchEngine engine;

    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(SSocket.PORT)) { // стартуем сервер один(!) раз

            System.out.println("Сервер запущен");

            try (Stream<Path> filePathStream = Files.walk(Paths.get("pdfs"))) {
                filePathStream.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        try {
                            String s = String.valueOf(filePath);
                            engine = new BooleanSearchEngine(new File(s));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                while (true) {
                    try (Socket socket = server.accept();
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter out = new PrintWriter(socket.getOutputStream());) {

                        String word = in.readLine();

                        out.println(engine.search(word));
                    }
                }
            } catch (IOException e) {
                System.out.println("Не могу стартовать сервер");
                e.printStackTrace();
            }
        }
    }
}
