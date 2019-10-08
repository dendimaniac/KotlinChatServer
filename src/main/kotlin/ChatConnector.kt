import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.lang.Exception
import java.net.Socket
import java.util.*

/*
Telnet client test
{"username":"dendi","command":"Chat","message":"asdasd","timestamp":"05300324"}
 */

class ChatConnector(
    inputStream: InputStream,
    outputStream: OutputStream,
    val connector: Socket,
    val topChatter: TopChatter
) : Runnable,
    IObserver {
    private val messageIn = Scanner(inputStream)
    private val messageOut = PrintStream(outputStream, true)

    var username = ""

    init {
        ChatHistory.registerObserver(this)
    }

    override fun run() {
        startChatting()
    }

    private fun startChatting() {
        while (true) {
            val newMessage: ChatMessage = getInput()
            if (!CommandHandlers.isCommand(newMessage, messageOut, this)) {
                ChatHistory.insert(newMessage)
            }
        }
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
