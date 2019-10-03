class TopChatter : IObserver {
    private val messageCountMap = mutableMapOf<String, Int>()

    init {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        if (!messageCountMap.containsKey(chatMessage.userName)) {
            messageCountMap[chatMessage.userName] = 1
        } else {
            var count = messageCountMap[chatMessage.userName]!!
            messageCountMap[chatMessage.userName] = ++count
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