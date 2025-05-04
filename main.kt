import java.io.File
import java.util.Scanner

val scanner = Scanner(System.`in`)
val dictFile = File("dictionary.txt")
val todoFile = File("todo.txt")

fun main() {
    println("=== Kotlin CLI Dictionary + Todo App ===")
    while (true) {
        println(
            """
            Menu:
            1. Dictionary
            2. Todo List
            0. Exit
            """.trimIndent()
        )
        when (scanner.nextLine().trim()) {
            "1" -> dictionaryMenu()
            "2" -> todoMenu()
            "0" -> {
                println("Exiting... Bye!")
                return
            }
            else -> println("Invalid option.")
        }
    }
}

fun dictionaryMenu() {
    println("=== Dictionary ===")
    val dict = loadDictionary()
    print("Enter a word: ")
    val word = scanner.nextLine().trim().lowercase()
    val meaning = dict[word]
    if (meaning != null) {
        println("Meaning: $meaning")
    } else {
        println("Word not found.")
        print("Add it? (y/n): ")
        if (scanner.nextLine().lowercase() == "y") {
            print("Enter meaning: ")
            val newMeaning = scanner.nextLine()
            dictFile.appendText("$word=$newMeaning\n")
            println("Word added.")
        }
    }
}

fun loadDictionary(): Map<String, String> {
    if (!dictFile.exists()) dictFile.createNewFile()
    return dictFile.readLines()
        .filter { it.contains("=") }
        .associate {
            val (key, value) = it.split("=", limit = 2)
            key to value
        }
}

fun todoMenu() {
    while (true) {
        println(
            """
            === Todo Menu ===
            1. View Todos
            2. Add Todo
            3. Delete Todo
            0. Back
            """.trimIndent()
        )
        when (scanner.nextLine().trim()) {
            "1" -> viewTodos()
            "2" -> addTodo()
            "3" -> deleteTodo()
            "0" -> return
            else -> println("Invalid option.")
        }
    }
}

fun viewTodos() {
    if (!todoFile.exists()) todoFile.createNewFile()
    val todos = todoFile.readLines()
    if (todos.isEmpty()) {
        println("No todos found.")
    } else {
        println("Your Todos:")
        todos.forEachIndexed { index, todo -> println("${index + 1}. $todo") }
    }
}

fun addTodo() {
    print("Enter new todo: ")
    val todo = scanner.nextLine()
    todoFile.appendText("$todo\n")
    println("Todo added.")
}

fun deleteTodo() {
    val todos = todoFile.readLines().toMutableList()
    if (todos.isEmpty()) {
        println("Todo list is empty.")
        return
    }
    viewTodos()
    print("Enter number to delete: ")
    val index = scanner.nextLine().toIntOrNull()
    if (index != null && index in 1..todos.size) {
        todos.removeAt(index - 1)
        todoFile.writeText(todos.joinToString("\n"))
        println("Todo deleted.")
    } else {
        println("Invalid index.")
    }
}
