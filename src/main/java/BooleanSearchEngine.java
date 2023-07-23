import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected static HashMap<String, List<PageEntry>> listHashMap = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        String name = pdfsDir.getName();

        var doc = new PdfDocument(new PdfReader(pdfsDir));
        int numberPage = doc.getNumberOfPages();

        for (int x = 1; x <= numberPage; x++) {
            Map<String, Integer> freqs = new HashMap<>();

            PdfPage n = doc.getPage(x);
            var text = PdfTextExtractor.getTextFromPage(n).toLowerCase();
            var words = text.split("\\P{IsAlphabetic}+");

            for (var word : words) { // перебираем слова
                if (word.isEmpty()) {
                    continue;
                }
                word = word.toLowerCase();
                if (!listHashMap.containsKey(word)) {
                    listHashMap.put(word, new ArrayList<>());
                }
                freqs.put(word, freqs.getOrDefault(word, 0) + 1);
            }

            for (int y = 0; y < freqs.size(); y++) {
                List<String> l = new ArrayList<>(freqs.keySet());
                String sl = l.get(y);

                listHashMap.get(sl).add(new PageEntry(name, x, freqs.get(sl)));
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) throws IOException, ParseException {
        word.toLowerCase();

        List<PageEntry> list = listHashMap.get(word);

        Collections.sort(list);

        return list;
    }
}