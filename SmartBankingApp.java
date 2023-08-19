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

    // static String[][] accounts = new String[0][3];
    static String[][] accounts = {{"1","Shehan rathnayake","5000"},{"2","Kamal Fernando","14000"},{"3","Saman Kanchana","45000"}};
    static int selectedIndex;

    private static Scanner scanner = new Scanner (System.in);
    public static void main(String[] args) {
        final String DASHBOARD = "💰 Welcome to Smart Banking App";
        final String OPEN_ACCOUNT = "Open New Account";
        final String DEPOSIT_MONEY = "Deposit Money";
        final String WITHDRAW_MONEY = "Withdraw Money";
        final String TRANSFER_MONEY = "Transfer Money";
        final String CHECK_BALANCE = "Check Account Balance";
        final String DROP_ACCOUNT = "Drop Existing Account";
        final String EXIT = "Exit App";

        

        String screen = DASHBOARD;

        do {
            final String APPTITLE = screen;
            System.out.println(CLEAR);
            System.out.printf(APP_MARGIN, APPTITLE);

            mainSwitch:
            switch (screen){
                case DASHBOARD:
                    System.out.printf("\n\t[1]. %s\n\t[2]. %s\n\t[3]. %s\n\t[4]. %s\n\t[5]. %s\n\t[6]. %s\n\t[7]. Exit\n", OPEN_ACCOUNT,DEPOSIT_MONEY,WITHDRAW_MONEY,TRANSFER_MONEY,CHECK_BALANCE,DROP_ACCOUNT);
                    System.out.print("\n\n\tEnter an option: ");
                    int option = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(RESET);
                    System.out.println();
                    System.out.println();

                    switch (option) {
                        case 1: screen = OPEN_ACCOUNT; break;
                        case 2: screen = DEPOSIT_MONEY; break;
                        case 3: screen = WITHDRAW_MONEY; break;
                        case 4: screen = TRANSFER_MONEY; break;
                        case 5: screen = CHECK_BALANCE; break;
                        case 6: screen = DROP_ACCOUNT; break;
                        case 7: screen = EXIT; break;
                        default:
                    }
                    break;

                case OPEN_ACCOUNT:

                    boolean valid;

                    // Generating account id
                    int accountId;
                    if (accounts.length == 0) accountId = 1;
                    else accountId = Integer.parseInt(accounts[accounts.length-1][0]) + 1;
                    System.out.printf("\n\t[1]. Account ID: SDB-%05d", accountId);

                    //Enter Account name
                    String accountName;

                    accountNameLoop:
                    do {
                        valid = true;
                        System.out.printf("\n\t[2]. Enter Customer name: ");
                        accountName = scanner.nextLine().strip();

                        //checking blank content
                        if (accountName.isBlank()){
                            System.out.printf(ERROR_MSG,"Customer name cannot be empty");
                            valid = false;

                            if (isContinue(CONTINUE_MESSAGE)) continue;
                            else {
                                screen = DASHBOARD;
                                break mainSwitch;
                            }

                        }

                        //Checking validity of Account name
                        for (int i = 0; i < accountName.length(); i++) {
                            if (!(Character.isLetter(accountName.charAt(i)) )) {
                                if (!(Character.isSpaceChar(accountName.charAt(i)))) {
                                    System.out.printf(ERROR_MSG, "Invalid account name");
                                    valid = false;

                                    if (isContinue(CONTINUE_MESSAGE)) continue accountNameLoop;
                                    else {
                                        screen = DASHBOARD;
                                        break mainSwitch;
                                    }
                                }
                            }
                        }

                    } while (!valid);

                    //Enter initial deposit
                    double accountBalance;
                    do {
                        valid = true;
                        System.out.print("\t[3]. Enter Initial Deposit: Rs. ");
                        accountBalance = scanner.nextDouble();
                        scanner.nextLine();
        
                        //Validating initial deposit
                        if (accountBalance < 5000.00) {
                            System.out.printf(ERROR_MSG, "Minimum initial deposit Rs.5000.00");
                            valid = false;

                            if (isContinue(CONTINUE_MESSAGE)) continue;
                            else {
                                screen = DASHBOARD;
                                break mainSwitch;
                            }
                        }
                        
                    } while (!valid);

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
                    newAccounts[index][2] = accountBalance+"";

                    accounts = newAccounts;

                    // Success message & Asking to add another customer
                    System.out.printf(SUCCESS_MSG,String.format("Account number SDB-%05d for %s has been created successfully.", accountId, accountName));

                    if (isContinue(NEWCUSTOMER_MESSAGE)) continue;
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                case DEPOSIT_MONEY:

                    if (!callAccount("\b")) {
                        screen = DASHBOARD;
                        break;
                    }
                    checkBalance();

                    if (!accountDeposit()) {
                        screen = DASHBOARD;
                        break;
                    }

                    System.out.printf(SUCCESS_MSG,"Transaction Successfull");
                    checkBalance();

                    if (isContinue(CONTINUE_MESSAGE)) continue;
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                case WITHDRAW_MONEY:

                    if (!callAccount("\b")) {
                        screen = DASHBOARD;
                        break;
                    }
                    checkBalance();

                    if (!accountWithdraw()) {
                        screen = DASHBOARD;
                        break;
                    }

                    System.out.printf(SUCCESS_MSG,"Transaction Successfull");
                    checkBalance();

                    if (isContinue(CONTINUE_MESSAGE)) continue;
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                case TRANSFER_MONEY:

                    if (!callAccount("From")) {
                        screen = DASHBOARD;
                        break;
                    }
                    int fromAccountIndex = selectedIndex;
                    checkBalance();

                    if (!callAccount("To")) {
                        screen = DASHBOARD;
                        break;
                    }

                    checkBalance();

                    if (!accountTransfer(fromAccountIndex)) {
                        screen = DASHBOARD;
                        break;
                    }

                    System.out.printf(SUCCESS_MSG,"Transaction Successfull");
                    checkBalance();

                    if (isContinue(CONTINUE_MESSAGE)) continue;
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                case CHECK_BALANCE:

                    if (!callAccount("\b")) {
                        screen = DASHBOARD;
                        break;
                    }
                    checkBalance();

                    if (isContinue(CONTINUE_MESSAGE)) continue;
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                case EXIT:

                    if (isContinue(EXITAPP_MESSAGE)) System.exit(0);
                    else {
                        screen = DASHBOARD;
                        break;
                    }

                default: screen = DASHBOARD;
            }
                
 
        } while (true);

    }
    public static boolean isContinue(String statement) {
        
        System.out.print(statement);
        String answer = scanner.nextLine().strip().toUpperCase();
        // System.out.println();
        if (answer.equals("Y")) return true;
        else return false;
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

        accounts[selectedIndex][2] = (Integer.parseInt(accounts[selectedIndex][2])+depositAmount)+"";
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

            if (Integer.parseInt(accounts[selectedIndex][2])-withdrawAmount < 500) {
                System.out.printf(ERROR_MSG, "Insufficient account balance");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

        } while (!valid);

        accounts[selectedIndex][2] = (Integer.parseInt(accounts[selectedIndex][2])-withdrawAmount)+"";
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
            if (Integer.parseInt(accounts[fromAccount][2])-transferAmount-feeAmount < 500) {
                System.out.printf(ERROR_MSG, "Insufficient account balance");
                valid = false;
                if (isContinue(CONTINUE_MESSAGE)) continue;
                else return false;
            }

        } while (!valid);

        accounts[selectedIndex][2] = (Integer.parseInt(accounts[selectedIndex][2])+transferAmount)+"";
        accounts[fromAccount][2] = (Integer.parseInt(accounts[fromAccount][2])-transferAmount-feeAmount)+"";
        return true;
    }

    public static void checkBalance() {
        System.out.printf("\n\tName: %s",accounts[selectedIndex][1]);
        System.out.printf("\n\tCurrent Balance: Rs.%.2f\n", Double.parseDouble(accounts[selectedIndex][2]));
    }


}