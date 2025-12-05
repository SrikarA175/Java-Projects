package gradleproject;
 import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public Task createTask(String description) {
        Task t = new Task(nextId++, description);
        tasks.add(t);
        return t;
    }

    public Task findTaskById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public boolean updateTask(int id, String newDescription) {
        Task t = findTaskById(id);
        if (t == null) return false;
        t.setDescription(newDescription);
        return true;
    }

    public boolean completeTask(int id) {
        Task t = findTaskById(id);
        if (t == null) return false;
        t.setCompleted(true);
        return true;
    }

    public boolean deleteTask(int id) {
        Task t = findTaskById(id);
        if (t == null) return false;
        return tasks.remove(t);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getCompletedTasks() {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (t.isCompleted()) result.add(t);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) return; // nothing to load first time
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(f))) {
            tasks = (List<Task>) ois.readObject();
            // recompute nextId
            int maxId = 0;
            for (Task t : tasks) {
                if (t.getId() > maxId) maxId = t.getId();
            }
            nextId = maxId + 1;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}

