import com.example.chatclient.Model.Time
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
    private val topChatter: TopChatter
) : Runnable,
    IObserver {
    private val messageIn = Scanner(inputStream)
    private val messageOut = PrintStream(outputStream, true)

    private var canExit = false
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
            if (!isCommand(newMessage)) {
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

    private fun isCommand(chatMessage: ChatMessage): Boolean {
        when (chatMessage.command) {
            Commands.Quit -> {
                ChatHistory.deregisterObserver(this)
                Users.removeUsername(username)
                canExit = true
                return true
            }
            Commands.History -> {
                val message = ChatHistory.getHistory(chatMessage.username)
                messageOut.println(message)
                return true
            }
            Commands.Users -> {
                messageOut.println(Users)
                return true
            }
            Commands.Top -> {
                val message = topChatter.getTopChatter(chatMessage.username)
                messageOut.println(message)
                return true
            }
            Commands.Login -> {
                var responseMessage: ChatMessage
                if (!Users.checkUsernameExist(chatMessage.username)) {
                    username = chatMessage.username
                    Users.insertUsername(username)
                    responseMessage =
                        ChatMessage(username, Commands.Chat, "$username has joined the chat!", Time.getTime())
                    ChatHistory.insert(responseMessage)
                } else {
                    responseMessage = ChatMessage(username, Commands.Login, "Username already exists!", Time.getTime())
                    newMessage(responseMessage)
                }
                return true
            }
            else -> return false
        }
    }

    override fun newMessage(chatMessage: ChatMessage) {
        val messageAsJson = Json.stringify(ChatMessage.serializer(), chatMessage)
        messageOut.println(messageAsJson)
    }
}
