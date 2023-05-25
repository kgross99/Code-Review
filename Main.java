import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
    //ArrayList to store the items in the shopping list.
    private static ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    //The file path to save the shopping list to.
    private static final String SAVE_FILE_PATH = "saved_list.txt";
    //Scanner for reading input from the user.
    private static final Scanner inputScanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (Files.fileExists(SAVE_FILE_PATH)) {
            //If a file for a saved list already exists, load the saved list from that file.
            loadList();
        } else {
            //If there is not a list saved, create a save file for a new list.
            Files.createFile(SAVE_FILE_PATH);
            System.out.println("No previously saved shopping list found. Creating new list.");
        }

        while (true) {
            //Ask the user to enter an action.
            String input = askForString("\nEnter an action. Enter \"actions\" to list all actions.");
            System.out.println();
            //Call a method depending on which action the user entered.
            switch (input) {
                case "list":
                    listItems();
                    break;
                case "update":
                    updateItemInformation();
                    break;
                case "add":
                    addItem();
                    break;
                case "remove":
                    removeItem();
                    break;
                case "cost":
                    printTotalCost();
                    break;
                case "clear":
                    clearList();
                    break;
                case "save":
                    saveList();
                    //End the program after the list has been saved.
                    return;
                case "actions":
                    listActions();
                    break;
                default:
                    //Tell the user if they enter an unknown action.
                    System.out.println("Unknown action \"" + input + "\".");
                    break;
            }
        }
    }

    //Method to list all actions the user can enter and what they do.
    private static void listActions() {
        System.out.println("list - List all items in the shopping list.");
        System.out.println("add - Add a new item to the list.");
        System.out.println("remove - Remove an item from the list.");
        System.out.println("update - Update information for an item in the list.");
        System.out.println("cost - Calculate the total cost of every item in the shopping list.");
        System.out.println("clear - Clear all items from the list.");
        System.out.println("save - Save the shopping list to a file and close the program.");
    }

    //Method to list all items in the shopping list
    private static void listItems() {
        //Tell the user if the list is empty and return from the method.
        if (shoppingItems.isEmpty()) {
            System.out.println("There are no items in this shopping list.");
            return;
        }
        //If the list is not empty, print each item in the list.
        shoppingItems.forEach(shoppingItem -> {
            System.out.printf("%s. Item: %s   Price: $%.2f%n", shoppingItems.indexOf(shoppingItem) + 1, shoppingItem.getName(), shoppingItem.getPrice());
        });
    }

    //Method to update information for an item in the shopping list.
    private static void updateItemInformation() {
        //Tell the user if the list is empty and return from the method.
        if (shoppingItems.isEmpty()) {
            System.out.println("There are no items in this shopping list to update.");
            return;
        }
        //Ask the user for the index of the item to modify and make sure that it is a valid index in the list.
        int indexToModify = askForAndValidateInt("Enter index of item to update its information.", "Please enter a valid integer.", index -> index > 0 && index <= shoppingItems.size(), "There is no item at this index.");
        //Get the item to modify from the shopping list.
        ShoppingItem itemToModify = shoppingItems.get(indexToModify - 1);
        //Ask whether the user wants to modify this item's name or price.
        switch (askForString("Enter \"name\" to update this item's name or \"price\" to update this item's price.")) {
            case "name":
                //Set the item's name to the inputted string.
                itemToModify.setName(askForString("Enter updated name."));
                break;
            case "price":
                //Set the item's price to the inputted float.
                itemToModify.setPrice(askForFloat("Enter updated price.", "Please enter a valid float."));
                break;
            default:
                //Tell the user if they inputted an invalid option.
                System.out.println("Invalid option.");
                break;
        }
    }

    //Method to add a new item to the shopping list.
    private static void addItem() {
        //Ask for the name of the new item from the user.
        String name = askForString("Enter name of item.");
        //Ask for the price of the new item from the user.
        float price = askForFloat("Enter price of item.", "Please enter a valid float.");
        //Add the new item to the list.
        addItemToList(name, price);
        System.out.println("Added item to list.");
    }

    //Method to add an item to the list of items.
    private static void addItemToList(String name, float price) {
        shoppingItems.add(new ShoppingItem(name, price));
    }

    //Method to remove an item from the shopping list.
    private static void removeItem() {
        //Tell the user if the list is empty and return from the method.
        if (shoppingItems.isEmpty()) {
            System.out.println("There are no items in this shopping list to remove.");
            return;
        }
        //Ask the user for the index of the item to remove and make sure that it is a valid index in the list.
        int removeIndex = askForAndValidateInt("Enter index of item to remove.", "Please enter a valid integer.", index -> index > 0 && index <= shoppingItems.size(), "There is no item at this index.");
        //Remove the item from the list.
        shoppingItems.remove(removeIndex - 1);
        System.out.println("Removed item " + removeIndex + " from list.");
    }

    //Method to calculate and print the total cost of the shopping list.
    private static void printTotalCost() {
        //Variable to store the total cost of the list.
        float totalCost = 0;
        //Loop for each ShoppingItem in the list.
        for (ShoppingItem shoppingItem : shoppingItems) {
            //Add the item's price to the total cost.
            totalCost += shoppingItem.getPrice();
        }
        //Print the total cost of the shopping list.
        System.out.printf("The total cost of this list is $%.2f%n", totalCost);
    }

    //Method to remove all items from the shopping list.
    private static void clearList() {
        //Tell the user if the list is already empty and return from the method.
        if (shoppingItems.isEmpty()) {
            System.out.println("This shopping list is already empty.");
            return;
        }
        //Set the shoppingItems list to an empty ArrayList.
        shoppingItems = new ArrayList<>();
        System.out.println("Cleared all items from this shopping list.");
    }

    //Method to load the shopping list from a file.
    private static void loadList() {
        //Get a BufferedReader for the file to load from.
        try (BufferedReader saveFileReader = Files.getFileReader(new File(SAVE_FILE_PATH))) {
            //Variables to hold the name and price of the current item that is being read.
            String itemName;
            String itemPrice;
            //Repeat while there are still lines in the file.
            while ((itemName = saveFileReader.readLine()) != null && (itemPrice = saveFileReader.readLine()) != null) {
                //Add the item to the shopping list.
                addItemToList(itemName, Float.parseFloat(itemPrice));
            }
        } catch (Exception e) {
            //Print if an error occurred while loading the list.
            System.out.println("An error occurred while loading the saved list.");
            return;
        }
        System.out.println("Loaded saved shopping list from \"" + SAVE_FILE_PATH + "\".");
    }
    
    //Method to save the shopping list to a file.
    private static void saveList() {
        //Get a BufferedWriter for the file to save to.
        try (BufferedWriter saveFileWriter = Files.getFileWriter(new File(SAVE_FILE_PATH))) {
            //For each ShoppingItem in the list, write the item's name and price to the file.
            for (ShoppingItem shoppingItem : shoppingItems) {
                saveFileWriter.write(shoppingItem.getName() + "\n");
                saveFileWriter.write(shoppingItem.getPrice() + "\n");
            }
        } catch (IOException e) {
            //Print if an error occurred while writing to the file.
            System.out.println("An error occurred while saving the list.");
        }
        System.out.println("Saved shopping list to \"" + SAVE_FILE_PATH + "\".");
    }

    //Method to ask for a String from the user.
    private static String askForString(String message) {
        //Print the message to tell the user what to input.
        System.out.println(message);
        //Return the String inputted by the user.
        return inputScanner.nextLine();
    }

    //Method to ask for an int from the user and to make sure the inputted int passes a condition that is passed into this method.
    private static int askForAndValidateInt(String message, String invalidInputMessage, Predicate<Integer> condition, String conditionNotPassedMessage) {
        //Print the message to tell the user what to input.
        System.out.println(message);
        //Repeat until the user's input is a valid int and it passes the condition.
        while (true) {
            try {
                //Store the int that the user inputted.
                int integer = Integer.parseInt(inputScanner.nextLine());
                //Test the int against the condition.
                if (condition.test(integer)) {
                    //If the int passes the condition, return it.
                    return integer;
                } else {
                    //If it does not pass the condition, tell the user.
                    System.out.println(conditionNotPassedMessage);
                }
            } catch (NumberFormatException e) {
                //If the input is not a valid int, tell the user.
                System.out.println(invalidInputMessage);
            }
        }
    }

    //Method to ask for a float from the user.
    private static float askForFloat(String message, String invalidInputMessage) {
        //Print the message to tell the user what to input.
        System.out.println(message);
        //Repeat until the user's input is a valid float.
        while (true) {
            try {
                //Return the float that the user inputted.
                return Float.parseFloat(inputScanner.nextLine());
            } catch (NumberFormatException e) {
                //If the input is not a valid float, tell the user.
                System.out.println(invalidInputMessage);
            }
        }
    }
}
