object ChatHistory : IObservable {
    override val observerSet: MutableSet<IObserver> = mutableSetOf()
    private val messageSet: MutableSet<ChatMessage> = mutableSetOf()

    override fun registerObserver(newObserver: IObserver) {
        observerSet.add(newObserver)
    }

    override fun deregisterObserver(targetObserver: IObserver) {
        observerSet.remove(targetObserver)
    }

    override fun notifyObservers(message: ChatMessage) {
        observerSet.forEach {
            it.newMessage(message)
        }
    }

    fun insert(message: ChatMessage) {
        messageSet.add(message)
        notifyObservers(message)
    }

    override fun toString(): String {
        var allMessage = ""
        for (message in messageSet) {
            allMessage += "$message\r\n"
        }
        return allMessage
    }
}