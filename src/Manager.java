// We need to import the java.sql package to use JDBC
import java.sql.*;

// for reading from the command line
import java.io.*;
import java.util.*;

public class Manager extends controller{

    private int managerID;
    private int branch;

    public void validateID () {
        int               input;
        int               id;
        boolean           quit = false;
        ResultSet         rs;
        PreparedStatement ps;

        System.out.println(connect("ora_a1q1b", "a24581167"));
        try {
            while (!quit) {
                System.out.print("\n\nPlease enter your manager id or press enter 0 to quit: \n");

                input = Integer.parseInt(in.readLine());

                if (input != 0) {
                    //id = input;
                    id = 1252;
                    ps = con.prepareStatement("SELECT * FROM Clerk WHERE clerkID = ? AND type = 'Manager'");
                    ps.setInt(1, id);

                    rs = ps.executeQuery();

                    if (rs.next()) {
                        System.out.print("Access granted: Welcome.");
                        managerID = id;


                        branch = rs.getInt("branchNumber");

                        showMenu();
                    } else {
                        System.out.print("Access denied: Invalid manager ID.");
                    }

                    // close the statement;
                    // the ResultSet will also be closed
                    ps.close();
                } else { // User quits the system
                    quit = true;
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());

            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    public void showMenu() throws SQLException, IOException{
        int     choice;
        boolean quit = false;

        while (!quit) {
            System.out.print("\n");
            System.out.print("1.  Show all employees\n");
            System.out.print("2.  Manage employee\n");
            System.out.print("3.  Manage item\n");
            System.out.print("4.  Manage membership\n");
            System.out.print("5.  Manage deal\n");
            System.out.print("6.  Generate report\n");
            System.out.print("7.  Quit\n>> ");


            choice = Integer.parseInt(in.readLine());

            System.out.println(" ");

            switch (choice) {
                case 1:
                    showAllEmployees();
                    break;
                case 2:
                    manageEmployeeWage();
                    break;
                case 3:
                    manageItem();
                    break;
                case 4:
                    manageMembership();
                    break;
                case 5:
                    manageDeal();
                    break;
                case 6:
                    getReport();
                    break;
                case 7:
                    quit = true;
            }
        }
    }

    private void manageEmployeeWage() throws IOException, SQLException{
        int                id;
        int                wage;
        PreparedStatement  ps;

        ps = con.prepareStatement("UPDATE Clerk SET wage = ? WHERE clerkID = ?");

        System.out.print("\nClerk ID: ");
        id = Integer.parseInt(in.readLine());
        ps.setInt(2, id);

        showEmployee(id);

        System.out.print("\nSet new wage: ");
        wage = Integer.parseInt(in.readLine());
        while (wage < 0) {
            System.out.print("\nWage cannot be negative, please try again: ");
            wage = Integer.parseInt(in.readLine());
        }
        ps.setInt(1, wage);

        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            System.out.println("\nEmployee " + id + " does not exist!");
        }

        con.commit();

        ps.close();
    }

    private void showEmployee(int id) throws IOException, SQLException {
        int clerkID = id;
        String     name;
        String     type;
        int        wage;
        int        branchNumber;
        PreparedStatement  ps;
        ResultSet  rs;

        ps = con.prepareStatement("SELECT * FROM Clerk WHERE clerkID = ?");
        ps.setInt(1, clerkID);

        rs = ps.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount();

        System.out.println(" ");

        // display column names;
        for (int i = 0; i < numCols; i++)
        {
            // get column name and print it
            System.out.printf("%-15s", rsmd.getColumnName(i+1));
        }

        System.out.println(" ");

        while(rs.next())
        {
            // simplified output formatting; truncation may occur
            clerkID = rs.getInt("clerkID");
            System.out.printf("%-5s", clerkID);

            name = rs.getString("name");
            System.out.printf("%-5s", name);

            wage = rs.getInt("wage");
            System.out.printf("%-5s\n", wage);

            branchNumber = rs.getInt("branchNumber");
            System.out.printf("%-5s", branchNumber);

            type = rs.getString("type");
            System.out.printf("%-5s", type);
        }
    }

    private void showAllEmployees() throws SQLException {
        int     clerkID;
        String     name;
        String     type;
        int        wage;
        int        branchNumber;
        Statement  stmt;
        ResultSet  rs;

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Clerk");

        // get info on ResultSet
        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount();

        System.out.println(" ");

        // display column names;
        for (int i = 0; i < numCols; i++)
        {
            // get column name and print it
            System.out.printf("%-15s", rsmd.getColumnName(i+1));
        }

        System.out.println(" ");

        while(rs.next())
        {
            // simplified output formatting; truncation may occur
            clerkID = rs.getInt("clerkID");
            System.out.printf("%-5s", clerkID);

            name = rs.getString("name");
            System.out.printf("%-5s", name);

            wage = rs.getInt("wage");
            System.out.printf("%-5s\n", wage);

            branchNumber = rs.getInt("branchNumber");
            System.out.printf("%-5s", branchNumber);

            type = rs.getString("type");
            System.out.printf("%-5s", type);
        }
        // close the statement;
        // the ResultSet will also be closed
        stmt.close();
    }

    private void manageItem() throws IOException, SQLException{

        int     choice;
        boolean quit = false;

        while (!quit) {
            System.out.print("1.  Manage item storage\n");
            System.out.print("2.  Display item storage\n");
            System.out.print("3.  Manage item price\n");
            System.out.print("4.  Go back\n>> ");

            choice = Integer.parseInt(in.readLine());

            System.out.println(" ");

            switch (choice) {
                case 1:
                    manageItemStorage();
                    break;
                case 2:
                    manageItemPrice();
                    break;
                case 3:
                    showMenu();
            }
        }
    }

    private void manageItemStorage() throws IOException, SQLException{
        int id;
        int amount;
        PreparedStatement ps;

        ps = con.prepareStatement("UPDATE Storage SET amount = ? WHERE itemID = ? AND branchNumber = ?");
        System.out.print("\nItem ID: ");
        id = Integer.parseInt(in.readLine());

        ps.setInt(2, id);
        ps.setInt(3, branch);
        displayItemInfo(id);
        System.out.print("\nSet new storage amount: ");
        amount = Integer.parseInt(in.readLine());
        while (amount < 0) {
            System.out.print("\nStorage cannot be negative, please try again: ");
            amount = Integer.parseInt(in.readLine());
        }
        ps.setInt(1, amount);

        int rowCount = ps.executeUpdate();
        if (rowCount == 0) {
            System.out.println("\nStorage of item ID " + id + " does not exist in your branch!");
        }
        con.commit();
    }

    private void manageItemPrice() throws IOException, SQLException {
      int id;
      double price;
      PreparedStatement ps;

      ps = con.prepareStatement("UPDATE Item SET price = ? WHERE itemID = ? AND branchNumber = ?");
      System.out.print("\nItem ID: ");
      id = Integer.parseInt(in.readLine());

      ps.setInt(2, id);
      ps.setInt(3, branch);
      displayItemInfo(id);
      System.out.print("\nSet new storage price: ");
      price = Double.parseDouble(in.readLine());
      while (price < 0) {
          System.out.print("\nStorage cannot be negative, please try again: ");
          price = Double.parseDouble(in.readLine());
      }
      ps.setDouble(1, price);

      int rowCount = ps.executeUpdate();
      if (rowCount == 0) {
          System.out.println("\nStorage of item ID " + id + " does not exist in your branch!");
      }
      con.commit();
    }

    private void displayItemInfo(int id) throws IOException, SQLException {
        int itemID = id;
        String     name;
        String     type;
        int        price;
        PreparedStatement  ps;
        ResultSet  rs;

        ps = con.prepareStatement("SELECT * FROM Item WHERE itemID = ?");
        ps.setInt(1, itemID);

        rs = ps.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();

        // get number of columns
        int numCols = rsmd.getColumnCount();

        System.out.println(" ");

        // display column names;
        for (int i = 0; i < numCols; i++)
        {
            // get column name and print it
            System.out.printf("%-15s", rsmd.getColumnName(i+1));
        }

        System.out.println(" ");

        while(rs.next())
        {
            // simplified output formatting; truncation may occur
            itemID = rs.getInt("itemID");
            System.out.printf("%-5s", itemID);

            name = rs.getString("name");
            System.out.printf("%-5s", name);

            price = rs.getInt("price");
            System.out.printf("%-5s\n", price);

            type = rs.getString("type");
            System.out.printf("%-5s", type);
        }
    }

    private void manageMembership(){
        // TODO:
    }
    private void manageDeal(){
        int     choice;
        boolean quit = false;

        try {
            while (!quit) {
                System.out.print("1.  Add new deal\n");
                System.out.print("2.  Display all deals\n");
                System.out.print("3.  Modify deal\n");
                System.out.print("4.  Go back\n>> ");

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        addNewDeal();
                        break;
                    case 2:
                        showAllDeals();
                        break;
                    case 3:
                        modifyDeal();
                }
            }
        }  catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    private void modifyDeal(){
        int     choice;
        boolean quit = false;

        try {
            while (!quit) {
                System.out.print("1.  Add item to deal\n");
                System.out.print("2.  delete deal\n");
                System.out.print("3.  delete item from deal\n");
                System.out.print("4.  modify deal name or duration\n>> ");
                System.out.println("5. modify deal percentage");


                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        addItemToDeal();
                        break;
                    case 2:
                        deleteDeal();
                        break;
                    case 3:
                        //deleteItemFromDeal();
                        break;
                    case 4:
                        //modifyDealNameOrDuration();
                        break;
                    case 5:
                        //modifyDealPercent();
                        break;
                }
            }
        }  catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addItemToDeal() throws IOException, SQLException {
        System.out.println("\n please enter the itemID you want to add to deal ");
        int itemID = searchItem();
        System.out.println("please enter deal name ");
        String  dealName = searchDeal();
        System.out.println("please enter percentage ");
        double persent = Double.parseDouble(in.readLine());
        PreparedStatement ps = con.prepareStatement("INSERT INTO Deal VALUES (?,?,?)");
        ps.setInt(1,itemID);
        ps.setString(2,dealName);
        ps.setDouble(3, persent);
        ps.executeUpdate();
        System.out.println("Success!!!");
        con.commit();
        ps.close();
    }
    private int searchItem() throws IOException, SQLException{
        boolean acc = false;
        int itemID = 0;
        while (!acc){
            System.out.print("ItemID: ");
            itemID = Integer.parseInt(in.readLine());
            Statement s = con.createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM Item WHERE itemID = " + itemID);
            if(!res.next()) {
                System.out.println("no such item please try again! ");
            } else {
                acc = true;
                return itemID;
            }
        }
        return  itemID;

    }

    private String searchDeal() throws IOException, SQLException {
        boolean acc = false;
        String dealName = null;
        while (!acc){
            System.out.print("dealName: ");
            dealName = in.readLine();
            PreparedStatement s = con.prepareStatement("SELECT * FROM Deal WHERE dealName = \'"+dealName+"\'");
            //s.setString(1, dealName);
            ResultSet res = s.executeQuery();
            if(!res.next()) {
                System.out.println("no such deal please try again! ");
            } else {
                acc = true;
                return dealName;
            }
        }
        return  dealName;

    }

    private void showAllDeals(){
        String     dealName;
        String     duration;
        int itemID;
        double percentage;

        Statement  stmt;
        ResultSet  rs;
        try
        {
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT d.dealName AS dealName, d.duration as duration, id.itemId as itemId, id.percentage as persentage FROM Deal d, ItemsInDeal id WHERE d.dealName = id.dealName");
            // get info on ResultSet
            ResultSetMetaData rsmd = rs.getMetaData();
            // get number of columns
            int numCols = rsmd.getColumnCount();
            System.out.println(" ");
            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it
                System.out.printf("%-15s", rsmd.getColumnName(i+1));
           }
            System.out.println(" ");
            while(rs.next())
            {
                // for display purposes get everything from Oracle
                // as a string
                // simplified output formatting; truncation may occur
                itemID = rs.getInt("itemId");
                System.out.printf("%-10.10s", itemID);
                dealName = rs.getString("dealName");
                System.out.printf("%-20.20s", dealName);
                duration = rs.getString("duration");
                System.out.printf("%-20.20s", duration);
                percentage = rs.getDouble("persentage");
                System.out.printf("%-15.15s\n", percentage);
            }
            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    private boolean checkValidDur(String duration){
        // TODO: check duration valid
        return true;
    }
    private void addNewDeal() {
        String dealName = null;
        String dur;

        int itemID;
        double percent;

        boolean canExit = false;
        PreparedStatement ps;

        while (!canExit) {
            try {
                ps = con.prepareStatement("INSERT INTO Deal VALUES (?,?)");
                System.out.print("\nDeal name: ");
                dealName = in.readLine();
                ps.setString(1, dealName);
                System.out.print("\nDuration: ");
                dur = in.readLine();
                ps.setString(2, dur);
                while (!checkValidDur(dur)) {
                    System.out.println("invalid duration format, please try again.");
                    dur = in.readLine();
                }
                ps.executeUpdate();
                // ps.close();
            } catch (IOException e) {
                System.out.println("IOException!");
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
                System.out.println("Insertion failed!");
                try {
                    // undo the insert
                    con.rollback();
                    System.exit(-1);
                } catch (SQLException ex2) {
                    System.out.println("Message: " + ex2.getMessage());
                    System.exit(-1);
                }
            }

            try {
                System.out.println("Do you want to add item in this deal? enter Y for yes");
                String ans = in.readLine();
                if (ans.equals("Y") || ans.equals("y")) {
                    System.out.print("please enter item id: ");
                    itemID = Integer.parseInt(in.readLine());
                    Statement s = con.createStatement();
                    ResultSet res = s.executeQuery("SELECT * FROM Item WHERE itemID = " + itemID);
                    if (!res.next()) {
                        System.out.println("no such item please try again! ");
                    } else {
                        System.out.println("item found, please enter persentage");
                        percent = Double.parseDouble(in.readLine());
                        ps = con.prepareStatement("INSERT INTO ItemsInDeal VALUES (?,?,?)");
                        ps.setInt(1, itemID);
                        ps.setString(2, dealName);
                        ps.setDouble(3, percent);
                        ps.executeUpdate();
                        ps.close();
                    }
//                    con.commit();
                    s.close();
                }
                System.out.println("do you want to add another deal? enter Y for yes");
                canExit = !in.readLine().equals("Y");
            } catch (SQLException se) {
                // TODO
            } catch (IOException ie) {
                System.out.println("IOException!");
                try {
                    con.close();
                    System.exit(-1);
                } catch (SQLException ex) {
                    System.out.println("Message: " + ex.getMessage());
                }
            }

        }
    }

    private void getReport() {

    }

    // private void getSalesRecord() throws IOException, SQLException{
    //     int receiptNumber;
    //     Time purchaseTime;
    //     java.sql.Date purchaseDate;
    //     int inputYear;
    //     int inputMonth;
    //     int inputDay;
    //     double totalPrice;
    //     int clerkID;
    //     int branchNumber;
    //
    //     PreparedStatement  ps;
    //     ResultSet  rs;
    //
    //     System.out.print("\nEnter starting year: ");
    //     inputYear = Integer.parseInt(in.readLine());
    //
    //     System.out.print("\nEnter starting Month: ");
    //     inputMonth = Integer.parseInt(in.readLine());
    //
    //     System.out.print("\nEnter starting Day: ");
    //     inputDay = Integer.parseInt(in.readLine());
    //
    //     Date startDate = new java.sql.Date(inputYear, inputMonth, inputDay);
    //
    //     System.out.print("\nEnter ending year: ");
    //     inputYear = Integer.parseInt(in.readLine());
    //
    //     System.out.print("\nEnter ending year: ");
    //     inputYear = Integer.parseInt(in.readLine());
    //
    //     System.out.print("\nEnter ending year: ");
    //     inputYear = Integer.parseInt(in.readLine());
    //
    //     Date endDate = new Date(inputYear, inputMonth, inputDay);
    //
    //     ps = con.prepareStatement("SELECT * FROM Purchase WHERE purchaseDate <= ? AND purchaseDate >= ? AND branchNumber = ?");
    //
    //     rs.setDate(1, startDate);
    //     rs.setDate(2, endDate);
    //     rs.setInt(3, branch);
    //
    //     // get info on ResultSet
    //     ResultSetMetaData rsmd = rs.getMetaData();
    //     // get number of columns
    //     int numCols = rsmd.getColumnCount();
    //
    //     System.out.println(" ");
    //
    //     // display column names;
    //     for (int i = 0; i < numCols; i++)
    //     {
    //       // get column name and print it
    //       System.out.printf("%-15s", rsmd.getColumnName(i+1));
    //     }
    //
    //     System.out.println(" ");
    //     while(rs.next()) {
    //       receiptNumber = rs.getInt("receiptNumber");
    //       System.out.printf("%-10.10s", receiptNumber);
    //
    //       purchaseDate = rs.getDate("purchaseDate");
    //       System.out.printf("%-20.20s", purchaseDate);
    //       purchaseTime = rs.getTime("purchaseTime");
    //       System.out.printf("%-20.20s", purchaseTime);
    //
    //       totalPrice = rs.getDouble("totalPrice");
    //       System.out.printf("%-15.15s", totalPrice);
    //
    //       clerkID = rs.getInt("clerkID");
    //       System.out.printf("%-15.15s", clerkID);
    //
    //       branchNumber = rs.getInt("branchNumber");
    //       System.out.printf("%-15.15s\n", branchNumber);
    //
    //         // close the statement;
    //         // the ResultSet will also be closed
    //         stmt.close();
    //     }
    // }

    private boolean isEarlier(String d1, String d2) {
        int month1 = Integer.parseInt(d1.substring(0,2));
        int month2 = Integer.parseInt(d2.substring(0,2));
        int day1 = Integer.parseInt(d1.substring(3));
        int day2 = Integer.parseInt(d2.substring(3));

        return month1 < month2 && day1 < day2;
      }


    private void addDeal() {
        // TODO
    }

    private void deleteDeal() throws IOException, SQLException {
        System.out.println("please enter the deal name you want to delete");
        String dName = searchDeal();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Deal WHRER DealName = \'"+dName+"\'");
        ps.executeUpdate();
        con.commit();
        System.out.println("delete successed!");
    }

}
