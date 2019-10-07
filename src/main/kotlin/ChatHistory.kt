import com.example.chatclient.Model.Time
import kotlinx.serialization.json.Json

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

    fun getHistory(username: String): String {
        var history: ChatMessage
        var historyMessage = ""
        messageSet.forEach { message ->
            historyMessage += if (messageSet.indexOf(message) != messageSet.size - 1) {
                "${Json.stringify(ChatMessage.serializer(), message)}|"
            } else {
                Json.stringify(ChatMessage.serializer(), message)
            }
        }
        history = ChatMessage(username, Commands.History, historyMessage, Time.getTime())
        return Json.stringify(ChatMessage.serializer(), history)
    }
}