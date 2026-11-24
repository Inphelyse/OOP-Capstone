import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Files {

    // Creating a default data
    static void defaultAccount() {
        // Creating the folder
        new File("Persons").mkdir();

        // For admin
        File admin = new File("Persons/admin.txt");
        if (admin.exists() && admin.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(admin, true))) {
            bw.write("Admin|Boss|11112222|099912312|Secret");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save");
            System.out.println("test admin acc");
        }

        // For courier
        File courier = new File("Persons/courier.txt");
        if (courier.exists() && courier.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(courier, true))) {
            bw.write("Courier|Nico Carato|courier123|09991112233|Cebu City");
            bw.newLine();
            bw.write("Courier|Courier|123123123|09171234567|Mandaue City");
            bw.newLine();
            bw.write("Courier|Luis Gomez|riderpass1|09228887766|Lapu-Lapu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test courier acc");
        }

        // For seller
        File seller = new File("Persons/seller.txt");
        if (seller.exists() && seller.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(seller, true))) {
            bw.write("Seller|Nico Carato|courier123|09991112233|Cebu City");
            bw.newLine();
            bw.write("Seller|Seller|123123123|09171234567|Mandaue City");
            bw.newLine();
            bw.write("Seller|Luis Gomez|riderpass1|09228887766|Lapu-Lapu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test seller acc");
        }

        // For customer
        File customer = new File("Persons/customer.txt");
        if (customer.exists() && customer.length() > 0) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customer, true))) {
            bw.write("Customer|Nico Carato|courier123|09991112233|Cebu City");
            bw.newLine();
            bw.write("Customer|Customer|123123123|09171234567|Mandaue City");
            bw.newLine();
            bw.write("Customer|Luis Gomez|riderpass1|09228887766|Lapu-Lapu City");
            bw.newLine();
            bw.write("Customer|Nico Carato|courier123|09991112233|Cebu City");
            bw.newLine();
            bw.write("Customer|Ana Reyes|delivery88|09171234567|Mandaue City");
            bw.newLine();
            bw.write("Customer|Luis Gomez|riderpass1|09228887766|Lapu-Lapu City");
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Could not save courier");
            System.out.println("test customer acc");
        }
    }
}
