import java.util.HashMap;
import java.util.Map;
import java.io.*;


public class UserStore {


    private static final Map<String, String> users = new HashMap<>();
    private static final String FILE_NAME = "users.csv";


    public static boolean exists(String username) {
        return users.containsKey(username.toLowerCase());
    }


    public static boolean register(String username, String password) {
        if (exists(username)) return false;
        users.put(username.toLowerCase(), password);
        saveUsers();
        return true;
    }

    private static void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadUsers() {

        users.clear();

        File file = new File(FILE_NAME);

        if (!file.exists()) return;

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username =
                            parts[0].trim().toLowerCase();
                    String password =
                            parts[1].trim();
                    users.put(username, password);
                    System.out.println(username + " : " + password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(String username, String password) {
        String stored = users.get(username.toLowerCase());
        return stored != null && stored.equals(password);
    }
}