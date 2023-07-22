import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.json.stream.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    static ListMultimap<String, HashMap> listWords = ArrayListMultimap.create();

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
                freqs.put(word, freqs.getOrDefault(word, 0) + 1);
            }

            for (int y = 0; y < freqs.size(); y++) {
                List<String> l = new ArrayList<>(freqs.keySet());

                Map<String, Object> ages = new HashMap<>();

                ages.put("pdfName", name);
                ages.put("page", x);
                ages.put("count", freqs.get(l.get(y)));

                listWords.put(l.get(y), (HashMap) ages);
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) throws IOException, ParseException {
        word.toLowerCase();
        List<PageEntry> list = new ArrayList<>();

        if (listWords.containsKey(word)) {

            for (int x = 0; x < listWords.get(word).size(); x++) {

                String pdfName = (String) listWords.get(word).get(x).get("pdfName");
                int page = (Integer) listWords.get(word).get(x).get("page");
                int count = (Integer) listWords.get(word).get(x).get("count");

                list.add(new PageEntry(pdfName, page, count));
            }

            Collections.sort(list);
        }
        return list;
    }
}
