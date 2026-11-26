import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class Files {

    // Creating a default data
    public static void defaultAccount() {
        // Creating the folder
        new File("Persons").mkdir();

        // For admin
        File admin = new File("Persons/admin.txt");
        if (admin.exists() && admin.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(admin, true))) {
            bw.write("Admin|Boss Tamaki|11112222|099912312|Secret");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save");
            System.out.println("test admin acc");
        }

        // For courier
        File courier = new File("Persons/courier.txt");
        if (courier.exists() && courier.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courier, true))) {
            bw.write("Courier|JK Catalo|22223333|09991112233|Cebu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test courier acc");
        }

        // For seller
        File seller = new File("Persons/seller.txt");
        if (seller.exists() && seller.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(seller, true))) {
            bw.write("Seller|JP Magdasal|22221111|09991112233|Cebu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test seller acc");
        }

        // For customer
        File customer = new File("Persons/customer.txt");
        if (customer.exists() && customer.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customer, true))) {
            bw.write("Customer|Nico Carato|11112222|09991112233|Cebu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test customer acc");
        }
    }

    static String[] findAccount(String username) {
        String[] fileLocation = {"Persons/admin.txt", "Persons/seller.txt", "Persons/customer.txt", "Persons/courier.txt"};

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
        String[] fileLocation = {"Persons/admin.txt", "Persons/seller.txt", "Persons/customer.txt", "Persons/courier.txt"};

        for (int i = 0; i < fileLocation.length; i++) {
            String file = fileLocation[i];
            try {
                Scanner read = new Scanner(new File(file));
                while (read.hasNextLine()) {
                    String line = read.nextLine();
                    String[] row = line.split("\\|");

                    int flagPhone = Integer.parseInt(row[2]);

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

    static void saveAccount(Person p) {
        String path = p.readType().equals("Seller") ? "Persons/seller.txt" : p.readType().equals("Courier") ? "Persons/courier.txt" : "Persons/customer.txt";
        String line = p.readLine();
        File file = new File(path);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save " + path + ": " + ex.getMessage());
        }
    }

    public static int customerTotal() {
        File file1 = new File("Persons/customer.txt");
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

    public static int totalPeople() {
        return customerTotal() + sellerTotal() + courierTotal();
    }
}
