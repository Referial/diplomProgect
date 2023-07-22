import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(SSocket.HOST, SSocket.PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Введите слово для поиска");

            writer.println(scanner.nextLine());

            String s = reader.readLine();

            ObjectMapper mapper = new ObjectMapper();

            Object json = mapper.readValue(s, Object.class);
            System.out.println("Результаты поиска \n"
                    + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
