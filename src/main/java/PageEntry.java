import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public int getCount() {
        return count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("pdfName", pdfName);
        jsonObject.put("page", page);
        jsonObject.put("count", count);

        return  jsonObject.toString();
    }

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.getCount(), getCount());
    }
}
