//ty miller

import java.io.*;
import java.util.Scanner;

public class TaskOrganizer {
    // Task class
    private static class Task {
        private String action;
        private int priority;
        
        public Task() {
            this.action = "none";
            this.priority = 4;
        }
        
        public Task(String action, int priority) {
            this.action = action != null ? action : "none";
            this.priority = (priority >= 0 && priority <= 4) ? priority : 4;
        }
        
        public String getAction() {
            return action;
        }
        
        public int getPriority() {
            return priority;
        }
        
        @Override
        public String toString() {
            return "[Task] Priority: " + priority + " Task: " + action;
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Task) {
                Task other = (Task) obj;
                return this.priority == other.priority && 
                       this.action.equals(other.action);
            }
            return false;
        }
    }
    
    // Generic Linked List class
    private static class GenLL<T> {
        private class Node {
            T data;
            Node next;
            
            Node(T data) {
                this.data = data;
                this.next = null;
            }
        }
        
        private Node head;
        private int size;
        
        public GenLL() {
            head = null;
            size = 0;
        }
        
        public void add(T item) {
            Node newNode = new Node(item);
            if(head == null) {
                head = newNode;
            } else {
                Node current = head;
                while(current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }
        
        public boolean remove(T item) {
            if(head == null) return false;
            
            if(head.data.equals(item)) {
                head = head.next;
                size--;
                return true;
            }
            
            Node current = head;
            while(current.next != null) {
                if(current.next.data.equals(item)) {
                    current.next = current.next.next;
                    size--;
                    return true;
                }
                current = current.next;
            }
            return false;
        }
        
        public boolean contains(T item) {
            Node current = head;
            while(current != null) {
                if(current.data.equals(item)) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node current = head;
            while(current != null) {
                sb.append(current.data.toString()).append("\n");
                current = current.next;
            }
            return sb.toString();
        }
        
        public Node getHead() {
            return head;
        }
    }
    
    // TaskOrganizer class members and methods
    private GenLL<Task>[] organizedTasks;
    
    @SuppressWarnings("unchecked")
    public TaskOrganizer() {
        organizedTasks = new GenLL[5];
        for(int i = 0; i < organizedTasks.length; i++) {
            organizedTasks[i] = new GenLL<Task>();
        }
    }
    
    public void addTask(Task task) {
        if(!organizedTasks[task.getPriority()].contains(task)) {
            organizedTasks[task.getPriority()].add(task);
            //System.out.println("Task added successfully!");
        } else {
            //System.out.println("Task already exists!");
        }
    }
    
    public void removeTask(Task task) {
        if(organizedTasks[task.getPriority()].remove(task)) {
            System.out.println("Task removed successfully!");
        } else {
            System.out.println("Task not found!");
        }
    }
    
    public void printTasks() {
        for(int i = 0; i < organizedTasks.length; i++) {
            System.out.print(organizedTasks[i].toString());
        }
    }
    
    public void readTaskFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Clear existing tasks
            for(int i = 0; i < organizedTasks.length; i++) {
                organizedTasks[i] = new GenLL<Task>();
            }
            
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if(parts.length == 2) {
                    try {
                        int priority = Integer.parseInt(parts[0]);
                        String action = parts[1];
                        addTask(new Task(action, priority));
                    } catch(NumberFormatException e) {
                        // Skip invalid lines
                    }
                }
            }
            //System.out.println("File read successfully!");
        } catch(IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    public void writeTaskFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for(GenLL<Task> list : organizedTasks) {
                GenLL<Task>.Node current = list.getHead();
                while(current != null) {
                    Task task = current.data;
                    pw.println(task.getPriority() + "\t" + task.getAction());
                    current = current.next;
                }
            }
            System.out.println("File written successfully!");
        } catch(IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        TaskOrganizer organizer = new TaskOrganizer();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Task Organizer!");
        
        while(true) {
            System.out.println("\nEnter 1. To Add a Task");
            System.out.println("Enter 2. To Remove a Task");
            System.out.println("Enter 3. To Print Tasks To Console");
            System.out.println("Enter 4. To Read from a Task File");
            System.out.println("Enter 5. To Write to a Task File");
            System.out.println("Enter 9. To Quit");
            
            try {
                String choice = scanner.nextLine();
                
                switch(choice) {
                    case "1":
                        System.out.println("Enter the task's priority (0-4):");
                        int priority = Integer.parseInt(scanner.nextLine());
                        if(priority < 0 || priority > 4) {
                            System.out.println("Invalid priority! Must be between 0 and 4.");
                            continue;
                        }
                        System.out.println("Enter the task's action:");
                        String action = scanner.nextLine();
                        if(action.trim().isEmpty()) {
                            System.out.println("Action cannot be empty!");
                            continue;
                        }
                        organizer.addTask(new Task(action, priority));
                        break;
                        
                    case "2":
                        System.out.println("Enter the task's priority (0-4):");
                        priority = Integer.parseInt(scanner.nextLine());
                        if(priority < 0 || priority > 4) {
                            System.out.println("Invalid priority! Must be between 0 and 4.");
                            continue;
                        }
                        System.out.println("Enter the task's action:");
                        action = scanner.nextLine();
                        organizer.removeTask(new Task(action, priority));
                        break;
                        
                    case "3":
                        organizer.printTasks();
                        break;
                        
                    case "4":
                        System.out.println("Enter the file name:");
                        String filename = scanner.nextLine();
                        organizer.readTaskFile(filename);
                        break;
                        
                    case "5":
                        System.out.println("Enter the file name:");
                        filename = scanner.nextLine();
                        organizer.writeTaskFile(filename);
                        break;
                        
                    case "9":
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch(NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
            } catch(Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}