import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.util.*

/*
Telnet client test
{"username":"dendi","command":"Chat","message":"asdasd","timestamp":"05300324"}
 */

class ChatConnector(
    inputStream: InputStream,
    outputStream: OutputStream,
    private val connector: Socket,
    val topChatter: TopChatter
) : Runnable,
    IObserver {
    private val messageIn = Scanner(inputStream)
    val messageOut = PrintStream(outputStream, true)

    var canExit = false
    var username = ""

    init {
        ChatHistory.registerObserver(this)
    }

    override fun run() {
        startChatting()
    }

    private fun startChatting() {
        while (!canExit) {
            val newMessage: ChatMessage = getInput()
            if (!CommandsHandler.isCommand(newMessage, this)) {
                ChatHistory.insert(newMessage)
            }
        }
        connector.close()
    }

    private fun getInput(): ChatMessage {
        val messageAsJson = messageIn.nextLine()
        return Json.parse(ChatMessage.serializer(), messageAsJson)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        val messageAsJson = Json.stringify(ChatMessage.serializer(), chatMessage)
        messageOut.println(messageAsJson)
    }
}
