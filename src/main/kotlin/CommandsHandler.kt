import com.example.chatclient.Model.Time

object CommandsHandler {
    fun isCommand(chatMessage: ChatMessage, chatConnector: ChatConnector): Boolean {
        when (chatMessage.command) {
            Commands.Quit -> {
                ChatHistory.deregisterObserver(chatConnector)
                Users.removeUsername(chatConnector.username)
                return true
            }
            Commands.History -> {
                val message = ChatHistory.getHistory(chatMessage.username)
                chatConnector.messageOut.println(message)
                return true
            }
            Commands.Users -> {
                chatConnector.messageOut.println(Users)
                return true
            }
            Commands.Top -> {
                val message = chatConnector.topChatter.getTopChatter(chatMessage.username)
                chatConnector.messageOut.println(message)
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
                        ChatMessage(chatConnector.username, Commands.Login, "Username already exists!", Time.getTime())
                    chatConnector.newMessage(responseMessage)
                }
                return true
            }
            else -> return false
        }
    }
}