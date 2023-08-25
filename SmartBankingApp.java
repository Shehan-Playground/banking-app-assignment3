import java.util.Scanner;

public class SmartBankingApp {

    final static String BLUE_COLOR = "\033[034;1m";
    final static String RED_COLOR = "\033[031;1m";
    final static String GREEN_COLOR = "\033[032;1m";
    final static String RESET = "\033[0m";
    final static String CLEAR = "\033[H\033[2J";
    final static String ERROR_MSG = String.format("\n\t%s%s%s\n", RED_COLOR,"%s",RESET);
    final static String SUCCESS_MSG = String.format("\n\t%s%s%s\n", GREEN_COLOR,"%s",RESET);
    final static String APP_MARGIN = String.format("\n\t%s%s%s\n", BLUE_COLOR,"%s",RESET);

    final static String CONTINUE_MESSAGE = "\n\tDo you want to continue (Y/n)? ";
    final static String EXITAPP_MESSAGE = "\n\tAre you sure you want to exit from App (Y/n)? ";
    final static String NEWCUSTOMER_MESSAGE = "\n\tDo you want to add another new customer (Y/n)? ";

    final static String DASHBOARD = "💰 Welcome to Smart Banking App";
    final static String OPEN_ACCOUNT = "Open New Account";
    final static String DEPOSIT_MONEY = "Deposit Money";
    final static String WITHDRAW_MONEY = "Withdraw Money";
    final static String TRANSFER_MONEY = "Transfer Money";
    final static String CHECK_BALANCE = "Check Account Balance";
    final static String DROP_ACCOUNT = "Drop Existing Account";
    final static String EXIT = "Exit App";

    static String[][] accounts = new String[0][3];
    // static String[][] accounts = {{"1","Shehan Rathnayake","5000"},{"2","Kamal Fernando","14000"},{"3","Saman Kanchana","45000"}};
    static int selectedIndex;

    private static Scanner scanner = new Scanner (System.in);
    public static void main(String[] args) {

        dashboard();
        
    }



    public static void dashboard() {

        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, DASHBOARD);

        System.out.printf("\n\t[1]. %s\n\t[2]. %s\n\t[3]. %s\n\t[4]. %s\n\t[5]. %s\n\t[6]. %s\n\t[7]. Exit\n", OPEN_ACCOUNT,DEPOSIT_MONEY,WITHDRAW_MONEY,TRANSFER_MONEY,CHECK_BALANCE,DROP_ACCOUNT);
        System.out.print("\n\n\tEnter an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        System.out.println(RESET);
        System.out.println();
        System.out.println();

        switch (option) {
            case 1: openAccount(); break;
            case 2: deposit(); break;
            case 3: withdraw(); break;
            case 4: transfer(); break;
            case 5: balance(); break;
            case 6: accountDrop(); break;
            case 7: exit(); break;
            default:
        }
    }



    public static void openAccount() {

        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, OPEN_ACCOUNT);

        // Generating account id
        int accountId = generateAccountID();;
        
        System.out.printf("\n\t[1]. Account ID: SDB-%05d", accountId);

        //Enter Account name
        String accountName = accountName();
        if (accountName == "") {
            dashboard();
            return;
        }
        

        //Enter initial deposit

        double initialDeposit = initialDeposit();
        if (initialDeposit == 0.0) {
            dashboard();
            return;
        }

        createAccount(accountId, accountName, initialDeposit);
        
        if (isContinue(NEWCUSTOMER_MESSAGE)) openAccount();
        else dashboard();
    }



    public static void deposit() {

        //Account deposit process
        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, DEPOSIT_MONEY);

        if (!callAccount("\b")) {
            dashboard();
            return;
        }
        checkBalance(selectedIndex);

        if (!accountDeposit()) {
            dashboard();
            return;
        }

        System.out.printf(SUCCESS_MSG,"Transaction Successfull");
        checkBalance(selectedIndex);

        if (isContinue(CONTINUE_MESSAGE)) deposit();
        else dashboard();
    }



    public static void withdraw () {

        //Account withdraw process
        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, WITHDRAW_MONEY);

        if (!callAccount("\b")) {
            dashboard();
            return;
        }
        checkBalance(selectedIndex);

        if (!accountWithdraw()) {
            dashboard();
            return;
        }

        System.out.printf(SUCCESS_MSG,"Transaction Successfull");
        checkBalance(selectedIndex);

        if (isContinue(CONTINUE_MESSAGE)) withdraw();
        else dashboard();
    }



    public static void transfer() {

        // Money transfer process
        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, TRANSFER_MONEY);

        if (!callAccount("From")) {
            dashboard();
            return;
        }
        int fromAccountIndex = selectedIndex;
        checkBalance(selectedIndex);

        if (!callAccount("To")) {
            dashboard();
            return;
        }

        checkBalance(selectedIndex);

        if (!accountTransfer(fromAccountIndex)) {
            dashboard();
            return;
        }

        //Successfull message and displaying balance
        System.out.printf(SUCCESS_MSG,"Transaction Successfull");

        System.out.println("\n\tFrom Account: ");
        checkBalance(fromAccountIndex);

        System.out.println("\n\tTo Account: ");
        checkBalance(selectedIndex);

        if (isContinue(CONTINUE_MESSAGE)) transfer();
        else dashboard();
    }
    


    public static void balance() {

        // Displaying balance process
        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, CHECK_BALANCE);

        if (!callAccount("\b")) {
            dashboard();
            return;
        }
        checkBalance(selectedIndex);

        double availableBalance = Double.parseDouble(accounts[selectedIndex][2])-500;
        System.out.printf("\tAvailable balance for withdrawal: Rs.%.2f", availableBalance);
        System.out.println();

        if (isContinue(CONTINUE_MESSAGE)) balance();
        else dashboard();
    }



    public static void accountDrop() {

        // Account delete process
        System.out.println(CLEAR);
        System.out.printf(APP_MARGIN, DROP_ACCOUNT);

        if (!callAccount("\b")) {
            dashboard();
            return;
        }
        checkBalance(selectedIndex);

        if (isContinue("\n\tAre you sure you want to delete (Y/n)? ")) deleteAccount();
        else {
            dashboard();
            return;
        }

        if (isContinue(CONTINUE_MESSAGE)) accountDrop();
        else dashboard();
    }



    public static void exit() {

        // Program exit process
        if (isContinue(EXITAPP_MESSAGE)) System.exit(0);

        else dashboard();
    }



    public static boolean isContinue(String statement) {
        
        System.out.print(statement);
        String answer = scanner.nextLine().strip().toUpperCase();

        if (answer.equals("Y")) return true;
        else return false;
    }



    public static int generateAccountID() {
        if (accounts.length == 0) return 1;
        else return Integer.parseInt(accounts[accounts.length-1][0]) + 1;
    }



    public static String accountName() {
        boolean valid;
        String accountName;

        do {
            valid = true;
            System.out.printf("\n\t[2]. Enter Customer name: ");
            accountName = scanner.nextLine().strip();

            if (!accountNameValidate(accountName)) {
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return "";
            }

        } while (!valid);

        return accountName;
    }



    public static boolean accountNameValidate(String accountName) {
        //checking blank content
        if (accountName.isBlank()){
            System.out.printf(ERROR_MSG,"Customer name cannot be empty");
            return false;
        }

        //Checking validity of Account name
        for (int i = 0; i < accountName.length(); i++) {
            if (!(Character.isLetter(accountName.charAt(i)) )) {
                if (!(Character.isSpaceChar(accountName.charAt(i)))) {
                    System.out.printf(ERROR_MSG, "Invalid account name");
                    return false;
                }
            }
        }
        return true;
    }



    public static double initialDeposit() {
        boolean valid;
        double initialDeposit;
        do {
            valid = true;
            System.out.print("\t[3]. Enter Initial Deposit: Rs. ");
            initialDeposit = scanner.nextDouble();
            scanner.nextLine();

            //Validating initial deposit
            if (initialDeposit < 5000.00) {
                System.out.printf(ERROR_MSG, "Minimum initial deposit Rs.5000.00");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return 0.0;
            }
            
        } while (!valid);

        return initialDeposit;
    }



    public static boolean accountNumberValidate(String accountNumber) {

        //Checking empty input
        if (accountNumber.isBlank()) {
            System.out.printf(ERROR_MSG,"Account number cannot be empty");
            return false;
        }

        // Checking format
        if (accountNumber.length() != 9 || !accountNumber.startsWith("SDB-")) {
            System.out.printf(ERROR_MSG,"Invalid format");
            return false;
        }

        //Checking digits
        String numberString = accountNumber.substring(4);
        for (int i = 0; i < numberString.length(); i++) {
            if(!Character.isDigit(numberString.charAt(i))) {
                System.out.printf(ERROR_MSG,"Invalid format");
                return false;
            }
        }

        //Checking existance
        int i;
        
        for (i = 0; i < accounts.length; i++) {
            if (Integer.parseInt(accountNumber.substring(4)) == Integer.parseInt(accounts[i][0])) {
                selectedIndex = i;
                return true;
            }
        }
        System.out.printf(ERROR_MSG,"Not found");
        return false;
    }



    public static boolean callAccount(String prefix) {
        boolean valid;
        do {
            
            valid = true;
            System.out.printf("\n\tEnter%s%s%sA/C No: "," ",prefix," ");
            String accountNumber = scanner.nextLine();

            if (!accountNumberValidate(accountNumber)) {
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            } 
        } while (!valid);

        return true;
    }



    public static boolean accountDeposit() {
        boolean valid;
        double depositAmount;
        do {
            valid = true;
            System.out.print("\n\tEnter Deposit Amount: ");
            depositAmount = scanner.nextDouble();
            scanner.nextLine();

            if (depositAmount < 500) {
                System.out.printf(ERROR_MSG, "Minimum depost amount is Rs.500.00");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }
        } while (!valid);

        accounts[selectedIndex][2] = (Double.parseDouble(accounts[selectedIndex][2])+depositAmount)+"";
        return true;
    }



    public static boolean accountWithdraw() {
        boolean valid;
        double withdrawAmount;
        do {
            valid = true;
            System.out.print("\n\tEnter Withdraw Amount: ");
            withdrawAmount = scanner.nextDouble();
            scanner.nextLine();

            if (withdrawAmount < 100) {
                System.out.printf(ERROR_MSG, "Minimum withdrawal amount is Rs.100.00");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

            if (Double.parseDouble(accounts[selectedIndex][2])-withdrawAmount < 500) {
                System.out.printf(ERROR_MSG, "Insufficient account balance");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

        } while (!valid);

        accounts[selectedIndex][2] = (Double.parseDouble(accounts[selectedIndex][2])-withdrawAmount)+"";
        return true;
    }



    public static boolean accountTransfer(int fromAccount) {

        boolean valid;
        double transferAmount;
        double feeAmount = 0;

        do {
            valid = true;
            System.out.print("\n\tEnter Transfer Amount: ");
            transferAmount = scanner.nextDouble();
            scanner.nextLine();

            if (transferAmount < 100) {
                System.out.printf(ERROR_MSG, "Minimum transferable amount is Rs.100.00");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

            feeAmount = transferAmount * 2 /100;
            if (Double.parseDouble(accounts[fromAccount][2])-transferAmount-feeAmount < 500) {
                System.out.printf(ERROR_MSG, "Insufficient account balance");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

        } while (!valid);
        
        accounts[fromAccount][2] = (Double.parseDouble(accounts[fromAccount][2])-transferAmount-feeAmount)+"";
        accounts[selectedIndex][2] = (Double.parseDouble(accounts[selectedIndex][2])+transferAmount)+"";

        return true;
    }

    
    
    public static void checkBalance(int index) {

        System.out.printf("\n\tName: %s",accounts[index][1]);
        System.out.printf("\n\tCurrent Balance: Rs.%.2f\n", Double.parseDouble(accounts[index][2]));
    }

    
    
    public static void deleteAccount() {

        // Deleting data from the array
        String[][] newAccounts = new String[accounts.length-1][3];

        for (int index = 0, i = 0; index < newAccounts.length; index++, i++) {

            if (index == selectedIndex) i++;
            newAccounts[index][0] = accounts[i][0];
            newAccounts[index][1] = accounts[i][1];
            newAccounts[index][2] = accounts[i][2];
        }

        int accountId = Integer.parseInt(accounts[selectedIndex][0]);
        String accountName = accounts[selectedIndex][1];
        accounts = newAccounts;

        System.out.printf(SUCCESS_MSG,String.format("Account number SDB-%05d for %s has been deleted successfully.", accountId, accountName));
    }



    public static void createAccount(int accountId, String accountName, double initialDeposit) {

        // Storing data into arrays
        String[][] newAccounts = new String[accounts.length+1][3];

        int index;
        for (index = 0; index < accounts.length; index++) {
            newAccounts[index][0] = accounts[index][0];
            newAccounts[index][1] = accounts[index][1];
            newAccounts[index][2] = accounts[index][2];
        }

        newAccounts[index][0] = accountId+"";
        newAccounts[index][1] = accountName;
        newAccounts[index][2] = initialDeposit+"";

        accounts = newAccounts;

        // Success message & Asking to add another customer
        System.out.printf(SUCCESS_MSG,String.format("Account number SDB-%05d for %s has been created successfully.", accountId, accountName));

    }

}