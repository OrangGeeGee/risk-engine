package lt.asinica.riskengine.data;

import lt.asinica.riskengine.exceptions.UserDoesNotExist;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In a non-csv data implementation this class would interact with persistent storage.
 */
@Repository
public class SampleUserRepository implements UserRepository {

    private Map<Integer, User> userMap;

    public SampleUserRepository() {
        userMap = loadCSV();
    }

    @Override
    public User findOne(Integer id) {
        if(userMap.containsKey(id))
            return userMap.get(id);
        else
            throw new UserDoesNotExist();
    }

    @Override
    public void save(User user) {}

    private Map<Integer, User> loadCSV() {
        List<String[]> content = loadCSVContent("initial-data-set.csv");
        return parseCSVContent(content);
    }

    private List<String[]> loadCSVContent(String filename) {
        List<String[]> content = new ArrayList<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line.split(","));
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read " + filename, e);
        }
        return content;
    }

    private Map<Integer, User> parseCSVContent(List<String[]> content) {
        Map<Integer, User> userMap = new HashMap<>();
        String[] header = content.get(0);
        for (int i = 1; i < content.size(); i++) {
            User user = parseUser(header, content.get(i));
            userMap.put(user.getId(), user);
        }
        return userMap;
    }

    private User parseUser(String[] header, String[] strings) {
        Map<String, BigDecimal> availableBalances = parseBalances(header, strings);
        return new User(Integer.parseInt(strings[0]), availableBalances);
    }

    private Map<String, BigDecimal> parseBalances(String[] header, String[] strings) {
        Map<String, BigDecimal> availableBalances = new HashMap<>();
        for (int j = 1; j < header.length; j++) {
            availableBalances.put(header[j], new BigDecimal(strings[j]));
        }
        return availableBalances;
    }
}
