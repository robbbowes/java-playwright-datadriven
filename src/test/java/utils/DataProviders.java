package utils;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataProviders {

    @DataProvider(name = "HashTableDataProvider", parallel = true)
    public static Iterator<Object[]> getData() throws IOException {
        File file = new File(DataPathResolve.resolveDataPath("AddCustomerData.csv"));
        List<Map<String, String>> read = CSVFileUtil.read(file);

        Collection<Object[]> dp = new ArrayList<>();
        for (Map<String, String> map : read) {
            dp.add(new Object[]{map});
        }
        return dp.iterator();
    }
}
