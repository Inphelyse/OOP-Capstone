import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class Files {

    // Creating a default data
    public static void defaultAccount() {
        System.out.println("default accs");
        new File("Persons").mkdirs();

        createDefaultFile("admin", "Admin|C|123123123|099912712|Secret");
        createDefaultFile("admin", "Admin|B|123123123|099912312|Secret");
        createDefaultFile("admin", "Admin|A|123123123|099911262|Secret");
        createDefaultFile("courier", "Courier|JK Catalo|22223333|09991112233|Cebu City");
        createDefaultFile("seller", "Seller|JP Magdasal|22221111|09991112333|Cebu City");
        createDefaultFile("customer", "Customer|Nico Carato|11112222|09991152233|Cebu City");
        createDefaultFile("seller", "Seller|Shinya Tamaki|11111111|09991192233|Cebu City");

        // Create banned.txt without adding a default account
        File file = new File("Persons/banned.txt");
    }

    private static int ctr = 0;

    // Creating a default account (already made accounts in files such as for Customer, Seller, Courier, and Admin)
    private static void createDefaultFile(String role, String line) {
        File file = new File("Persons/" + role + ".txt");
        ctr++;
        System.out.println(ctr);

        // This will check if an existing accounts already exists or already stored in the file
        if (file.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(file))) {
                String thisLine;

                while((thisLine = br.readLine()) != null) {
                    if(thisLine.equals(line)) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error 12 " + role);
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error creating " + role + ".txt");
        }
    }


    // This method/function  reads all account from a specific file (reason why we need "String role") and return as ArrayList of that account
    public static ArrayList<String[]> readAccounts(String role) throws IOException {
        ArrayList<String[]> accounts = new ArrayList<>();
        File file = new File("Persons/" + role.toLowerCase() + ".txt");

        if (!file.exists()) {
            return accounts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                if (parts.length >= 5) {
                    accounts.add(parts);
                }
            }
        }

        return accounts;
    }

    // This
    static String[] findAccount(String username) {
        String[] fileLocation = {"Persons/admin.txt", "Persons/seller.txt", "Persons/customer.txt", "Persons/courier.txt", "Persons/banned.txt"};

        for (int i = 0; i < fileLocation.length; i++) {
            String file = fileLocation[i];
            try {
                Scanner read = new Scanner(new File(file));
                while (read.hasNextLine()) {
                    String line = read.nextLine();
                    String[] row = line.split("\\|");

                    if (row.length >= 5 && row[1].equals(username)) {
                        read.close();
                        return row;
                    }
                }
                read.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + file);
            }
        }
        return null;
    }

    static boolean findPhoneNum(int phoneNum) {
        String[] fileLocation = {"Persons/admin.txt", "Persons/seller.txt", "Persons/customer.txt", "Persons/courier.txt", "Persons/banned.txt"};

        for (int i = 0; i < fileLocation.length; i++) {
            String file = fileLocation[i];
            try {
                Scanner read = new Scanner(new File(file));
                while (read.hasNextLine()) {
                    String line = read.nextLine();
                    String[] row = line.split("\\|");

                    int flagPhone = Integer.parseInt(row[3]);

                    if(flagPhone == phoneNum) {
                        read.close();
                        return true;
                    }
                }
                read.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + file);
            }
        }
        return false;
    }


    // This saves the account to a file. This is used in signing up
    static void saveAccount(Person p) {
        String path = p.readType().equals("Seller") ? "Persons/seller.txt" : p.readType().equals("Courier") ? "Persons/courier.txt" : "Persons/customer.txt";
        String line = p.readLine();
        File file = new File(path);
        // Reading
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println("save");
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save " + path + ": " + ex.getMessage());
        }
    }

    // This checks how many customer accounts were made or are stored in the customer.txt file
    public static int customerTotal() {
        File file1 = new File("Persons/customer.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                System.out.println("Exist");
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error5: " + ex.getMessage());
            }
        }
        return count;
    }

    // This checks how many seller accounts were made or are stored in the customer.txt file
    public static int sellerTotal() {
        File file1 = new File("Persons/seller.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error5: " + ex.getMessage());
            }
        }
        return count;
    }

    // This checks how many courier accounts were made or are stored in the courier.txt file
    public static int courierTotal() {
        File file1 = new File("Persons/courier.txt");
        int count = 0;

        if(file1.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length < 5) {
                        continue;
                    }
                    count++;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error5: " + ex.getMessage());
            }
        }
        return count;
    }

    // This checks how many accounts were made or are stored in all files
    public static int totalPeople() {
        return customerTotal() + sellerTotal() + courierTotal();
    }

    // Write a list of accounts to a file
    private static void writeAccounts(String role, ArrayList<String[]> accounts) throws IOException {
        File file = new File("Persons/" + role.toLowerCase() + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println("write" + i);
                bw.write(String.join("|", accounts.get(i)));
                bw.newLine();
            }
        }
    }

    // ======== Banning =======//

    // This is
    public static void banAccount(String role, String[] account, String reason) throws IOException {
        ArrayList<String[]> accounts = readAccounts(role);
        ArrayList<String[]> banned = readAccounts("banned");

        boolean flag = false;

        for (int i = 0; i < accounts.size(); i++) {String[] parts = accounts.get(i);
            if (parts[0].equals(role) && parts[1].equals(account[1]) && parts[2].equals(account[2])) {
                String[] bannedAccount = {parts[0], parts[1], parts[2], parts[3], parts[4], reason};
                banned.add(bannedAccount);
                accounts.remove(i);
                flag = true;
                break;
            }
        }

        if (!flag) {

            throw new IOException("Account not found.");
        }

        // Save the banned account first
        writeAccounts("banned", banned);
        writeAccounts(role, accounts);
    }

    // Restore a banned account to its original role file
    public static void unbanAccount(String role, String username) throws IOException {
        ArrayList<String[]> banned = readAccounts("banned");
        ArrayList<String[]> accounts = readAccounts(role);

        boolean found = false;

        for (int i = 0; i < banned.size(); i++) {
            String[] parts = banned.get(i);
            if (parts[0].equals(role) && parts[1].equals(username)) {
                System.out.println("restore" + i);
                String[] restored = {parts[0], parts[1], parts[2], parts[3], parts[4]};
                accounts.add(restored);
                banned.remove(i);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("Banned account not found.");
        }

        System.out.println("Unban success");

        writeAccounts(role, accounts);
        writeAccounts("banned", banned);
    }

    // This creates a new file named history.txt, it saves the activity of the admin whenever they uses their authority like banning
    public static void saveHistory(String action, String username, String role, String admin) throws IOException {
        File file = new File("Persons/history.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(action + "|" + username + "|" + role + "|" + admin);
            bw.newLine();
        }
    }
}
