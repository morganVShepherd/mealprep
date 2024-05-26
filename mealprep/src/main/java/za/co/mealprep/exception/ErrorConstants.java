package za.co.mealprep.exception;

public class ErrorConstants {
    public static String ERROR = "ERROR";
    public static String CANNOT_BE_EMPTY = "Should not be null/empty";
    public static String MUST_BE_POSITIVE = "Should not be a negative number";
    public static String INCORRECT_LENGTH = "Is not a valid length";
    public static Error UNEXPECTED_EXCEPTION = new Error("Something strange happened",
            "Please contact support",1);
    public static Error ALREADY_EXISTS = new Error("Already Exists",
            "Cannot create as it already exists",2);
    public static Error DOES_NOT_EXIST = new Error("Cannot update an Entity that does not exist",
            "Cannot edit an entity that hasn't been saved",3);
    public static Error DOES_EXIST = new Error("Cannot update an Entity to an Entity that already Exists",
            "Cannot be updated to one that already exists",4);
    public static Error EMPTY_OPTIONAL = new Error("Entity Cannot be found with Request criteria",
            "No results for your search",5);
    public static Error NEEDS_PARENT = new Error("This Entity cannot be created with a Parent",
            "Please check that this Item is linked to another",6);
    public static Error NEED_DATA = new Error("Need Data",
            "You do not have enough recipes saved to fulfil this request",7);
}