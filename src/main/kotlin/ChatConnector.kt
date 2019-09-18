import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ChatConnector(inputStream: InputStream, outputStream: OutputStream, private val connector: Socket) : Runnable, IObserver {
    private val messageIn = Scanner(inputStream)
    private val messageOut = PrintStream(outputStream)

    private var canExit = false
    private var hasStart = false
    private var username = ""
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    init {
        ChatHistory.registerObserver(this)
    }

    override fun run() {
        getNewUsername()

        startChatting()
    }

    private fun getNewUsername() {
        do {
            messageOut.print("Please enter a new username: ")
            username = Json.parse(ChatMessage.serializer(), getInput()).message
            if (Users.checkUsernameExist(username)) {
                messageOut.println("Username already exist!")
            }
        } while (Users.checkUsernameExist(username))
        Users.insertUsername(username)
        messageOut.println("Happy chatting!")
        hasStart = true
    }

    private fun startChatting() {
        while (!canExit) {
            messageOut.flush()
            val newMessage: ChatMessage = Json.parse(ChatMessage.serializer(), getInput())
            if (!checkCommand(newMessage.message)) {
                ChatHistory.insert(newMessage)
            }
        }
        connector.close()
    }

    private fun getInput(): String {
        val line = messageIn.nextLine()

        val current = LocalDateTime.now()
        val timestamp = current.format(formatter)

        val newMessage = ChatMessage(line, username, timestamp)
        return Json.stringify(ChatMessage.serializer(), newMessage)
    }

    private fun checkCommand(input: String): Boolean {
        when {
            input == "!exit" -> {
                ChatHistory.deregisterObserver(this)
                Users.removeUsername(username)
                canExit = true
                return true
            }
            input == "!history" -> {
                messageOut.println(ChatHistory)
                return true
            }
            input == "!users" -> {
                messageOut.println(Users)
                return true
            }
            input.indexOf("!") == 0 -> {
                messageOut.println("Invalid command!")
                return true
            }
            else -> return false
        }
    }

    override fun newMessage(chatMessage: ChatMessage) {
        if (!hasStart) return
        messageOut.println(chatMessage)
    }
}
