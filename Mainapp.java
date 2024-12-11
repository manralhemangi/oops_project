package oops.project;

import java.util.*;
import java.util.function.Predicate;

// Enums for Book Status
enum BookStatus {
    AVAILABLE, BORROWED
}

// Record for immutable User
record User(String name, int userId) {
    @Override
    public String toString() {
        return "User: " + name + " (ID: " + userId + ")";
    }
}

// Base class for Library Items
abstract class LibraryItem {
    private final int id;
    private final String title;

    public LibraryItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

// Book class inheriting LibraryItem and implementing an interface
class Book extends LibraryItem {
    private String author;
    private BookStatus status;

    // Constructor with method overloading
    public Book(int id, String title, String author) {
        super(id, title);
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public Book(int id, String title) {
        this(id, title, "Unknown");
    }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void borrowBook() {
        if (isAvailable()) {
            status = BookStatus.BORROWED;
            System.out.println(getTitle() + " has been borrowed.");
        } else {
            System.out.println(getTitle() + " is already borrowed.");
        }
    }

    public void returnBook() {
        if (status == BookStatus.BORROWED) {
            status = BookStatus.AVAILABLE;
            System.out.println(getTitle() + " has been returned.");
        } else {
            System.out.println(getTitle() + " was not borrowed.");
        }
    }

    @Override
    public String toString() {
        return getId() + " - " + getTitle() + " by " + author + (isAvailable() ? " [Available]" : " [Borrowed]");
    }
}

// Interface for library operations
interface LibraryOperations {
    void addItem(LibraryItem item);
    void listItems();
}

// Library class implementing operations with encapsulation
class Library implements LibraryOperations {
    private final List<Book> books = new ArrayList<>();

    @Override
    public void addItem(LibraryItem item) {
        if (item instanceof Book book) {
            boolean idExists = books.stream().anyMatch(b -> b.getId() == book.getId());
            if (idExists) {
                System.out.println("Error: Book with ID " + book.getId() + " already exists.");
            } else {
                books.add(book);
                System.out.println("Added: " + book);
            }
        }
    }

    @Override
    public void listItems() {
        books.forEach(System.out::println);
    }

    public Optional<Book> findBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    public void borrowBook(int id) {
        findBookById(id).ifPresentOrElse(Book::borrowBook, () -> System.out.println("Book not found."));
    }

    public void returnBook(int id) {
        findBookById(id).ifPresentOrElse(book -> {
            if (!book.isAvailable()) {
                book.returnBook();
            } else {
                System.out.println("Book was not borrowed.");
            }
        }, () -> System.out.println("Book not found."));
    }
}

public class Mainapp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Demonstrate method references and lambdas
        Predicate<Book> isAvailable = Book::isAvailable;

        while (true) {
            System.out.println("\nLibrary Management System:");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book ID: ");
                    int id = scanner.nextInt();
                    library.addItem(new Book(id, title, author));
                }
                case 2 -> library.listItems();
                case 3 -> {
                    System.out.print("Enter book ID to borrow: ");
                    int borrowId = scanner.nextInt();
                    library.borrowBook(borrowId);
                }
                case 4 -> {
                    System.out.print("Enter book ID to return: ");
                    int returnId = scanner.nextInt();
                    library.returnBook(returnId);
                }
                case 5 -> {
                    System.out.println("Exiting Library Management System. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
