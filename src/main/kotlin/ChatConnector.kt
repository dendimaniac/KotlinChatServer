import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.util.*

/*
Telnet client test
{"message":"asdasd", "username":"dendi", "timestamp":"05300324"}
 */

class ChatConnector(inputStream: InputStream, outputStream: OutputStream, private val connector: Socket) : Runnable, IObserver {
    private val messageIn = Scanner(inputStream)
    private val messageOut = PrintStream(outputStream, true)

    private var canExit = false
    private var hasStart = false
    private var username = ""

    init {
        ChatHistory.registerObserver(this)
    }

    override fun run() {
        startChatting()
    }

    private fun startChatting() {
        while (!canExit) {
            val newMessage: ChatMessage = getInput()
            if (!isCommand(newMessage.message)) {
                ChatHistory.insert(newMessage)
            }
        }
        connector.close()
    }

    private fun getInput(): ChatMessage {
        val messageAsJson = messageIn.nextLine()
        println(messageAsJson)
        return Json.parse(ChatMessage.serializer(), messageAsJson)
    }

    private fun isCommand(input: String): Boolean {
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
        val messageAsJson = Json.stringify(ChatMessage.serializer(), chatMessage)
        messageOut.println(messageAsJson)
    }
}
