import com.example.chatclient.Model.Time
import java.io.PrintStream

object CommandHandlers {
    fun isCommand(chatMessage: ChatMessage, messageOut: PrintStream, chatConnector: ChatConnector): Boolean {
        when (chatMessage.command) {
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
                val message = chatConnector.topChatter.getTopChatter(chatMessage.username)
                messageOut.println(message)
                return true
            }
            Commands.Login -> {
                val responseMessage: ChatMessage
                if (!Users.checkUsernameExist(chatMessage.username)) {
                    chatConnector.username = chatMessage.username
                    Users.insertUsername(chatConnector.username)
                    responseMessage =
                        ChatMessage(
                            chatConnector.username,
                            Commands.Chat,
                            "${chatConnector.username} has joined the chat!",
                            Time.getTime()
                        )
                    ChatHistory.insert(responseMessage)
                } else {
                    responseMessage =
                        ChatMessage(chatMessage.username, Commands.Login, "Username already exists!", Time.getTime())
                    chatConnector.newMessage(responseMessage)
                }
                return true
            }
            else -> return false
        }
    }
}