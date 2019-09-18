class ChatConsole: IObserver {
    init {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        println(chatMessage)
    }
}