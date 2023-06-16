package utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class CSVFileUtil {

    private CSVFileUtil() {
    }

    public static List<Map<String, String>> read(File file) throws IOException {
        List<Map<String, String>> response = new LinkedList<>();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        try (MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class)
                .with(schema)
                .readValues(file)) {
            while (iterator.hasNext()) {
                response.add(iterator.next());
            }
        }
        return response;
    }


}
