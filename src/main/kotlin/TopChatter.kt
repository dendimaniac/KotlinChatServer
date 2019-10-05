class TopChatter : IObserver {
    private val messageCountMap = mutableMapOf<String, Int>()

    init {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        if (!messageCountMap.containsKey(chatMessage.username)) {
            messageCountMap[chatMessage.username] = 1
        } else {
            var count = messageCountMap[chatMessage.username]!!
            messageCountMap[chatMessage.username] = ++count
        }

        printTopChatter()
    }

    private fun printTopChatter() {
        println("Top Chatter List:")
        println(messageCountMap)
    }

    //Not currently in used!
    fun removeUser(username: String) {
        messageCountMap.remove(username)
    }
}