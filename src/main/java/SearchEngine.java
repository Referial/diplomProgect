import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface SearchEngine { // получает слово отправлят список
    List<PageEntry> search(String wosrd) throws IOException, ParseException;
}
